package utils.supplier.btindexpage;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.bp.BPIDBTIndexPage;
import diskmgr.Page;
import global.BPID;
import global.BPID;
import global.PageId;
import labelheap.Label;

import java.io.IOException;

public class BPIDBTIndexPageSupplier implements BTIndexPageSupplier<BPID, BasicPattern> {
    
    @Override
    public BPIDBTIndexPage getBTIndexPage(PageId pageno, int keyType) throws IOException, ConstructPageException {
        return new BPIDBTIndexPage(pageno,keyType);
    }
    
    @Override
    public BPIDBTIndexPage getBTIndexPage(Page page, int keyType) throws IOException, ConstructPageException {
        return new BPIDBTIndexPage(page,keyType);
    }
    
    @Override
    public BPIDBTIndexPage getBTIndexPage(int keyType) throws IOException, ConstructPageException {
        return new BPIDBTIndexPage(keyType);
    }
    
    private BPIDBTIndexPageSupplier() {
    }
    
    private static BPIDBTIndexPageSupplier supplier;
    public static BPIDBTIndexPageSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTIndexPageSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTIndexPageSupplier();
                }
            }
        }
        return supplier;
    }
}
