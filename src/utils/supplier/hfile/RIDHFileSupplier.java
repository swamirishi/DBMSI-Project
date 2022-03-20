package utils.supplier.hfile;

import global.RID;
import heap.*;
import heap.interfaces.HFile;
import utils.supplier.hfilepage.RIDHFilePageSupplier;

import java.io.IOException;

public class RIDHFileSupplier implements HFileSupplier<RID, Tuple> {
    @Override
    public HFile<RID, Tuple> getHFile(String name) throws IOException, HFException, HFBufMgrException, HFDiskMgrException {
        return new Heapfile(name);
    }
    
    private RIDHFileSupplier() {
    }
    
    private static RIDHFileSupplier supplier;
    public static RIDHFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDHFileSupplier.class){
                if(supplier == null){
                    supplier = new RIDHFileSupplier();
                }
            }
        }
        return supplier;
    }
}
