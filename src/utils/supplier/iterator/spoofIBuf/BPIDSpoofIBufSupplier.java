package utils.supplier.iterator.spoofIBuf;

import basicpatternheap.BasicPattern;
import global.BPID;
import iterator.bp.BPSpoofIbuf;
import iterator.interfaces.SpoofIbufI;

public class BPIDSpoofIBufSupplier implements SpoofIBufSupplier<BPID, BasicPattern> {
    
    
    private static BPIDSpoofIBufSupplier supplier;
    public static BPIDSpoofIBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDSpoofIBufSupplier.class){
                if(supplier == null){
                    supplier = new BPIDSpoofIBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public SpoofIbufI<BPID, BasicPattern> getSpoofIBuf() {
        return new BPSpoofIbuf();
    }
}
