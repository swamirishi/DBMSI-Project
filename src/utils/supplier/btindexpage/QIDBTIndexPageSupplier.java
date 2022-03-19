package utils.supplier.btindexpage;

import btree.BTIndexPage;
import btree.ConstructPageException;
import btree.quadraple.QIDBTIndexPage;
import diskmgr.Page;
import global.PageId;
import global.QID;
import heap.Tuple;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class QIDBTIndexPageSupplier implements BTIndexPageSupplier<QID, Quadruple> {
    
    @Override
    public QIDBTIndexPage getBTIndexPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new QIDBTIndexPage(pageno,keyType);
    }
    
    @Override
    public QIDBTIndexPage getBTIndexPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new QIDBTIndexPage(page,keyType);
    }
    
    @Override
    public QIDBTIndexPage getBTIndexPage(int keyType) throws IOException, ConstructPageException {
        return new QIDBTIndexPage(keyType);
    }
    
    private QIDBTIndexPageSupplier() {
    }
    
    private static QIDBTIndexPageSupplier supplier;
    public static QIDBTIndexPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTIndexPageSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTIndexPageSupplier();
                }
            }
        }
        return supplier;
    }
}
