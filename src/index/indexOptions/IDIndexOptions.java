package index.indexOptions;

import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import btree.interfaces.BTFileScanI;
import global.ID;
import global.QID;
import heap.Tuple;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.KeyClassManager;

import java.util.List;

public interface IDIndexOptions<T extends Tuple> {
    public KeyClassManager indexKeyClassManagerForIndex(int indexOption);
    public KeyClass getKeyClassForIndexOption(KeyClassManager keyClassManager, int indexOption,T tuple) throws KeyTooLongException, KeyNotMatchException;
    public List<Integer> getNumberOfOptions();
    @Override
    public BTFileScanI<QID, Quadruple, KeyClassManager> getBTFileScan(int indexOption);
}
