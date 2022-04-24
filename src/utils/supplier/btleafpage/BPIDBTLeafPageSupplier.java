package utils.supplier.btleafpage;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.bp.BPIDBTLeafPage;
import btree.interfaces.BTLeafPageI;
import diskmgr.Page;
import global.BPID;
import global.PageId;

import java.io.IOException;

public class BPIDBTLeafPageSupplier implements BTLeafPageSupplier<BPID, BasicPattern>{
    @Override
    public BPIDBTLeafPage getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new BPIDBTLeafPage(page, keyType);
    }
    
    @Override
    public BTLeafPageI<BPID, BasicPattern> getBTLeafPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new BPIDBTLeafPage(pageno,keyType);
    }
    
    @Override
    public BPIDBTLeafPage getBTLeafPage(int keyType) throws IOException, ConstructPageException {
        return new BPIDBTLeafPage(keyType);
    }
    
    private BPIDBTLeafPageSupplier() {
    }
    
    private static BPIDBTLeafPageSupplier supplier;
    public static BPIDBTLeafPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTLeafPageSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTLeafPageSupplier();
                }
            }
        }
        return supplier;
    }
}
