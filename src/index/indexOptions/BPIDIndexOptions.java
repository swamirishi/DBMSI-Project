package index.indexOptions;

import basicpatternheap.BasicPattern;
import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.util.Arrays;
import java.util.List;

public class BPIDIndexOptions implements IDIndexOptions<BasicPattern>{
    private static final List<Integer> indexOptions = Arrays.asList(1,2,3,4);
    @Override
    public KeyClassManager indexKeyClassManagerForIndex(int indexOption) {
        List<KeyClassManager> keyClassManagers = null;
        switch (indexOption) {
            case 1:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
            case 2:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
            case 3:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
            case 4:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
        }
        
        return new IDListKeyClassManager(keyClassManagers,20,10);
    }
    
    @Override
    public KeyClass getKeyClassForIndexOption(KeyClassManager keyClassManager, int indexOption, BasicPattern id) throws KeyTooLongException, KeyNotMatchException {
        switch (indexOption) {
            case 1:
                return keyClassManager.getKeyClass(Arrays.asList(id.getNode(Quadruple.SUBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.OBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.PREDICTE_NODE_INDEX).returnLid()));
            case 2:
                return keyClassManager.getKeyClass(Arrays.asList(id.getNode(Quadruple.SUBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.PREDICTE_NODE_INDEX).returnLid()));
            case 3:
                return keyClassManager.getKeyClass(Arrays.asList(id.getNode(Quadruple.OBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.SUBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.PREDICTE_NODE_INDEX).returnLid()));
            case 4:
                return keyClassManager.getKeyClass(Arrays.asList(id.getNode(Quadruple.OBJECT_NODE_INDEX).returnLid(),id.getNode(Quadruple.PREDICTE_NODE_INDEX).returnLid()));
        }
        return null;
    }
    
    @Override
    public List<Integer> getNumberOfOptions() {
        return indexOptions;
    }
    
    public static int getIndexOption(boolean joinOnSubject, boolean subjectFilterGiven, boolean objectFilterGiven){
        if(joinOnSubject){
            return objectFilterGiven?1:2;
        }
        return subjectFilterGiven?3:4;
    }
}
