package utils.supplier.scan;

import global.ID;
import global.RID;
import heap.InvalidTupleSizeException;
import heap.Tuple;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;

import java.io.IOException;

public interface ScanSupplier<I extends ID,T extends Tuple> {
    public ScanI<I,T> getScan(HFile<I,T> hFile) throws InvalidTupleSizeException, IOException;
}
