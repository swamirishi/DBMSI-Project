package utils.supplier.btleafpage;

import btree.BTLeafPage;
import btree.ConstructPageException;
import btree.interfaces.BTLeafPageI;
import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.Tuple;

import java.io.IOException;

public class RIDBTLeafPageSupplier implements BTLeafPageSupplier<RID, Tuple>{
    @Override
    public BTLeafPage getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new BTLeafPage(page,keyType);
    }
    
    @Override
    public BTLeafPageI<RID, Tuple> getBTLeafPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new BTLeafPage(pageno,keyType);
    }
    
    @Override
    public BTLeafPage getBTLeafPage(int keyType) throws IOException, ConstructPageException {
        return new BTLeafPage(keyType);
    }
    
    private RIDBTLeafPageSupplier() {
    }
    
    private static RIDBTLeafPageSupplier supplier;
    public static RIDBTLeafPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTLeafPageSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTLeafPageSupplier();
                }
            }
        }
        return supplier;
    }
}
