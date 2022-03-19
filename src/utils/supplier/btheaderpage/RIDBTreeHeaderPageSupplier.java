package utils.supplier.btheaderpage;

import btree.BTreeHeaderPage;
import btree.ConstructPageException;
import btree.interfaces.BTreeHeaderPageI;
import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.Tuple;
import utils.supplier.btindexpage.RIDBTIndexPageSupplier;

public class RIDBTreeHeaderPageSupplier implements BTreeHeaderPageSupplier<RID, Tuple> {
    @Override
    public BTreeHeaderPage getBTreeHeaderPage(PageId pageId) throws ConstructPageException {
        return new BTreeHeaderPage(pageId);
    }
    
    @Override
    public BTreeHeaderPage getBTreeHeaderPage(Page page){
        return new BTreeHeaderPage(page);
    }
    
    @Override
    public BTreeHeaderPage getBTreeHeaderPage() throws ConstructPageException {
        return new BTreeHeaderPage();
    }
    
    private RIDBTreeHeaderPageSupplier() {
    }
    
    private static RIDBTreeHeaderPageSupplier supplier;
    public static RIDBTreeHeaderPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTreeHeaderPageSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTreeHeaderPageSupplier();
                }
            }
        }
        return supplier;
    }
}
