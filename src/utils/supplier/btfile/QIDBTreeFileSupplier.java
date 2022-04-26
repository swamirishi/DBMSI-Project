package utils.supplier.btfile;

import btree.BTreeFile;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import btree.quadraple.QIDBTreeFile;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.KeyClassManager;

public class QIDBTreeFileSupplier<K> implements BTreeFileSupplier<QID, Quadruple,K> {
    @Override
    public BTreeFileI<QID, Quadruple,K> getBTreeFile(String name, KeyClassManager<K> keyClassManager) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new QIDBTreeFile<K>(name) {
            @Override
            public KeyClassManager<K> getKeyClassManager() {
                return keyClassManager;
            }
        };
    }
    
    public QIDBTreeFileSupplier() {
    }
    
}
