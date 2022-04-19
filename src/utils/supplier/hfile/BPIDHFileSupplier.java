package utils.supplier.hfile;

import basicpatternheap.BasicPattern;
import basicpatternheap.BasicPatternHeapFile;
import global.BPID;
import global.LID;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.interfaces.HFile;
import labelheap.Label;
import labelheap.LabelHeapFile;

import java.io.IOException;

public class BPIDHFileSupplier implements HFileSupplier<BPID, BasicPattern> {
    @Override
    public HFile<BPID, BasicPattern> getHFile(String name) throws IOException, HFException, HFBufMgrException, HFDiskMgrException {
        return new BasicPatternHeapFile(name);
    }
    
    private BPIDHFileSupplier() {
    }
    
    private static BPIDHFileSupplier supplier;
    public static BPIDHFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDHFileSupplier.class){
                if(supplier == null){
                    supplier = new BPIDHFileSupplier();
                }
            }
        }
        return supplier;
    }
}
