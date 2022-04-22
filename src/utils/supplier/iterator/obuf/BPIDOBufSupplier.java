package utils.supplier.iterator.obuf;

import basicpatternheap.BasicPattern;
import global.BPID;
import iterator.bp.BPOBuf;
import iterator.interfaces.OBufI;

public class BPIDOBufSupplier implements OBufSupplier<BPID, BasicPattern> {
    
    
    private BPIDOBufSupplier() {
    }
    
    private static BPIDOBufSupplier supplier;
    public static BPIDOBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDOBufSupplier.class){
                if(supplier == null){
                    supplier = new BPIDOBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public OBufI<BPID, BasicPattern> getOBuf() {
        return new BPOBuf();
    }
}
