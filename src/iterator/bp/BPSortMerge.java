package iterator.bp;

import basicpatternheap.BasicPattern;
import bpiterator.BasicPatternUtils;
import global.AttrType;
import global.BPID;
import global.TupleOrder;
import heap.Tuple;
import iterator.*;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortMergeI;
import utils.supplier.hfile.BPIDHFileSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.iterator.iobuf.BPIDIoBufSupplier;
import utils.supplier.iterator.iobuf.IoBufSupplier;
import utils.supplier.iterator.sort.BPIDSortSupplier;
import utils.supplier.iterator.sort.SortSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

/**
 * This file contains the interface for the sort_merg joins.
 * We name the two relations being joined as R and S.
 * This file contains an implementation of the sort merge join
 * algorithm as described in the Shapiro paper. It makes use of the external
 * sorting utility to generate runs, and then uses the iterator interface to
 * get successive tuples for the final merge.
 */
public class BPSortMerge extends SortMergeI<BPID, BasicPattern>
{
	/**
	 * constructor,initialization
	 *
	 * @param in1          Array containing field types of R
	 * @param len_in1      # of columns in R
	 * @param s1_sizes     shows the length of the string fields in R.
	 * @param in2          Array containing field types of S
	 * @param len_in2      # of columns in S
	 * @param s2_sizes     shows the length of the string fields in S
	 * @param join_col_in1 The col of R to be joined with S
	 * @param sortFld1Len  the length of sorted field in R
	 * @param join_col_in2 the col of S to be joined with R
	 * @param sortFld2Len  the length of sorted field in S
	 * @param amt_of_mem   IN PAGES
	 * @param am1          access method for left input to join
	 * @param am2          access method for right input to join
	 * @param in1_sorted   is am1 sorted?
	 * @param in2_sorted   is am2 sorted?
	 * @param order        the order of the tuple: assending or desecnding?
	 * @param outFilter    Ptr to the output filter
	 * @param proj_list    shows what input fields go where in the output tuple
	 * @param n_out_flds   number of outer relation fileds
	 * @throws JoinNewFailed       allocate failed
	 * @throws JoinLowMemory       memory not enough
	 * @throws SortException       exception from sorting
	 * @throws TupleUtilsException exception from using tuple utils
	 * @throws IOException         some I/O fault
	 */
	public BPSortMerge(AttrType[] in1,
                     int len_in1,
                     short[] s1_sizes,
                     AttrType[] in2,
                     int len_in2,
                     short[] s2_sizes,
                     int join_col_in1,
                     int sortFld1Len,
                     int join_col_in2,
                     int sortFld2Len,
                     int amt_of_mem,
                     IteratorI<BasicPattern> am1,
                     IteratorI<BasicPattern> am2,
                     boolean in1_sorted,
                     boolean in2_sorted,
                     TupleOrder order,
                     CondExpr[] outFilter,
                     FldSpec[] proj_list,
                     int n_out_flds) throws JoinNewFailed, JoinLowMemory, SortException, TupleUtilsException, IOException {
		super(in1,
		      len_in1,
		      s1_sizes,
		      in2,
		      len_in2,
		      s2_sizes,
		      join_col_in1,
		      sortFld1Len,
		      join_col_in2,
		      sortFld2Len,
		      amt_of_mem,
		      am1,
		      am2,
		      in1_sorted,
		      in2_sorted,
		      order,
		      outFilter,
		      proj_list,
		      n_out_flds);
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
	public IoBufSupplier<BPID, BasicPattern> getIoBufSupplier() {
		return BPIDIoBufSupplier.getSupplier();
	}
	
	@Override
	public SortSupplier<BPID, BasicPattern> getSortSupplier() {
		return BPIDSortSupplier.getSupplier();
	}

	@Override
	public int compare(AttrType fldType, BasicPattern t1, int t1_fld_no, BasicPattern t2, int t2_fld_no) {
		return BasicPatternUtils.CompareBPWithBP(fldType,t1,t1_fld_no,t2,t2_fld_no,true);
	}
}


