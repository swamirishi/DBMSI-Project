package utils.supplier.hfilepage;

import global.RID;
import heap.HFPage;
import heap.Tuple;
import heap.interfaces.HFilePage;
import utils.supplier.btsortedpage.RIDBTSortedPageSupplier;

public class RIDHFilePageSupplier implements HFilePageSupplier<RID, Tuple> {
    @Override
    public HFPage getHFilePage() {
        return new HFPage();
    }
    
    @Override
    public HFPage getHFilePage(HFilePage<RID, Tuple> heapFilePage) {
        return new HFPage(heapFilePage);
    }
    
    private RIDHFilePageSupplier() {
    }
    
    private static RIDHFilePageSupplier supplier;
    public static RIDHFilePageSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDHFilePageSupplier.class){
                if(supplier == null){
                    supplier = new RIDHFilePageSupplier();
                }
            }
        }
        return supplier;
    }
}
