package iterator.interfaces;

import basicpatternheap.BasicPattern;
import btree.KeyClass;
import btree.quadraple.QIDBTFileScan;
import btree.quadraple.QIDBTreeFile;
import btree.quadraple.QIDKeyDataEntry;
import btree.quadraple.QIDLeafData;
import bufmgr.PageNotReadException;
import global.*;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import index.IndexException;
import index.indexOptions.IDIndexOptions;
import index.indexOptions.QIDIndexOptions;
import iterator.*;
import quadrupleheap.Quadruple;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;
import utils.supplier.keyclass.QIDKeyClassManager;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This file contains an implementation of the nested loops join
 * algorithm as described in the Shapiro paper.
 * The algorithm is extremely simple:
 * <p>
 * foreach tuple r in R do
 * foreach tuple s in S do
 * if (ri == sj) then add (r, s) to the result.
 */
public abstract class IndexNestedLoopsJoinsI<I extends ID, T extends Tuple> extends IteratorI<T> {
    private AttrType _in1[], _in2[];
    private int in1_len, in2_len;
    private IteratorI<T> outer;
    private short t2_str_sizescopy[];
    private JoinCondition joinCondition;
    private LID rightSubjectFilter;
    private LID rightObjectFilter;
    private LID rightPredicateFilter;
    private Float confidenceFilter;
    private int n_buf_pgs;        // # of buffer pages available.
    private boolean done,         // Is the join complete
            get_from_outer;                 // if TRUE, a tuple is got from outer
    private T outer_tuple, inner_tuple;
    private T Jtuple;           // Joined tuple
    private FldSpec perm_mat[];
    private int nOutFlds;
    public QIDBTreeFile<List<?>> btreeIndexFile;
    private QIDBTFileScan<List<?>> inner;
    private IDIndexOptions<T> indexOptions;
    private String relationName;
    private int index;
    CondExpr[] confidenceCondExpr;

    public abstract IDSupplier<I> getIDSupplier();

    public abstract TupleSupplier<T> getTupleSupplier();

    public abstract HFileSupplier<I, T> getHFileSupplier();

    protected abstract boolean predictedEvaluation(CondExpr p[], T t1, T t2, AttrType in1[],
                                                   AttrType in2[]) throws Exception;

    protected abstract void projectionEvaluation(T t1, AttrType type1[],
                                                 T t2, AttrType type2[],
                                                 T Jtuple, FldSpec perm_mat[],
                                                 int nOutFlds) throws Exception;

    /**
     * constructor
     * Initialize the two relations which are joined, including relation type,
     *
     * @param in1          Array containing field types of R.
     * @param len_in1      # of columns in R.
     * @param t1_str_sizes shows the length of the string fields.
     * @param in2          Array containing field types of S
     * @param len_in2      # of columns in S
     * @param t2_str_sizes shows the length of the string fields.
     * @param amt_of_mem   IN PAGES
     * @param am1          access method for left i/p to join
     * @param relationName access hfapfile for right i/p to join
     * @param outFilter    select expressions
     * @param rightFilter  reference to filter applied on right i/p
     * @param proj_list    shows what input fields go where in the output tuple
     * @param n_out_flds   number of outer relation fileds
     * @throws IOException         some I/O fault
     * @throws NestedLoopException exception from this class
     */
    public IndexNestedLoopsJoinsI(AttrType in1[],
                                  int len_in1,
                                  short t1_str_sizes[],
                                  AttrType in2[],
                                  int len_in2,
                                  short t2_str_sizes[],
                                  int amt_of_mem,
                                  IteratorI<T> am1,
                                  String relationName,
                                  JoinCondition joinCondition,
                                  LID subjectFilter,
                                  LID predicateFilter,
                                  LID objectFilter,
                                  Float confidenceFilter,
                                  IDIndexOptions<T> indexOptions,
                                  FldSpec proj_list[],
                                  int n_out_flds) throws IOException, NestedLoopException {

        _in1 = new AttrType[in1.length];
        _in2 = new AttrType[in2.length];
        System.arraycopy(in1, 0, _in1, 0, in1.length);
        System.arraycopy(in2, 0, _in2, 0, in2.length);
        in1_len = len_in1;
        in2_len = len_in2;
        this.indexOptions = indexOptions;

        outer = am1;
        t2_str_sizescopy = t2_str_sizes;
        inner_tuple = getTupleSupplier().getTuple();
        Jtuple = getTupleSupplier().getTuple();
        this.joinCondition = joinCondition;
        this.rightSubjectFilter = subjectFilter;
        this.rightObjectFilter = objectFilter;
        this.rightPredicateFilter = predicateFilter;
        this.confidenceFilter = confidenceFilter;
        this.relationName = relationName;

        n_buf_pgs = amt_of_mem;
        inner = null;
        done = false;
        get_from_outer = true;

        AttrType[] Jtypes = new AttrType[n_out_flds];
        short[] t_size;

        perm_mat = proj_list;
        nOutFlds = n_out_flds;
        try {
            t_size = TupleUtils.setup_op_tuple(Jtuple,
                    Jtypes,
                    in1,
                    len_in1,
                    in2,
                    len_in2,
                    t1_str_sizes,
                    t2_str_sizes,
                    proj_list,
                    nOutFlds);
        } catch (TupleUtilsException e) {
            throw new NestedLoopException(e, "TupleUtilsException is caught by NestedLoopsJoins.java");
        }


        try {
            boolean subjectFilterGiven = rightSubjectFilter != null ? true : false;
            boolean objectFilterGiven = rightObjectFilter != null ? true : false;
            boolean joinOnSubject = joinCondition.isJoinOnSubject();
            index = QIDIndexOptions.getIndexOption(joinOnSubject, subjectFilterGiven, objectFilterGiven);
            btreeIndexFile = indexOptions.getBTFile(index);

        } catch (Exception e) {
            throw new NestedLoopException(e, "Create new heapfile failed.");
        }
        
        confidenceCondExpr = getConfidenceCondExpr();
    }

