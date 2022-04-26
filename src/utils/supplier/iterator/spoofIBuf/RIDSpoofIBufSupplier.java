package utils.supplier.iterator.spoofIBuf;

import global.RID;
import heap.Tuple;
import iterator.SpoofIbuf;
import iterator.interfaces.SpoofIbufI;

public class RIDSpoofIBufSupplier implements SpoofIBufSupplier<RID, Tuple> {
    
    
    private static RIDSpoofIBufSupplier supplier;
    public static RIDSpoofIBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDSpoofIBufSupplier.class){
                if(supplier == null){
                    supplier = new RIDSpoofIBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public SpoofIbufI<RID, Tuple> getSpoofIBuf() {
        return new SpoofIbuf();
    }
}
