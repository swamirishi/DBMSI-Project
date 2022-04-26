package utils.supplier.iterator.obuf;

import global.RID;
import heap.Tuple;
import iterator.OBuf;
import iterator.interfaces.OBufI;

public class RIDOBufSupplier implements OBufSupplier<RID,Tuple> {
    
    
    private RIDOBufSupplier() {
    }
    
    private static RIDOBufSupplier supplier;
    public static RIDOBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDOBufSupplier.class){
                if(supplier == null){
                    supplier = new RIDOBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public OBufI<RID, Tuple> getOBuf() {
        return new OBuf();
    }
}
