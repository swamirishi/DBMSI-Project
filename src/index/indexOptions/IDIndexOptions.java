package index.indexOptions;

import btree.*;
import btree.interfaces.BTFileScanI;
import btree.quadraple.QIDBTreeFile;
import global.ID;
import global.QID;
import heap.Tuple;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.KeyClassManager;

import java.io.IOException;
import java.util.List;

public interface IDIndexOptions<T extends Tuple> {
    public KeyClassManager indexKeyClassManagerForIndex(int indexOption);
    public KeyClass getKeyClassForIndexOption(KeyClassManager keyClassManager, int indexOption,T tuple) throws KeyTooLongException, KeyNotMatchException;
    public List<Integer> getNumberOfOptions();
    public QIDBTreeFile<List<?>> getBTFile(int indexOption) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException;
    public BTFileScanI<QID, Quadruple, KeyClassManager> getBTFileScan(int indexOption);
}
