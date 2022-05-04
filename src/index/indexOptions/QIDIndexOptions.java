package index.indexOptions;

import btree.*;
import btree.interfaces.BTFileScanI;
import btree.quadraple.QIDBTreeFile;
import diskmgr.RDFDB;
import global.AttrType;
import global.QID;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QIDIndexOptions implements IDIndexOptions<Quadruple>{
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
    public KeyClass getKeyClassForIndexOption(KeyClassManager keyClassManager, int indexOption, Quadruple id) throws KeyTooLongException, KeyNotMatchException {
        switch (indexOption) {
            case 1:
                return keyClassManager.getKeyClass(Arrays.asList(id.getSubject().returnLid(),id.getObject().returnLid(),id.getPredicate().returnLid()));
            case 2:
                return keyClassManager.getKeyClass(Arrays.asList(id.getSubject().returnLid(),id.getPredicate().returnLid()));
            case 3:
                return keyClassManager.getKeyClass(Arrays.asList(id.getObject().returnLid(),id.getSubject().returnLid(),id.getPredicate().returnLid()));
            case 4:
                return keyClassManager.getKeyClass(Arrays.asList(id.getObject().returnLid(),id.getPredicate().returnLid()));
        }
        return null;
    }
    
    @Override
    public BTFileScanI<QID, Quadruple, KeyClassManager> getBTFileScan(int indexOption){
        return null;
    }

    public QIDBTreeFile<List<?>> getBTFile(int indexOption) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        QIDBTreeFile<List<?>> bTreeFile = null;
        String bTreeFileName = null;
        switch (indexOption){
            case 1:
                bTreeFileName = RDFDB.sopQidBtreeFileName;
                break;
            case 2:
                bTreeFileName = RDFDB.spQidBtreeFileName;
                break;
            case 3:
                bTreeFileName = RDFDB.ospQidBtreeFileName;
                break;
            case 4:
                bTreeFileName = RDFDB.opQidBtreeFileName;
                break;
        }
        bTreeFile =  new QIDBTreeFile<List<?>>(bTreeFileName, AttrType.attrString, RDFDB.REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return indexKeyClassManagerForIndex(indexOption);
            }
        };
        return bTreeFile;
    }
    
    @Override
    public List<Integer> getNumberOfOptions() {
        return indexOptions;
    }
    
    public static int getIndexOption(boolean joinOnSubject, boolean subjectFilterGiven, boolean objectFilterGiven){
        if(joinOnSubject){
            return objectFilterGiven?1:2;
        }
        return subjectFilterGiven?3:3;
    }
}
