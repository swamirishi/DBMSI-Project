package utils.supplier.btfilescan;

import btree.interfaces.BTFileScanI;
import global.ID;
import heap.Tuple;

public interface BTFileScanSupplier<I extends ID, T extends Tuple> {
    public BTFileScanI<I,T> getBTFileScan();
}
