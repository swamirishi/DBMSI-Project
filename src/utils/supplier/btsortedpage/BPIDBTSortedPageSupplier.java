package utils.supplier.btsortedpage;

import btree.BTSortedPage;
import btree.ConstructPageException;
import btree.interfaces.BTSortedPageI;
import btree.basicpattern.BPIDBTSortedPage;
import diskmgr.Page;
import global.PageId;
import global.BPID;
import global.RID;
import heap.Tuple;
import basicpatternheap.BasicPattern;

public class BPIDBTSortedPageSupplier implements BTSortedPageSupplier<BPID, BasicPattern> {
    @Override
    public BPIDBTSortedPage getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException {
        return new BPIDBTSortedPage(pageno,keyType);
    }
    
    @Override
    public BTSortedPageI<BPID, BasicPattern> getBTSortedPage(Page page, int keyType) {
        return new BPIDBTSortedPage(page,keyType);
    }
    
    private BPIDBTSortedPageSupplier() {
    }
    
    
    private static BPIDBTSortedPageSupplier supplier;
    public static BPIDBTSortedPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTSortedPageSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTSortedPageSupplier();
                }
            }
        }
        return supplier;
    }
}
