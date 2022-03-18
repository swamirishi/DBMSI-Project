package utils.supplier.id;

import global.PID;
import global.PageId;

public class PIDSupplier implements IDSupplier<PID>{
    @Override
    public PID getID() {
        return new PID();
    }
    
    @Override
    public PID getID(PageId pageno, int slotno) {
        return new PID(pageno,slotno);
    }
    
    private PIDSupplier() {
    }
    
    private static PIDSupplier supplier;
    public static PIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (PIDSupplier.class){
                if(supplier == null){
                    supplier = new PIDSupplier();
                }
            }
        }
        return supplier;
    }
}
