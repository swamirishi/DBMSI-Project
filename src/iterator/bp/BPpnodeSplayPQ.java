
package iterator.bp;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.TupleOrder;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.interfaces.pnodeSplayPQI;
import utils.supplier.iterator.pnodesplaynode.BPIDPNodeSplayNodeSupplier;
import utils.supplier.iterator.pnodesplaynode.PNodeSplayNodeSupplier;

/**
 * Implements a sorted binary tree (extends class pnodePQ).
 * Implements the <code>enq</code> and the <code>deq</code> functions.
 */
public class BPpnodeSplayPQ extends pnodeSplayPQI<BasicPattern>
{
	public BPpnodeSplayPQ() {
		super();
	}
	
	public BPpnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order) {
		super(fldNo, fldType, order);
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
