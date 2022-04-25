
package iterator.bp;

import basicpatternheap.BasicPattern;
import bpiterator.BasicPatternUtils;
import global.AttrType;
import global.TupleOrder;
import heap.Tuple;
import iterator.TupleUtilsException;
import iterator.UnknowAttrType;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.interfaces.pnodeSplayPQI;
import utils.supplier.iterator.pnodesplaynode.BPIDPNodeSplayNodeSupplier;
import utils.supplier.iterator.pnodesplaynode.PNodeSplayNodeSupplier;

import java.io.IOException;

/**
 * Implements a sorted binary tree (extends class pnodePQ).
 * Implements the <code>enq</code> and the <code>deq</code> functions.
 */
public class BPpnodeSplayPQ extends pnodeSplayPQI<BasicPattern>
{
	private boolean referenceBased = true;
	public BPpnodeSplayPQ(boolean referenceBased) {
		super();
		this.referenceBased = referenceBased;
	}
	
	@Override
	public int compare(AttrType fldType,
	                   BasicPattern t1,
	                   int t1_fld_no,
	                   BasicPattern t2,
	                   int t2_fld_no) throws IOException, UnknowAttrType, TupleUtilsException {
		return BasicPatternUtils.CompareBPWithBP(fldType,t1,t1_fld_no,t2,t2_fld_no,this.referenceBased);
	}
	
	public BPpnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order,boolean referenceBased) {
		super(fldNo, fldType, order);
		this.referenceBased = referenceBased;
	}
	
	@Override
	protected PNodeSplayNodeSupplier<BasicPattern> getPNodeSplayNodeSupplier() {
		return BPIDPNodeSplayNodeSupplier.getSupplier();
	}
	
	@Override
	protected pnodeSplayNodeI<BasicPattern> getDummyPNodeSPlayNode() {
		return BPpnodeSplayNode.dummy;
	}
}
