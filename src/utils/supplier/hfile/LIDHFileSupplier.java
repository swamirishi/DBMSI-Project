package utils.supplier.hfile;

import global.LID;
import global.QID;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.interfaces.HFile;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;

import java.io.IOException;

public class LIDHFileSupplier implements HFileSupplier<LID, Label> {
    @Override
    public HFile<LID, Label> getHFile(String name) throws IOException, HFException, HFBufMgrException, HFDiskMgrException {
        return new LabelHeapFile(name);
    }
    
    private LIDHFileSupplier() {
    }
    
    private static LIDHFileSupplier supplier;
    public static LIDHFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDHFileSupplier.class){
                if(supplier == null){
                    supplier = new LIDHFileSupplier();
                }
            }
        }
        return supplier;
    }
}
