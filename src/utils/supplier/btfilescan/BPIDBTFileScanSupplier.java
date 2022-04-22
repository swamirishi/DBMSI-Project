package utils.supplier.btfilescan;

import btree.basicpattern.BPIDBTFileScan;
import global.BPID;
import basicpatternheap.BasicPattern;

public class BPIDBTFileScanSupplier<K> implements BTFileScanSupplier<BPID, BasicPattern, K> {
    @Override
    public BPIDBTFileScan getBTFileScan() {
        return new BPIDBTFileScan();
    }

    public BPIDBTFileScanSupplier() {
    }
    
    private static BPIDBTFileScanSupplier supplier;
    public static BPIDBTFileScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTFileScanSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTFileScanSupplier();
                }
            }
        }
        return supplier;
    }
}
