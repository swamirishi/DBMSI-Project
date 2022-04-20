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
import utils.supplier.keyclass.KeyClassManager;

import java.io.IOException;

public class RIDBTreeFileSupplier<K> implements BTreeFileSupplier<RID, Tuple,K> {
    @Override
    public BTreeFileI<RID, Tuple,K> getBTreeFile(String name,KeyClassManager<K> keyClassManager) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new BTreeFile<K>(name) {
            @Override
            public KeyClassManager<K> getKeyClassManager() {
                return keyClassManager;
            }
        };
    }
    
    public RIDBTreeFileSupplier() {
    }
}
