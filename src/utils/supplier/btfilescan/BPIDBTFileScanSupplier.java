package utils.supplier.btfilescan;

import basicpatternheap.BasicPattern;
import btree.bp.BPIDBTFileScan;
import global.BPID;

public class BPIDBTFileScanSupplier<K> implements BTFileScanSupplier<BPID, BasicPattern,K> {
    @Override
    public BPIDBTFileScan<K> getBTFileScan() {
        return new BPIDBTFileScan<K>();
    }
    
    public BPIDBTFileScanSupplier() {
    }
    
    
}
