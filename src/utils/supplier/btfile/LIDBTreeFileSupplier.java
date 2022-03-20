package utils.supplier.btfile;

import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.LID;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDBTreeFileSupplier implements BTreeFileSupplier<LID, Label> {
    @Override
    public BTreeFileI<LID, Label> getBTreeFile(String name) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new LIDBTreeFile(name);
    }
    
    private LIDBTreeFileSupplier() {
    }
    
    private static LIDBTreeFileSupplier supplier;
    public static LIDBTreeFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDBTreeFileSupplier.class){
                if(supplier == null){
                    supplier = new LIDBTreeFileSupplier();
                }
            }
        }
        return supplier;
    }
}
