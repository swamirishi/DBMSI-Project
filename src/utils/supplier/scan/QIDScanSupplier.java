package utils.supplier.scan;

import global.LID;
import global.QID;
import global.RID;
import heap.InvalidTupleSizeException;
import heap.Scan;
import heap.Tuple;
import heap.interfaces.HFile;
import quadrupleheap.Quadruple;
import quadrupleheap.TScan;

import java.io.IOException;

public class QIDScanSupplier implements ScanSupplier<QID, Quadruple> {
    @Override
    public TScan getScan(HFile<QID,Quadruple> hFile) throws InvalidTupleSizeException, IOException {
        return new TScan(hFile);
    }
    
    private QIDScanSupplier() {
    }
    
    private static QIDScanSupplier supplier;
    public static QIDScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDScanSupplier.class){
                if(supplier == null){
                    supplier = new QIDScanSupplier();
                }
            }
        }
        return supplier;
    }
}
