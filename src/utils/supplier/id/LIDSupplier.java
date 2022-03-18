package utils.supplier.id;

import global.PageId;
import global.LID;

public class LIDSupplier implements IDSupplier<LID>{
    @Override
    public LID getID() {
        return new LID();
    }
    
    @Override
    public LID getID(PageId pageno, int slotno) {
        return new LID(pageno,slotno);
    }
    
    private LIDSupplier() {
    }
    
    private static LIDSupplier supplier;
    public static LIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDSupplier.class){
                if(supplier == null){
                    supplier = new LIDSupplier();
                }
            }
        }
        return supplier;
    }
}
