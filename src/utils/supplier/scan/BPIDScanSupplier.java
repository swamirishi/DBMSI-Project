package utils.supplier.scan;

import basicpatternheap.BasicPattern;
import global.BPID;
import heap.InvalidTupleSizeException;
import heap.interfaces.HFile;
import basicpatternheap.TScan;

import java.io.IOException;

public class BPIDScanSupplier implements ScanSupplier<BPID, BasicPattern> {
    @Override
    public TScan getScan(HFile<BPID,BasicPattern> hFile) throws InvalidTupleSizeException, IOException {
        return new TScan(hFile);
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
