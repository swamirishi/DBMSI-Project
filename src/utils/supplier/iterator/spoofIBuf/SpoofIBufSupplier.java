package utils.supplier.iterator.spoofIBuf;

import global.ID;
import global.PageId;
import heap.Tuple;
import iterator.interfaces.SpoofIbufI;

public interface SpoofIBufSupplier<I extends ID, T extends Tuple> {
    public SpoofIbufI<I,T> getSpoofIBuf();
}
