package iterator.bp;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.BPID;
import global.JoinCondition;
import global.LID;
import index.indexOptions.IDIndexOptions;
import iterator.*;
import iterator.interfaces.IndexNestedLoopsJoinsI;
import iterator.interfaces.IteratorI;
import utils.supplier.hfile.BPIDHFileSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

public class BPIndexNestedLoopJoins extends IndexNestedLoopsJoinsI<BPID, BasicPattern> {
    /**
     * constructor
     * Initialize the two relations which are joined, including relation type,
     *
     * @param in1              Array containing field types of R.
     * @param len_in1          # of columns in R.
     * @param t1_str_sizes     shows the length of the string fields.
     * @param in2              Array containing field types of S
     * @param len_in2          # of columns in S
     * @param t2_str_sizes     shows the length of the string fields.
     * @param amt_of_mem       IN PAGES
     * @param am1              access method for left i/p to join
     * @param relationName     access hfapfile for right i/p to join
     * @param joinCondition
     * @param subjectFilter
     * @param objectFilter
     * @param predicateFilter
     * @param confidenceFilter
     * @param indexOptions
     * @param proj_list        shows what input fields go where in the output tuple
     * @param n_out_flds       number of outer relation fileds
     * @throws IOException         some I/O fault
     * @throws NestedLoopException exception from this class
     */
    public BPIndexNestedLoopJoins(AttrType[] in1, int len_in1, short[] t1_str_sizes, AttrType[] in2, int len_in2, short[] t2_str_sizes, int amt_of_mem, IteratorI<BasicPattern> am1, String relationName, JoinCondition joinCondition, LID subjectFilter, LID objectFilter, LID predicateFilter, Float confidenceFilter, IDIndexOptions<BasicPattern> indexOptions, FldSpec[] proj_list, int n_out_flds) throws IOException, NestedLoopException {
        super(in1, len_in1, t1_str_sizes, in2, len_in2, t2_str_sizes, amt_of_mem, am1, relationName, joinCondition, subjectFilter, objectFilter, predicateFilter, confidenceFilter, indexOptions, proj_list, n_out_flds);
    }

    @Override
    public IDSupplier<BPID> getIDSupplier() {
        return BPIDSupplier.getSupplier();
    }

    @Override
    public TupleSupplier<BasicPattern> getTupleSupplier() {
        return BPIDTupleSupplier.getSupplier();
    }

    @Override
    public HFileSupplier<BPID, BasicPattern> getHFileSupplier() {
        return BPIDHFileSupplier.getSupplier();
    }

    @Override
    protected boolean predictedEvaluation(CondExpr[] p, BasicPattern t1, BasicPattern t2, AttrType[] in1, AttrType[] in2) throws Exception {
        boolean res = PredEval.Eval(p, t1, t2, in1, in2);
        return res;
    }

    @Override
    protected void projectionEvaluation(BasicPattern t1, AttrType[] type1, BasicPattern t2, AttrType[] type2, BasicPattern Jtuple, FldSpec[] perm_mat, int nOutFlds) throws Exception {
        BasicPatternProjection.Join(t1, type1, t2, type2, Jtuple, perm_mat, nOutFlds);
    }
}
