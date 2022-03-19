package utils.supplier.hfilepage;

import global.LID;
import global.RID;
import heap.HFPage;
import heap.Tuple;
import heap.interfaces.HFilePage;
import labelheap.LHFPage;
import labelheap.Label;

public class LIDHFilePageSupplier implements HFilePageSupplier<LID, Label> {
    @Override
    public LHFPage getHFilePage() {
        return new LHFPage();
    }
    
    @Override
    public LHFPage getHFilePage(HFilePage<LID, Label> heapFilePage) {
        return new LHFPage(heapFilePage);
    }
    
    private LIDHFilePageSupplier() {
    }
    
    private static LIDHFilePageSupplier supplier;
    public static LIDHFilePageSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDHFilePageSupplier.class){
                if(supplier == null){
                    supplier = new LIDHFilePageSupplier();
                }
            }
        }
        return supplier;
    }
}
