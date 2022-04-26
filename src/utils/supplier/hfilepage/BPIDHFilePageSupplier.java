package utils.supplier.hfilepage;

import basicpatternheap.BasicPattern;
import basicpatternheap.THFPage;
import global.BPID;
import heap.interfaces.HFilePage;

public class BPIDHFilePageSupplier implements HFilePageSupplier<BPID, BasicPattern> {
    @Override
    public THFPage getHFilePage() {
        return new THFPage();
    }

    @Override
    public THFPage getHFilePage(HFilePage<BPID, BasicPattern> heapFilePage) {
        return new THFPage(heapFilePage);
    }

    private BPIDHFilePageSupplier() {
    }

    private static BPIDHFilePageSupplier supplier;
    public static BPIDHFilePageSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDHFilePageSupplier.class){
                if(supplier == null){
                    supplier = new BPIDHFilePageSupplier();
                }
            }
        }
        return supplier;
    }
}
