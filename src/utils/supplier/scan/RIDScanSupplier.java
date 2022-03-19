package utils.supplier.scan;

import global.ID;
import global.RID;
import heap.InvalidTupleSizeException;
import heap.Scan;
import heap.Tuple;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import utils.supplier.hfilepage.RIDHFilePageSupplier;

import java.io.IOException;

public class RIDScanSupplier implements ScanSupplier<RID, Tuple> {
    @Override
    public Scan getScan(HFile<RID,Tuple> hFile) throws InvalidTupleSizeException, IOException {
        return new Scan(hFile);
    }
    
    private RIDScanSupplier() {
    }
    
    private static RIDScanSupplier supplier;
    public static RIDScanSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDScanSupplier.class){
                if(supplier == null){
                    supplier = new RIDScanSupplier();
                }
            }
        }
        return supplier;
    }
}
