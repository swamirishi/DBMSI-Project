package utils.supplier.iterator.obuf;

import global.ID;
import heap.Tuple;
import iterator.interfaces.OBufI;

public interface OBufSupplier<I extends ID,T extends Tuple> {
    public OBufI<I,T> getOBuf();
}
