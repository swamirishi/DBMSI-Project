package utils.supplier.leafdata;

import btree.label.LIDLeafData;
import btree.quadraple.QIDLeafData;
import global.LID;
import global.QID;

public class LIDLeafDataSupplier implements LeafDataSupplier<LID>{
    
    @Override
    public LIDLeafData getLeafData(LID id) {
        return new LIDLeafData(id);
    }
    
    private LIDLeafDataSupplier() {
    }
    
    private static LIDLeafDataSupplier supplier;
    public static LIDLeafDataSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDLeafDataSupplier.class){
                if(supplier == null){
                    supplier = new LIDLeafDataSupplier();
                }
            }
        }
        return supplier;
    }
}
