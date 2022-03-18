package utils.supplier.btsortedpage;

import btree.ConstructPageException;
import btree.interfaces.BTSortedPageI;
import btree.label.LIDBTSortedPage;
import btree.quadraple.QIDBTSortedPage;
import diskmgr.Page;
import global.LID;
import global.PageId;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDBTSortedPageSupplier implements BTSortedPageSupplier<LID, Label> {
    @Override
    public LIDBTSortedPage getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException {
        return new LIDBTSortedPage(pageno,keyType);
    }
    
    @Override
    public BTSortedPageI<LID, Label> getBTSortedPage(Page page, int keyType) {
        return new LIDBTSortedPage(page, keyType);
    }
    
    private LIDBTSortedPageSupplier() {
    }
    
    
    private static LIDBTSortedPageSupplier supplier;
    public static LIDBTSortedPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTSortedPageSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTSortedPageSupplier();
                }
            }
        }
        return supplier;
    }
}
