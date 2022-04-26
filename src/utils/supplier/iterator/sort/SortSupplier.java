package utils.supplier.iterator.sort;

import global.AttrType;
import global.ID;
import global.TupleOrder;
import heap.Tuple;
import iterator.SortException;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortI;

import java.io.IOException;

public interface SortSupplier<I extends ID,T extends Tuple> {
    public SortI<I,T> getSort(AttrType[] in,
                              short len_in,
                              short[] str_sizes,
                              IteratorI<T> am,
                              int sort_fld,
                              TupleOrder sort_order,
                              int sort_fld_len,
                              int n_pages) throws IOException, SortException;
}
