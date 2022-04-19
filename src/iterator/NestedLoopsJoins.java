package iterator;
   

import heap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import index.*;
import iterator.interfaces.IteratorI;
import iterator.interfaces.NestedLoopsJoinsI;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.hfile.RIDHFileSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.lang.*;
import java.io.*;
public class NestedLoopsJoins  extends NestedLoopsJoinsI<RID,Tuple>
{

	public NestedLoopsJoins(AttrType[] in1,
	                        int len_in1,
	                        short[] t1_str_sizes,
	                        AttrType[] in2,
	                        int len_in2,
	                        short[] t2_str_sizes,
	                        int amt_of_mem,
	                        IteratorI<Tuple> am1,
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
	public IDSupplier<RID> getIDSupplier() {
		return RIDSupplier.getSupplier();
	}
	
	@Override
	public TupleSupplier<Tuple> getTupleSupplier() {
		return RIDTupleSupplier.getSupplier();
	}
	
	@Override
	public HFileSupplier<RID, Tuple> getHFileSupplier() {
		return RIDHFileSupplier.getSupplier();
	}
	
	@Override
	protected boolean predictedEvaluation(CondExpr[] p, Tuple t1, Tuple t2, AttrType[] in1, AttrType[] in2) throws InvalidTupleSizeException, FieldNumberOutOfBoundException, IOException, UnknowAttrType, InvalidTypeException, PredEvalException {
		return PredEval.Eval(p, t1, t2, in1, in2);
	}
}






