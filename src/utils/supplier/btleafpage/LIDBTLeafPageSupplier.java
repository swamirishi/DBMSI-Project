package utils.supplier.btleafpage;

import btree.ConstructPageException;
import btree.interfaces.BTLeafPageI;
import btree.label.LIDBTLeafPage;
import btree.quadraple.QIDBTLeafPage;
import diskmgr.Page;
import global.LID;
import global.PageId;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class LIDBTLeafPageSupplier implements BTLeafPageSupplier<LID, Label>{
    @Override
    public LIDBTLeafPage getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new LIDBTLeafPage(page,keyType);
    }
    
    @Override
    public BTLeafPageI<LID, Label> getBTLeafPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new LIDBTLeafPage(pageno,keyType);
    }
    
    @Override
    public LIDBTLeafPage getBTLeafPage(int keyType) throws IOException, ConstructPageException {
        return new LIDBTLeafPage(keyType);
    }
    
    private LIDBTLeafPageSupplier() {
    }
    
    private static LIDBTLeafPageSupplier supplier;
    public static LIDBTLeafPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTLeafPageSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTLeafPageSupplier();
                }
            }
        }
        return supplier;
    }
}
