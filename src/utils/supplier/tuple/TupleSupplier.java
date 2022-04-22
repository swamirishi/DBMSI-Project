package utils.supplier.tuple;

import heap.InvalidTupleSizeException;
import heap.Tuple;

public interface TupleSupplier<T extends Tuple> {
    public T getTuple();
    public T getTuple(byte [] atuple, int offset, int length);
    public T getTuple(int size);
    public T getTuple(T tuple);
}
