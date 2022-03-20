package utils.supplier.btfilescan;

import btree.label.LIDBTFileScan;
import btree.quadraple.QIDBTFileScan;
import global.LID;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDBTFileScanSupplier<K> implements BTFileScanSupplier<LID, Label,K> {
    @Override
    public LIDBTFileScan<K> getBTFileScan() {
        return new LIDBTFileScan<K>();
    }
    
    public LIDBTFileScanSupplier() {
    }
    
    
}
