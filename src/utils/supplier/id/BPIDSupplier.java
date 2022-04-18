package utils.supplier.id;

import global.BPID;
import global.PageId;

public class BPIDSupplier implements IDSupplier<BPID>{
    @Override
    public BPID getID() {
        return new BPID();
    }

    @Override
    public BPID getID(PageId pageno, int slotno) {
        return new BPID(pageno,slotno);
    }

    private BPIDSupplier() {
    }

    private static BPIDSupplier supplier;
    public static BPIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDSupplier.class){
                if(supplier == null){
                    supplier = new BPIDSupplier();
                }
            }
        }
        return supplier;
    }
}
