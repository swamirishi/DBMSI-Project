package utils.supplier.iterator.iobuf;

import global.ID;
import heap.Tuple;
import iterator.interfaces.IoBufI;
import iterator.interfaces.SpoofIbufI;

public interface IoBufSupplier<I extends ID, T extends Tuple> {
    public IoBufI<I,T> getIoBuf();
}
