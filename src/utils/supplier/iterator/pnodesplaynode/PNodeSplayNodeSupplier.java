package utils.supplier.iterator.pnodesplaynode;

import heap.Tuple;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;

public interface PNodeSplayNodeSupplier<T extends Tuple> {
    public pnodeSplayNodeI<T> getPNodeSplayNode(pnodeI<T> h);
}
