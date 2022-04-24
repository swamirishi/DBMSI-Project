package utils.supplier.btheaderpage;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.bp.BPIDBTreeHeaderPage;
import btree.label.LIDBTreeHeaderPage;
import diskmgr.Page;
import global.BPID;
import global.LID;
import global.PageId;
import labelheap.Label;

public class BPIDBTreeHeaderPageSupplier implements BTreeHeaderPageSupplier<BPID, BasicPattern> {
    @Override
    public BPIDBTreeHeaderPage getBTreeHeaderPage(PageId pageId) throws ConstructPageException {
        return new BPIDBTreeHeaderPage(pageId);
    }
    
    @Override
    public BPIDBTreeHeaderPage getBTreeHeaderPage(Page page){
        return new BPIDBTreeHeaderPage(page);
    }
    
    @Override
    public BPIDBTreeHeaderPage getBTreeHeaderPage() throws ConstructPageException {
        return new BPIDBTreeHeaderPage();
    }
    
    private BPIDBTreeHeaderPageSupplier() {
    }
    
    private static BPIDBTreeHeaderPageSupplier supplier;
    public static BPIDBTreeHeaderPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTreeHeaderPageSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTreeHeaderPageSupplier();
                }
            }
        }
        return supplier;
    }
}
