package utils.supplier.scan;

import global.LID;
import heap.InvalidTupleSizeException;
import heap.Scan;
import heap.Tuple;
import heap.interfaces.HFile;
import labelheap.LScan;
import labelheap.Label;

import java.io.IOException;

public class LIDScanSupplier implements ScanSupplier<LID, Label> {
    @Override
    public LScan getScan(HFile<LID,Label> hFile) throws InvalidTupleSizeException, IOException {
        return new LScan(hFile);
    }
    
    private LIDScanSupplier() {
    }
    
    private static LIDScanSupplier supplier;
    public static LIDScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDScanSupplier.class){
                if(supplier == null){
                    supplier = new LIDScanSupplier();
                }
            }
        }
        return supplier;
    }
}
