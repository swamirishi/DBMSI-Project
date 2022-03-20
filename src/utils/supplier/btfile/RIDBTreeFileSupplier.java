package utils.supplier.btfile;

import btree.BTreeFile;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import global.RID;
import heap.*;
import heap.interfaces.HFile;
import utils.supplier.hfile.HFileSupplier;

import java.io.IOException;

public class RIDBTreeFileSupplier implements BTreeFileSupplier<RID, Tuple> {
    @Override
    public BTreeFileI<RID, Tuple> getBTreeFile(String name) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new BTreeFile(name);
    }
    
    private RIDBTreeFileSupplier() {
    }
    
    private static RIDBTreeFileSupplier supplier;
    public static RIDBTreeFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDBTreeFileSupplier.class){
                if(supplier == null){
                    supplier = new RIDBTreeFileSupplier();
                }
            }
        }
        return supplier;
    }
}
