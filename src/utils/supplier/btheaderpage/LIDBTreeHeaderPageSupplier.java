package utils.supplier.btheaderpage;

import btree.ConstructPageException;
import btree.label.LIDBTreeHeaderPage;
import btree.quadraple.QIDBTreeHeaderPage;
import diskmgr.Page;
import global.LID;
import global.PageId;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDBTreeHeaderPageSupplier implements BTreeHeaderPageSupplier<LID, Label> {
    @Override
    public LIDBTreeHeaderPage getBTreeHeaderPage(PageId pageId) throws ConstructPageException {
        return new LIDBTreeHeaderPage(pageId);
    }
    
    @Override
    public LIDBTreeHeaderPage getBTreeHeaderPage(Page page){
        return new LIDBTreeHeaderPage(page);
    }
    
    @Override
    public LIDBTreeHeaderPage getBTreeHeaderPage() throws ConstructPageException {
        return new LIDBTreeHeaderPage();
    }
    
    private LIDBTreeHeaderPageSupplier() {
    }
    
    private static LIDBTreeHeaderPageSupplier supplier;
    public static LIDBTreeHeaderPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTreeHeaderPageSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTreeHeaderPageSupplier();
                }
            }
        }
        return supplier;
    }
}
