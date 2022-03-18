package utils.supplier.btfilescan;

import btree.label.LIDBTFileScan;
import btree.quadraple.QIDBTFileScan;
import global.LID;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDBTFileScanSupplier implements BTFileScanSupplier<LID, Label> {
    @Override
    public LIDBTFileScan getBTFileScan() {
        return new LIDBTFileScan();
    }
    
    private LIDBTFileScanSupplier() {
    }
    
    private static LIDBTFileScanSupplier supplier;
    public static LIDBTFileScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTFileScanSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTFileScanSupplier();
                }
            }
        }
        return supplier;
    }
}
