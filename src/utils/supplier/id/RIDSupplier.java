package utils.supplier.id;

import global.PageId;
import global.RID;

public class RIDSupplier implements IDSupplier<RID>{
    @Override
    public RID getID() {
        return new RID();
    }
    
    @Override
    public RID getID(PageId pageno, int slotno) {
        return new RID(pageno,slotno);
    }
    
    private RIDSupplier() {
    }
    
    private static RIDSupplier supplier;
    public static RIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDSupplier.class){
                if(supplier == null){
                    supplier = new RIDSupplier();
                }
            }
        }
        return supplier;
    }
}
