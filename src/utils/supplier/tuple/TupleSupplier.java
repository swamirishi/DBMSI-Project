package utils.supplier.tuple;

import heap.Tuple;

public interface TupleSupplier<T extends Tuple> {
    public T getTuple();
}
