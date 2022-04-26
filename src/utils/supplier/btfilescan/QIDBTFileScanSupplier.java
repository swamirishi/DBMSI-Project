package utils.supplier.btfilescan;

import btree.BTFileScan;
import btree.quadraple.QIDBTFileScan;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDBTFileScanSupplier<K> implements BTFileScanSupplier<QID, Quadruple,K> {
    @Override
    public QIDBTFileScan<K> getBTFileScan() {
        return new QIDBTFileScan<K>();
    }
    
    public QIDBTFileScanSupplier() {
    }
    
}
