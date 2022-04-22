package utils.supplier.iterator.iobuf;

import global.RID;
import heap.Tuple;
import iterator.IoBuf;
import iterator.SpoofIbuf;
import iterator.interfaces.IoBufI;
import iterator.interfaces.SpoofIbufI;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;

public class RIDIoBufSupplier implements IoBufSupplier<RID, Tuple> {
    
    
    private static RIDIoBufSupplier supplier;
    public static RIDIoBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDIoBufSupplier.class){
                if(supplier == null){
                    supplier = new RIDIoBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public IoBufI<RID, Tuple> getIoBuf() {
        return new IoBuf();
    }
}
