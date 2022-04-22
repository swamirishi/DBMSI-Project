package iterator.bp;

import basicpatternheap.BasicPattern;
import heap.Tuple;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;

/**
 * An element in the binary tree.
 * including pointers to the children, the parent in addition to the item.
 */
public class BPpnodeSplayNode extends pnodeSplayNodeI<BasicPattern> {
    public static pnodeSplayNodeI<BasicPattern> dummy = new BPpnodeSplayNode(null);
    
    public BPpnodeSplayNode(pnodeI<BasicPattern> h) {
        super(h);
    }
    
    public BPpnodeSplayNode(pnodeI<BasicPattern> h, pnodeSplayNodeI<BasicPattern> l, pnodeSplayNodeI<BasicPattern> r) {
        super(h, l, r);
    }
}

