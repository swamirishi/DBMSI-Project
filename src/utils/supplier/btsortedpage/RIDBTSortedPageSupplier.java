package utils.supplier.btsortedpage;

import btree.BTSortedPage;
import btree.ConstructPageException;
import btree.interfaces.BTSortedPageI;
import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.Tuple;

public class RIDBTSortedPageSupplier implements BTSortedPageSupplier<RID, Tuple> {
    @Override
    public BTSortedPage getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException {
        return new BTSortedPage(pageno,keyType);
    }
    
    @Override
    public BTSortedPageI<RID, Tuple> getBTSortedPage(Page page, int keyType) {
        return new BTSortedPage(page,keyType);
    }
    
    private RIDBTSortedPageSupplier() {
    }
    
    
    private static RIDBTSortedPageSupplier supplier;
    public static RIDBTSortedPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTSortedPageSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTSortedPageSupplier();
                }
            }
        }
        return supplier;
    }
}
