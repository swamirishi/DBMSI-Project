package utils.supplier.leafdata;

import btree.LeafData;
import btree.interfaces.ILeafData;
import global.RID;
import utils.supplier.id.RIDSupplier;

public class RIDLeafDataSupplier implements LeafDataSupplier<RID>{
    
    @Override
    public LeafData getLeafData(RID id) {
        return new LeafData(id);
    }
    
    private RIDLeafDataSupplier() {
    }
    
    private static RIDLeafDataSupplier supplier;
    public static RIDLeafDataSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDLeafDataSupplier.class){
                if(supplier == null){
                    supplier = new RIDLeafDataSupplier();
                }
            }
        }
        return supplier;
    }
}
