package utils.supplier.btsortedpage;

import btree.BTSortedPage;
import btree.ConstructPageException;
import btree.interfaces.BTSortedPageI;
import btree.quadraple.QIDBTSortedPage;
import diskmgr.Page;
import global.PageId;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDBTSortedPageSupplier implements BTSortedPageSupplier<QID, Quadruple> {
    @Override
    public QIDBTSortedPage getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException {
        return new QIDBTSortedPage(pageno,keyType);
    }
    
    @Override
    public BTSortedPageI<QID, Quadruple> getBTSortedPage(Page page, int keyType) {
        return new QIDBTSortedPage(page,keyType);
    }
    
    private QIDBTSortedPageSupplier() {
    }
    
    
    private static QIDBTSortedPageSupplier supplier;
    public static QIDBTSortedPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTSortedPageSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTSortedPageSupplier();
                }
            }
        }
        return supplier;
    }
}
