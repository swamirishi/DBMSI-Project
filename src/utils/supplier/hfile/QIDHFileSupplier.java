package utils.supplier.hfile;

import global.QID;
import global.RID;
import heap.*;
import heap.interfaces.HFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;

import java.io.IOException;

public class QIDHFileSupplier implements HFileSupplier<QID, Quadruple> {
    @Override
    public HFile<QID, Quadruple> getHFile(String name) throws IOException, HFException, HFBufMgrException, HFDiskMgrException {
        return new QuadrupleHeapFile(name);
    }
    
    private QIDHFileSupplier() {
    }
    
    private static QIDHFileSupplier supplier;
    public static QIDHFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDHFileSupplier.class){
                if(supplier == null){
                    supplier = new QIDHFileSupplier();
                }
            }
        }
        return supplier;
    }
}
