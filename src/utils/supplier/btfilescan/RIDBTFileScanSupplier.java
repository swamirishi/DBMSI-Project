package utils.supplier.btfilescan;

import btree.BTFileScan;
import btree.interfaces.BTFileScanI;
import global.RID;
import heap.Tuple;
import utils.supplier.btheaderpage.RIDBTreeHeaderPageSupplier;

public class RIDBTFileScanSupplier implements BTFileScanSupplier<RID, Tuple> {
    @Override
    public BTFileScan getBTFileScan() {
        return new BTFileScan();
    }
    
    private RIDBTFileScanSupplier() {
    }
    
    private static RIDBTFileScanSupplier supplier;
    public static RIDBTFileScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTFileScanSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTFileScanSupplier();
                }
            }
        }
        return supplier;
    }
}
