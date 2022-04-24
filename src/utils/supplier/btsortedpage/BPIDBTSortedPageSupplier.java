package utils.supplier.btsortedpage;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.bp.BPIDBTSortedPage;
import btree.interfaces.BTSortedPageI;
import btree.label.LIDBTSortedPage;
import diskmgr.Page;
import global.BPID;
import global.LID;
import global.PageId;
import labelheap.Label;

public class BPIDBTSortedPageSupplier implements BTSortedPageSupplier<BPID, BasicPattern> {
    @Override
    public BPIDBTSortedPage getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException {
        return new BPIDBTSortedPage(pageno,keyType);
    }
    
    @Override
    public BTSortedPageI<BPID, BasicPattern> getBTSortedPage(Page page, int keyType) {
        return new BPIDBTSortedPage(page, keyType);
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
