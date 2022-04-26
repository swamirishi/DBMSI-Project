package utils.supplier.iterator.sort;

import global.AttrType;
import global.RID;
import global.TupleOrder;
import heap.Tuple;
import iterator.Sort;
import iterator.SortException;
import iterator.SpoofIbuf;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortI;
import iterator.interfaces.SpoofIbufI;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;

import java.io.IOException;

public class RIDSortSupplier implements SortSupplier<RID, Tuple> {
    
    
    private static RIDSortSupplier supplier;
    public static RIDSortSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDSortSupplier.class){
                if(supplier == null){
                    supplier = new RIDSortSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public SortI<RID, Tuple> getSort(AttrType[] in,
                                     short len_in,
                                     short[] str_sizes,
                                     IteratorI<Tuple> am,
                                     int sort_fld,
                                     TupleOrder sort_order,
                                     int sort_fld_len,
                                     int n_pages) throws IOException, SortException {
        return new Sort(in,len_in,str_sizes,am,sort_fld,sort_order,sort_fld_len,n_pages);
    }
}
