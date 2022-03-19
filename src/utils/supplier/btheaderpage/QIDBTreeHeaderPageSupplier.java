package utils.supplier.btheaderpage;

import btree.BTreeHeaderPage;
import btree.ConstructPageException;
import btree.quadraple.QIDBTreeHeaderPage;
import diskmgr.Page;
import global.PageId;
import global.QID;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDBTreeHeaderPageSupplier implements BTreeHeaderPageSupplier<QID, Quadruple> {
    @Override
    public QIDBTreeHeaderPage getBTreeHeaderPage(PageId pageId) throws ConstructPageException {
        return new QIDBTreeHeaderPage(pageId);
    }
    
    @Override
    public QIDBTreeHeaderPage getBTreeHeaderPage(Page page){
        return new QIDBTreeHeaderPage(page);
    }
    
    @Override
    public QIDBTreeHeaderPage getBTreeHeaderPage() throws ConstructPageException {
        return new QIDBTreeHeaderPage();
    }
    
    private QIDBTreeHeaderPageSupplier() {
    }
    
    private static QIDBTreeHeaderPageSupplier supplier;
    public static QIDBTreeHeaderPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTreeHeaderPageSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTreeHeaderPageSupplier();
                }
            }
        }
        return supplier;
    }
}