    private CondExpr[] getConfidenceCondExpr() {
        CondExpr[] exprs = new CondExpr[2];
        CondExpr expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopGE);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrReal);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Quadruple.VALUE_FLD);
        expr.operand2.real = confidenceFilter.floatValue();
        exprs[0] = expr;
        exprs[1] = null;
        return exprs;
    }

    /**
     * @return The joined tuple is returned
     * @throws IOException               I/O errors
     * @throws JoinsException            some join exception
     * @throws IndexException            exception from super class
     * @throws InvalidTupleSizeException invalid tuple size
     * @throws InvalidTypeException      tuple type not valid
     * @throws PageNotReadException      exception from lower layer
     * @throws TupleUtilsException       exception from using tuple utilities
     * @throws PredEvalException         exception from PredEval class
     * @throws SortException             sort exception
     * @throws LowMemException           memory error
     * @throws UnknowAttrType            attribute type unknown
     * @throws UnknownKeyTypeException   key type unknown
     * @throws Exception                 other exceptions
     */
    public T get_next() throws IOException, JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, Exception {
        // This is a DUMBEST form of a join, not making use of any key information...

        KeyClassManager idListKeyClassManager = indexOptions.indexKeyClassManagerForIndex(index);

        if (done) {
            return null;
        }

        do {
            // If get_from_outer is true, Get a tuple from the outer, delete
            // an existing scan on the file, and reopen a new scan on the file.
            // If a get_next on the outer returns DONE?, then the nested loops
            //join is done too.

            if (get_from_outer) {
                get_from_outer = false;
                if (inner != null)     // If this not the first time,
                {
                    // close scan
                    inner.DestroyBTreeFileScan();
                    inner = null;
                }

                if ((outer_tuple = outer.get_next()) == null) {
                    done = true;
                    if (inner != null) {
                        inner.DestroyBTreeFileScan();
                        inner = null;
                    }

                    return null;
                }

                try {
                    if (!evalLeftRightFilter()) {
                        get_from_outer = true;
                        continue;
                    }
                    KeyClass lowKey = idListKeyClassManager.getKeyClass(getMinFilterList());
                    KeyClass hiKey = idListKeyClassManager.getKeyClass(getMaxFilterList());
                    inner = btreeIndexFile.new_scan(lowKey, hiKey);
                } catch (Exception e) {
                    throw new NestedLoopException(e, "openScan failed");
                }
            }  // ENDS: if (get_from_outer == TRUE)

            // The next step is to get a tuple from the inner,
            // while the inner is not completely scanned && there
            // is no match (with pred),get a tuple from the inner.

            QIDKeyDataEntry filterQidKeyDataEntry = inner.get_next();
            while (filterQidKeyDataEntry != null) {
                I qid = (I) ((QIDLeafData) filterQidKeyDataEntry.data).getData();
                inner_tuple = this.getHFileSupplier().getHFile(relationName).getRecord(qid);
                // Apply a projection on the outer and inner tuples.
                inner_tuple.setHdr((short) in2_len, _in2, t2_str_sizescopy);

                if (this.predictedEvaluation(confidenceCondExpr, inner_tuple, null, _in2, null) == true) {
                    this.projectionEvaluation(outer_tuple, _in1, inner_tuple, _in2, Jtuple, perm_mat, nOutFlds);
                    return Jtuple;
                }
                filterQidKeyDataEntry = inner.get_next();
            }

            // There has been no match. (otherwise, we would have
            //returned from t//he while loop. Hence, inner is
            //exhausted, => set get_from_outer = TRUE, go to top of loop

            get_from_outer = true; // Loop back to top and get next outer tuple.
        } while (true);
    }

    private List<?> getMaxFilterList() {
        NID leftFilter = ((BasicPattern) outer_tuple).getNode(joinCondition.getLeftNodePosition());
        LID rightSubjectFilter = this.rightSubjectFilter != null ? this.rightSubjectFilter : new LID(new PageId(QIDKeyClassManager.MAX_PAGE_NO), QIDKeyClassManager.MAX_SLOT_NO);
        LID rightPredicateFilter = this.rightPredicateFilter != null ? this.rightPredicateFilter : new LID(new PageId(QIDKeyClassManager.MAX_PAGE_NO), QIDKeyClassManager.MAX_SLOT_NO);
        LID rightObjectFilter = this.rightObjectFilter != null ? this.rightObjectFilter : new LID(new PageId(QIDKeyClassManager.MAX_PAGE_NO), QIDKeyClassManager.MAX_SLOT_NO);

        if (joinCondition.isJoinOnSubject()) {
            rightSubjectFilter = leftFilter.returnLid();
        }else{
            rightObjectFilter = leftFilter.returnLid();
        }

        switch (index) {
            case 1:
                return Arrays.asList(rightSubjectFilter, rightObjectFilter, rightPredicateFilter);
            case 2:
                return Arrays.asList(rightSubjectFilter, rightPredicateFilter);
            case 3:
                return Arrays.asList(rightObjectFilter, rightSubjectFilter, rightPredicateFilter);
            case 4:
                return Arrays.asList(rightObjectFilter, rightPredicateFilter);
        }
        return null;
    }

    private List<?> getMinFilterList() {
        NID leftFilter = ((BasicPattern) outer_tuple).getNode(joinCondition.getLeftNodePosition());
        LID rightSubjectFilter = this.rightSubjectFilter != null ? this.rightSubjectFilter : new LID(new PageId(0), 0);
        LID rightPredicateFilter = this.rightPredicateFilter != null ? this.rightPredicateFilter : new LID(new PageId(0), 0);
        LID rightObjectFilter = this.rightObjectFilter != null ? this.rightObjectFilter : new LID(new PageId(0), 0);

        if (joinCondition.isJoinOnSubject()) {
            rightSubjectFilter = leftFilter.returnLid();
        }else{
            rightObjectFilter = leftFilter.returnLid();
        }

        switch (index) {
            case 1:
                return Arrays.asList(rightSubjectFilter, rightObjectFilter, rightPredicateFilter);
            case 2:
                return Arrays.asList(rightSubjectFilter, rightPredicateFilter);
            case 3:
                return Arrays.asList(rightObjectFilter, rightSubjectFilter, rightPredicateFilter);
            case 4:
                return Arrays.asList(rightObjectFilter, rightPredicateFilter);
        }
        return null;
    }

    private boolean evalLeftRightFilter() {
//        ((BasicPattern)outer_tuple).printBasicPatternValues();
        NID leftFilter = ((BasicPattern) outer_tuple).getNode(joinCondition.getLeftNodePosition());
        if (joinCondition.isJoinOnSubject()) {
            if(rightSubjectFilter==null) {
                return true;
            }
            return leftFilter.returnLid().equals(rightSubjectFilter);
        } else {
            if(rightObjectFilter==null){
                return true;
            }
            return leftFilter.returnLid().equals(rightObjectFilter);
        }
    }

    /**
     * implement the abstract method close() from super class Iterator
     * to finish cleaning up
     *
     * @throws IOException    I/O error from lower layers
     * @throws JoinsException join error from lower layers
     * @throws IndexException index access error
     */
    public void close() throws JoinsException, IOException, IndexException {
        if (!closeFlag) {
            try {
                btreeIndexFile.close();
                outer.close();
            } catch (Exception e) {
                throw new JoinsException(e, "NestedLoopsJoin.java: error in closing iterator.");
            }
            closeFlag = true;
        }
    }
}






