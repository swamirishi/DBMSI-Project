package utils.supplier.btfile;

import btree.AddFileEntryException;
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
import utils.supplier.keyclass.KeyClassManager;

import java.io.IOException;

public class LIDBTreeFileSupplier<K> implements BTreeFileSupplier<LID, Label,K> {
    @Override
    public BTreeFileI<LID, Label,K> getBTreeFile(String name, KeyClassManager<K> KeyClassManager) throws ConstructPageException, GetFileEntryException, PinPageException, IOException, AddFileEntryException {
        return new LIDBTreeFile<K>(name) {
            @Override
            public utils.supplier.keyclass.KeyClassManager<K> getKeyClassManager() {
                return KeyClassManager;
            }
        };
    }
    
    public LIDBTreeFileSupplier() {
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
