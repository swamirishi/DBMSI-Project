package diskmgr;

import basicpatternheap.BasicPattern;
import bufmgr.PageNotReadException;
import global.AttrOperator;
import global.AttrType;
import global.LID;
import global.QID;
import heap.HFBufMgrException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import index.IndexException;
import iterator.*;
import iterator.bp.BPFileScan;
import iterator.interfaces.IteratorI;
import qiterator.QuadrupleSort;
import quadrupleheap.Quadruple;
import quadrupleheap.TScan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stream extends IteratorI<BasicPattern> {
    public static QuadrupleSort quadrupleSort;
    public static RDFDB rdfDB;
    public static LID subjectFilter;
    public static LID predicateFilter;
    public static LID objectFilter;
    public static Float confidenceFilter;

//    LIDIndexScan<Void> subjectIndexScan;
//    LIDIndexScan<Void> predicateIndexScan;
//    LIDIndexScan<Void> objectIndexScan;

    public static final int maxLabelLen = 200;
    public int orderType;
    IteratorI<BasicPattern> bpFileScan;

    public Stream(RDFDB rdfdb, int orderType, LID subjectFil, LID predicateFil,
                  LID objectFil, Double confidenceFil) throws Exception {
        rdfDB = rdfdb;
        this.orderType = orderType;

        subjectFilter = subjectFil;
        predicateFilter = predicateFil;
        objectFilter = objectFil;
        confidenceFilter = confidenceFil == null ? 0 : confidenceFil.floatValue();

        List<FldSpec> projectionList1 = new ArrayList<FldSpec>();
        projectionList1.add(BasicPattern.getValueProject(new RelSpec(RelSpec.outer)));
        projectionList1.addAll(BasicPattern.getProjectListForNode(Quadruple.SUBJECT_NODE_INDEX, new RelSpec(RelSpec.outer)));
        projectionList1.addAll(BasicPattern.getProjectListForNode(Quadruple.OBJECT_NODE_INDEX, new RelSpec(RelSpec.outer)));
//        projectionList1.addAll(BasicPattern.getProjectListForNode(Quadruple.PREDICTE_NODE_INDEX, new RelSpec(RelSpec.outer)));

        FldSpec[] fldSpecs = projectionList1.toArray(new FldSpec[projectionList1.size()]);

        CondExpr[] rightFilter = getRightFilter();
        bpFileScan = new BPFileScan(RDFDB.quadrupleHeapFileName, Quadruple.headerTypes, Quadruple.strSizes,
                (short) Quadruple.headerTypes.length, fldSpecs.length, fldSpecs, rightFilter);
    }

    private CondExpr[] getRightFilter() {
        List<CondExpr> exprs = new ArrayList<>();
        if (subjectFilter != null) {
            List<CondExpr> subjectCondExpr = getCondExprs(subjectFilter, Quadruple.SUBJECT_NODE_INDEX);
            exprs.addAll(subjectCondExpr);
        }

        if (predicateFilter != null) {
            List<CondExpr> predicateCondExpr = getCondExprs(predicateFilter, Quadruple.PREDICTE_NODE_INDEX);
            exprs.addAll(predicateCondExpr);
        }

        if (objectFilter != null) {
            List<CondExpr> objectCondExpr = getCondExprs(objectFilter, Quadruple.OBJECT_NODE_INDEX);
            exprs.addAll(objectCondExpr);
        }

        if (confidenceFilter != null) {
            CondExpr expr = new CondExpr();
            expr.op = new AttrOperator(AttrOperator.aopGE);
            expr.next = null;
            expr.type1 = new AttrType(AttrType.attrSymbol);
            expr.type2 = new AttrType(AttrType.attrReal);
            expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Quadruple.VALUE_FLD);
            expr.operand2.real = confidenceFilter.floatValue();
        }

        CondExpr[] condExprs = new CondExpr[exprs.size() + 1];
        for (int i = 0; i < exprs.size(); i++) {
            condExprs[i] = exprs.get(i);
        }
        condExprs[exprs.size()] = null;

        return condExprs;
    }

    public List<CondExpr> getCondExprs(LID id, int nodeIndex) {
        int[] offsets = Quadruple.getPageNumberAndSlot(nodeIndex);
        List<CondExpr> exprs = new ArrayList<>();

        CondExpr expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[0]);
        expr.operand2.integer = id.getPageNo().pid;
        exprs.add(expr);

        expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[1]);
        expr.operand2.integer = id.getSlotNo();
        exprs.add(expr);

        return exprs;
    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException, IndexException, JoinsException {
        bpFileScan.close();
    }

    @Override
    public BasicPattern get_next() throws IOException, JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, Exception {
        return bpFileScan.get_next();
    }

    @Override
    public void close() throws IOException, JoinsException, SortException, IndexException, HFBufMgrException {
        this.closeStream();
    }
}

