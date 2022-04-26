package utils.supplier.scan;

import basicpatternheap.BPScan;
import basicpatternheap.BasicPattern;
import global.BPID;
import heap.InvalidTupleSizeException;
import heap.interfaces.HFile;

import java.io.IOException;

public class BPIDScanSupplier implements ScanSupplier<BPID, BasicPattern> {
    @Override
    public BPScan getScan(HFile<BPID,BasicPattern> hFile) throws InvalidTupleSizeException, IOException {
        return new BPScan(hFile);
    }

    private BPIDScanSupplier() {
    }

    private static BPIDScanSupplier supplier;
    public static BPIDScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDScanSupplier.class){
                if(supplier == null){
                    supplier = new BPIDScanSupplier();
                }
            }
        }
        return supplier;
    }
}
