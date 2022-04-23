package iterator.bp;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.BPID;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import iterator.*;
import iterator.interfaces.IteratorI;
import iterator.interfaces.NestedLoopsJoinsI;
import quadrupleheap.Quadruple;
import utils.supplier.hfile.BPIDHFileSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

public class BPNestedLoopJoin extends NestedLoopsJoinsI<BPID, BasicPattern> {

    public BPNestedLoopJoin(AttrType[] in1,
                            int len_in1,
                            short[] t1_str_sizes,
                            AttrType[] in2,
                            int len_in2,
                            short[] t2_str_sizes,
                            int amt_of_mem,
                            IteratorI<BasicPattern> am1,
                            String relationName,
                            CondExpr[] outFilter,
                            CondExpr[] rightFilter,
                            FldSpec[] proj_list,
                            int n_out_flds) throws IOException, NestedLoopException {
        super(in1,
              len_in1,
              t1_str_sizes,
              in2,
              len_in2,
              t2_str_sizes,
              amt_of_mem,
              am1,
              relationName,
              outFilter,
              rightFilter,
              proj_list,
              n_out_flds);
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
}
