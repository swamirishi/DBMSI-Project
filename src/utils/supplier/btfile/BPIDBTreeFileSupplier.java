package utils.supplier.btfile;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.basicpattern.BPIDBTreeFile;
import btree.interfaces.BTreeFileI;
import global.BPID;
import utils.supplier.keyclass.KeyClassManager;

public class BPIDBTreeFileSupplier<K> implements BTreeFileSupplier<BPID, BasicPattern,K> {
    @Override
    public BTreeFileI<BPID, BasicPattern,K> getBTreeFile(String name, KeyClassManager<K> keyClassManager) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new BPIDBTreeFile<K>(name) {
            @Override
            public KeyClassManager<K> getKeyClassManager() {
                return keyClassManager;
            }
        };
    }

    public BPIDBTreeFileSupplier() {
    }

}
