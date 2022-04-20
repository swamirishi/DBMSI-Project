package utils.supplier.btleafpage;

import btree.BTLeafPage;
import btree.ConstructPageException;
import btree.interfaces.BTLeafPageI;
import btree.basicpattern.BPIDBTLeafPage;
import diskmgr.Page;
import global.PageId;
import global.BPID;
import global.RID;
import heap.Tuple;
import basicpatternheap.BasicPattern;

import java.io.IOException;

public class BPIDBTLeafPageSupplier implements BTLeafPageSupplier<BPID, BasicPattern>{
    @Override
    public BPIDBTLeafPage getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new BPIDBTLeafPage(page,keyType);
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
