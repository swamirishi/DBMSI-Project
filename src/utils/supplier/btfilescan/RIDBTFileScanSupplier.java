package utils.supplier.btfilescan;

import btree.BTFileScan;
import btree.interfaces.BTFileScanI;
import global.RID;
import heap.Tuple;
import utils.supplier.btheaderpage.RIDBTreeHeaderPageSupplier;

public class RIDBTFileScanSupplier<K> implements BTFileScanSupplier<RID, Tuple,K> {
    @Override
    public BTFileScan getBTFileScan() {
        return new BTFileScan();
    }
    
    public RIDBTFileScanSupplier() {
    }
    
}
