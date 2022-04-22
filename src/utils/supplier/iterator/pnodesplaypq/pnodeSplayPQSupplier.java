package utils.supplier.iterator.pnodesplaypq;

import global.AttrType;
import global.TupleOrder;
import heap.Tuple;
import iterator.interfaces.pnodeSplayPQI;
import iterator.pnodeSplayPQ;

public interface pnodeSplayPQSupplier<T extends Tuple> {
    pnodeSplayPQI<T> getPnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order);
    pnodeSplayPQI<T> getPnodeSplayPQ();
}
