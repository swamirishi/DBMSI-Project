package utils.supplier.leafdata;

import btree.basicpattern.BPIDLeafData;
import global.BPID;

public class BPIDLeafDataSupplier implements LeafDataSupplier<BPID>{

    @Override
    public BPIDLeafData getLeafData(BPID id) {
        return new BPIDLeafData(id);
    }

    private BPIDLeafDataSupplier() {
    }
    
    private static BPIDLeafDataSupplier supplier;
    public static BPIDLeafDataSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDLeafDataSupplier.class){
                if(supplier == null){
                    supplier = new BPIDLeafDataSupplier();
                }
            }
        }
        return supplier;
    }
}
