package utils.supplier.btindexpage;

import btree.BTIndexPage;
import btree.ConstructPageException;
import btree.basicpattern.BPIDBTIndexPage;
import diskmgr.Page;
import global.PageId;
import global.BPID;
import heap.Tuple;
import bsicpatternheap.BasicPattern;

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
