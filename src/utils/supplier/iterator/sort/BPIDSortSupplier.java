package utils.supplier.iterator.sort;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.BPID;
import global.TupleOrder;
import iterator.SortException;
import iterator.bp.BPSort;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortI;

import java.io.IOException;

public class BPIDSortSupplier implements SortSupplier<BPID, BasicPattern> {
    
    
    private static BPIDSortSupplier supplier;
    public static BPIDSortSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDSortSupplier.class){
                if(supplier == null){
                    supplier = new BPIDSortSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public SortI<BPID, BasicPattern> getSort(AttrType[] in,
                                      short len_in,
                                      short[] str_sizes,
                                      IteratorI<BasicPattern> am,
                                      int sort_fld,
                                      TupleOrder sort_order,
                                      int sort_fld_len,
                                      int n_pages) throws IOException, SortException {
        return this.getSort(in, len_in, str_sizes, am, sort_fld, sort_order, sort_fld_len, n_pages,true);
    }
    
    public SortI<BPID, BasicPattern> getSort(AttrType[] in,
                                             short len_in,
                                             short[] str_sizes,
                                             IteratorI<BasicPattern> am,
                                             int sort_fld,
                                             TupleOrder sort_order,
                                             int sort_fld_len,
                                             int n_pages,
                                             boolean referenceBased) throws IOException, SortException {
        return new BPSort(in, len_in, str_sizes, am, sort_fld, sort_order, sort_fld_len, n_pages) {
            @Override
            public boolean isReferenceBased() {
                return referenceBased;
            }
        };
    }
}
