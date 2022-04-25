
package iterator;
import bpiterator.BasicPatternUtils;
import global.*;
import heap.Tuple;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.interfaces.pnodeSplayPQI;
import utils.supplier.iterator.pnodesplaynode.PNodeSplayNodeSupplier;
import utils.supplier.iterator.pnodesplaynode.RIDPNodeSplayNodeSupplier;

import java.io.*;

/**
 * Implements a sorted binary tree (extends class pnodePQ).
 * Implements the <code>enq</code> and the <code>deq</code> functions.
 */
public class pnodeSplayPQ extends pnodeSplayPQI<Tuple>
{
	public pnodeSplayPQ() {
		super();
	}
	
	@Override
	public int compare(AttrType fldType,
	                   Tuple t1,
	                   int t1_fld_no,
	                   Tuple t2,
	                   int t2_fld_no) throws IOException, UnknowAttrType, TupleUtilsException {
		return TupleUtils.CompareTupleWithTuple(fldType,t1,t1_fld_no,t2,t2_fld_no);
	}
	
	public pnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order) {
		super(fldNo, fldType, order);
	}
	
	@Override
	protected PNodeSplayNodeSupplier<Tuple> getPNodeSplayNodeSupplier() {
		return RIDPNodeSplayNodeSupplier.getSupplier();
	}
	
	@Override
	protected pnodeSplayNodeI<Tuple> getDummyPNodeSPlayNode() {
		return pnodeSplayNode.dummy;
	}
}
