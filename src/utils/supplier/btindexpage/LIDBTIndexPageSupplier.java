package utils.supplier.btindexpage;

import btree.ConstructPageException;
import btree.label.LIDBTIndexPage;
import btree.quadraple.QIDBTIndexPage;
import diskmgr.Page;
import global.LID;
import global.PageId;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class LIDBTIndexPageSupplier implements BTIndexPageSupplier<LID, Label> {
    
    @Override
    public LIDBTIndexPage getBTIndexPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new LIDBTIndexPage(pageno,keyType);
    }
    
    @Override
    public LIDBTIndexPage getBTIndexPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new LIDBTIndexPage(page,keyType);
    }
    
    @Override
    public LIDBTIndexPage getBTIndexPage(int keyType) throws IOException, ConstructPageException {
        return new LIDBTIndexPage(keyType);
    }
    
    private LIDBTIndexPageSupplier() {
    }
    
    private static LIDBTIndexPageSupplier supplier;
    public static LIDBTIndexPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTIndexPageSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTIndexPageSupplier();
                }
            }
        }
        return supplier;
    }
}
