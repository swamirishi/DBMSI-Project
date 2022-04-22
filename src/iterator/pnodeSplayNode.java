package iterator;

import heap.Tuple;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;

/**
 * An element in the binary tree.
 * including pointers to the children, the parent in addition to the item.
 */
public class pnodeSplayNode extends pnodeSplayNodeI<Tuple> {
    public static pnodeSplayNodeI<Tuple> dummy = new pnodeSplayNode(null);
    
    public pnodeSplayNode(pnodeI<Tuple> h) {
        super(h);
    }
    
    public pnodeSplayNode(pnodeI<Tuple> h, pnodeSplayNodeI<Tuple> l, pnodeSplayNodeI<Tuple> r) {
        super(h, l, r);
    }
}

