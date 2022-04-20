package utils.supplier.btfile;

import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import btree.basicpattern.BPIDBTreeFile;
import global.BPID;
import basicpatternheap.BasicPattern;

public class BPIDBTreeFileSupplier implements BTreeFileSupplier<BPID, BasicPattern> {
    @Override
    public BTreeFileI<BPID, BasicPattern> getBTreeFile(String name) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new BPIDBTreeFile(name);
    }

    private BPIDBTreeFileSupplier() {
    }
    
    private static BPIDBTreeFileSupplier supplier;
    public static BPIDBTreeFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDBTreeFileSupplier.class){
                if(supplier == null){
                    supplier = new BPIDBTreeFileSupplier();
                }
            }
        }
        return supplier;
    }
}
