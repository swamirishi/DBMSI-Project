package utils.supplier.btleafpage;

import btree.BTLeafPage;
import btree.ConstructPageException;
import btree.interfaces.BTLeafPageI;
import btree.quadraple.QIDBTLeafPage;
import diskmgr.Page;
import global.PageId;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class QIDBTLeafPageSupplier implements BTLeafPageSupplier<QID, Quadruple>{
    @Override
    public QIDBTLeafPage getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new QIDBTLeafPage(page,keyType);
    }
    
    @Override
    public BTLeafPageI<QID, Quadruple> getBTLeafPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new QIDBTLeafPage(pageno,keyType);
    }
    
    @Override
    public QIDBTLeafPage getBTLeafPage(int keyType) throws IOException, ConstructPageException {
        return new QIDBTLeafPage(keyType);
    }
    
    private QIDBTLeafPageSupplier() {
    }
    
    private static QIDBTLeafPageSupplier supplier;
    public static QIDBTLeafPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTLeafPageSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTLeafPageSupplier();
                }
            }
        }
        return supplier;
    }
}
