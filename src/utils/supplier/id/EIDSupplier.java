package utils.supplier.id;

import global.EID;
import global.PageId;
import utils.supplier.btsortedpage.RIDBTSortedPageSupplier;

public class EIDSupplier implements IDSupplier<EID>{
    @Override
    public EID getID() {
        return new EID();
    }
    
    @Override
    public EID getID(PageId pageno, int slotno) {
        return new EID(pageno,slotno);
    }
    
    private EIDSupplier() {
    }
    
    private static EIDSupplier supplier;
    public static EIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (EIDSupplier.class){
                if(supplier == null){
                    supplier = new EIDSupplier();
                }
            }
        }
        return supplier;
    }
    
}
