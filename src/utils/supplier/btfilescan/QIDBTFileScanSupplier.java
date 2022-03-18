package utils.supplier.btfilescan;

import btree.BTFileScan;
import btree.quadraple.QIDBTFileScan;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDBTFileScanSupplier implements BTFileScanSupplier<QID, Quadruple> {
    @Override
    public QIDBTFileScan getBTFileScan() {
        return new QIDBTFileScan();
    }
    
    private QIDBTFileScanSupplier() {
    }
    
    private static QIDBTFileScanSupplier supplier;
    public static QIDBTFileScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTFileScanSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTFileScanSupplier();
                }
            }
        }
        return supplier;
    }
}
