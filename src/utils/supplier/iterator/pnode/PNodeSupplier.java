package utils.supplier.iterator.pnode;

import heap.Tuple;
import iterator.interfaces.pnodeI;

public interface PNodeSupplier<T extends Tuple> {
    public pnodeI<T> getPNode();
}
