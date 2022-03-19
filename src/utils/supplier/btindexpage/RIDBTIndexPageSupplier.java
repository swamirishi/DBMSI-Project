package utils.supplier.btindexpage;

import btree.BTIndexPage;
import btree.ConstructPageException;
import btree.interfaces.BTIndexPageI;
import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.Tuple;

import java.io.IOException;

public class RIDBTIndexPageSupplier implements BTIndexPageSupplier<RID, Tuple> {
    
    @Override
    public BTIndexPage getBTIndexPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new BTIndexPage(pageno,keyType);
    }
    
    @Override
    public BTIndexPage getBTIndexPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new BTIndexPage(page,keyType);
    }
    
    @Override
    public BTIndexPage getBTIndexPage(int keyType) throws IOException, ConstructPageException {
        return new BTIndexPage(keyType);
    }
    
    private RIDBTIndexPageSupplier() {
    }
    
    private static RIDBTIndexPageSupplier supplier;
    public static RIDBTIndexPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTIndexPageSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTIndexPageSupplier();
                }
            }
        }
        return supplier;
    }
}
