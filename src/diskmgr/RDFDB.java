package diskmgr;

import Phase3.QueryUtils;
import btree.*;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.*;
import heap.*;
import index.IndexException;
import index.IndexUtils;
import index.UnknownIndexTypeException;
import index.indexOptions.QIDIndexOptions;
import iterator.UnknownKeyTypeException;
import javafx.util.Pair;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RDFDB extends DB {

    private String curr_dbname;                //RDF Database name
    public static final short REC_LEN1 = 150;

    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;

    public static String quadrupleHeapFileName = "quadrupleHeapFile";
    public static String entityLabelFileName = "entityLabelHeapFile";
    public static String predicateLabelFileName = "predicateLabelHeapFile";

    public static String subjectBTreeFileName = "SubjectLabelBTreeIndexFile";
    public static String predicateBTreeFileName = "PredicateLabelBTreeIndexFile";
    public static String objectBTreeFileName = "ObjectLabelBTreeIndexFile";

    public static String sopQidBtreeFileName = "sopQidBtreeFile";
    public static String spQidBtreeFileName = "spQidBtreeFile";
    public static String ospQidBtreeFileName = "ospQidBtreeFile";
    public static String opQidBtreeFileName = "opQidBtreeFile";

    private int indexType = 0;

    private LIDBTreeFile<Void> subjectBtreeIndexFile;
    private LIDBTreeFile<Void> predicateBtreeIndexFile;
    private LIDBTreeFile<Void> objectBtreeIndexFile;
    private QIDBTreeFile<List<?>> sopQidBtreeFile;
    private QIDBTreeFile<List<?>> spQidBtreeFile;
    private QIDBTreeFile<List<?>> ospQidBtreeFile;
    private QIDBTreeFile<List<?>> opQidBtreeFile;

    private int subjectCount = 0; //TODO Sure these values are updated correctly?
    private int objectCount = 0;

    private int quadrupleCount = 0;

    private int predicateCount = 0;

    public RDFDB() throws HFDiskMgrException, HFException, HFBufMgrException, IOException {
//        quadrupleHeapFile = new QuadrupleHeapFile(quadrupleHeapFileName);
//        entityLabelHeapFile = new LabelHeapFile(entityLabelFileName);
//        predicateLabelHeapFile = new LabelHeapFile(predicateLabelFileName);
        super();
    }

    public void setRDFDBProperties(int type) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        try {
            indexType = type;
            quadrupleHeapFile = new QuadrupleHeapFile(quadrupleHeapFileName);
            entityLabelHeapFile = new LabelHeapFile(entityLabelFileName);
            predicateLabelHeapFile = new LabelHeapFile(predicateLabelFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeIndexesAsPerType() throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        QIDIndexOptions qidIndexOptions = new QIDIndexOptions();
        KeyClassManager sopIdListKeyClassManager = qidIndexOptions.indexKeyClassManagerForIndex(1);
        this.sopQidBtreeFile = new QIDBTreeFile<List<?>>(sopQidBtreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return sopIdListKeyClassManager;
            }
        };

        KeyClassManager spIdListKeyClassManager = qidIndexOptions.indexKeyClassManagerForIndex(2);
        this.spQidBtreeFile = new QIDBTreeFile<List<?>>(spQidBtreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return spIdListKeyClassManager;
            }
        };

        KeyClassManager ospIdListKeyClassManager = qidIndexOptions.indexKeyClassManagerForIndex(3);
        this.ospQidBtreeFile = new QIDBTreeFile<List<?>>(ospQidBtreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return ospIdListKeyClassManager;
            }
        };

        KeyClassManager opIdListKeyClassManager = qidIndexOptions.indexKeyClassManagerForIndex(4);
        this.opQidBtreeFile = new QIDBTreeFile<List<?>>(ospQidBtreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return opIdListKeyClassManager;
            }
        };
    }

    public void initializePredicateBTreeFile() throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        predicateBtreeIndexFile = new LIDBTreeFile<Void>(predicateBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
    }

    public void initializeEntityBTreeFiles() throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
//        if(isSubject) {
        subjectBtreeIndexFile = new LIDBTreeFile<Void>(subjectBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
//        }else {

        objectBtreeIndexFile = new LIDBTreeFile<Void>(objectBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
//        }
    }

    public int getQuadrupleCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return quadrupleCount;
    }

    public int getEntityCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return subjectCount + objectCount;
    }

    public int getPredicateCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return predicateCount;
    }

    public int getSubjectCnt() {
        return subjectCount;
    }

    public int getObjectCnt() {
        return objectCount;
    }

    //Need to return EID
    public EID insertEntity(String entityLabel, boolean isSubject) {
        try {
            initializeEntityBTreeFiles();
            LID lid = null;
//            lid = getLIDEntityFromHeapFileScan("entityLabelHeapFile", entityLabel);
            LID subjectId = IndexUtils.isLabelRecordInBtreeFound(subjectBtreeIndexFile, entityLabel);
            LID objectId = IndexUtils.isLabelRecordInBtreeFound(objectBtreeIndexFile, entityLabel);
            if(subjectId!=null && objectId!=null){
                lid = subjectId;
            }else if(subjectId!=null){
                lid = subjectId;
                if(!isSubject){
                    objectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                }
            }else if(objectId!=null){
                lid = objectId;
                if(isSubject){
                    subjectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                }
            }
            if (lid == null) {
                lid = entityLabelHeapFile.insertRecord(new Label(entityLabel).getLabelByteArray());
//
//                updating subject/object counters
                if (isSubject)
                    subjectCount++;
                else
                    objectCount++;
//
                if (isSubject) {
                    subjectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                } else {
                    objectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                }
            }
            this.closeEntityBTreeFile();
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeEntityBTreeFile() throws HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
//        if(isSubject) {
        subjectBtreeIndexFile.close();
//        }else {
        objectBtreeIndexFile.close();
//        }
    }

    public void closePredicateBTreeFile() throws HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
        predicateBtreeIndexFile.close();
    }

    public boolean deleteEntity(String entityLabel, boolean isSubject) {
        try {
            LID lid = getLIDEntityFromHeapFileScan("entityLabelHeapFile", entityLabel);
            //updating subject/object counters
            if (isSubject)
                subjectCount--;
            else
                objectCount--;

            return entityLabelHeapFile.deleteRecord(lid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PID insertPredicate(String predicateLabel) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException, InvalidTypeException {

        try {
            initializePredicateBTreeFile();
            LID lid;
            lid = getLIDPredicateFromHeapFileScan("predicateLabelHeapFile", predicateLabel);
            if (lid == null) {
                lid = predicateLabelHeapFile.insertRecord(new Label(predicateLabel).getLabelByteArray());
                predicateBtreeIndexFile.insert(new StringKey(predicateLabel), lid);
                predicateCount++;
            }
            predicateBtreeIndexFile.close();
            return lid.returnPid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deletePredicate(String predicateLabel) throws Exception {
        LID lid = getLIDPredicateFromHeapFileScan("predicateLabelHeapFile", predicateLabel);
        return predicateLabelHeapFile.deleteRecord(lid);
    }

    //Need to return QID. Change type void to QID
    public QID insertQuadruple(byte[] inputData) throws Exception {
        initializeIndexesAsPerType();
        Quadruple givenQuadruple = new Quadruple(inputData, 0, inputData.length);
        QID qid = getQIDFromHeapFileScan(inputData);
        if (qid == null) {
            qid = quadrupleHeapFile.insertRecord(inputData);
            this.insertInQIDBTree(givenQuadruple, qid);
            quadrupleCount++;
        } else {
            Quadruple found = quadrupleHeapFile.getRecord(qid);
            if (givenQuadruple.getValue() >= found.getValue()) {
                quadrupleHeapFile.updateRecord(qid, givenQuadruple);
            }
        }
        this.closeQIDBTreeFiles();
        return qid;
    }

    public boolean deleteQuadruple(byte[] quadruplePtr) throws Exception {
        QID qid = getQIDFromHeapFileScan(quadruplePtr);
        return quadrupleHeapFile.deleteRecord(qid);
    }

    public Stream openStream(int orderType, String subjectFilter, String predicateFilter,
                             String objectFilter, Double confidenceFilter) {
        Stream stream = null;
        try {
            this.initializeEntityBTreeFiles();
            this.initializePredicateBTreeFile();
            LID subjectId = IndexUtils.isLabelRecordInBtreeFound(this.getSubjectBtreeIndexFile(),
                    subjectFilter);
            LID predicateId = IndexUtils.isLabelRecordInBtreeFound(this.getPredicateBtreeIndexFile(),
                    predicateFilter);
            LID objectId = IndexUtils.isLabelRecordInBtreeFound(this.getObjectBtreeIndexFile(),
                    objectFilter);
            this.closeEntityBTreeFile();
            this.closePredicateBTreeFile();
            if((!"*".equals(subjectFilter)) && subjectId==null){
                subjectId = new LID(new PageId(GlobalConst.INVALID_PAGE),GlobalConst.INVALID_PAGE);
            }
            if((!"*".equals(predicateFilter)) && predicateId==null){
                predicateId = new LID(new PageId(GlobalConst.INVALID_PAGE),GlobalConst.INVALID_PAGE);
            }
            if((!"*".equals(objectFilter)) && objectId==null){
                objectId = new LID(new PageId(GlobalConst.INVALID_PAGE),GlobalConst.INVALID_PAGE);
            }
            stream = new Stream(this, orderType, subjectId, predicateId, objectId, confidenceFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Opened new stream: ");
        return stream;
    }

    private LID getLIDEntityFromHeapFileScan(String heapFileName, String inputLabel) throws InvalidTupleSizeException, IOException, HFDiskMgrException, HFException, HFBufMgrException, IndexException, UnknownKeyTypeException, UnknownIndexTypeException, InvalidTypeException, IteratorException, ConstructPageException, KeyNotMatchException, ScanIteratorException, PinPageException, UnpinPageException, HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
        LID subjectId = IndexUtils.isLabelRecordInBtreeFound(subjectBtreeIndexFile, inputLabel);
        if (subjectId != null) {
            return subjectId;
        }
        LID objectId = IndexUtils.isLabelRecordInBtreeFound(objectBtreeIndexFile, inputLabel);
        return objectId;
    }

    private LID getLIDPredicateFromHeapFileScan(String heapFileName, String inputLabel) throws InvalidTupleSizeException, IOException, HFDiskMgrException, HFException, HFBufMgrException, IndexException, UnknownKeyTypeException, UnknownIndexTypeException, InvalidTypeException, IteratorException, ConstructPageException, KeyNotMatchException, ScanIteratorException, PinPageException, UnpinPageException, HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
        LID predicateId = IndexUtils.isLabelRecordInBtreeFound(predicateBtreeIndexFile, inputLabel);
        return predicateId;
    }

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException, KeyTooLongException, UnknownKeyTypeException, IndexException, KeyNotMatchException, UnknownIndexTypeException, InvalidTypeException, IteratorException, HashEntryNotFoundException, ConstructPageException, ScanIteratorException, PinPageException, InvalidFrameNumberException, PageUnpinnedException, UnpinPageException, ReplacerException {
        Quadruple q = new Quadruple(inputData, 0, inputData.length);
        List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        List<?> ids = Arrays.asList(q.getSubject().returnLid(), q.getObject().returnLid(), q.getPredicate().returnLid());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        QID qid = IndexUtils.getQIDFromQIDTree(sopQidBtreeFile, idListKeyClassManager, ids);
        return qid;
    }

    public QuadrupleHeapFile getQuadrupleHeapFile() {
        return this.quadrupleHeapFile;
    }

    public LabelHeapFile getEntityLabelHeapFile() {
        return entityLabelHeapFile;
    }

    public LabelHeapFile getPredicateLabelHeapFile() {
        return predicateLabelHeapFile;
    }

    public LIDBTreeFile<Void> getSubjectBtreeIndexFile() {
        return subjectBtreeIndexFile;
    }

    public LIDBTreeFile<Void> getPredicateBtreeIndexFile() {
        return predicateBtreeIndexFile;
    }

    public LIDBTreeFile<Void> getObjectBtreeIndexFile() {
        return objectBtreeIndexFile;
    }

    public void insertInLabelBTree(String subjectLabel, EID subjectId, String predicateLabel, PID predicateId, String objectLabel, EID objectId) throws IteratorException, ConstructPageException, ConvertException, InsertException, IndexInsertRecException, LeafDeleteException, NodeNotMatchException, LeafInsertRecException, PinPageException, IOException, UnpinPageException, DeleteRecException, KeyTooLongException, KeyNotMatchException, IndexSearchException {
        subjectBtreeIndexFile.insert(new StringKey(subjectLabel), subjectId.returnLid());
        predicateBtreeIndexFile.insert(new StringKey(predicateLabel), predicateId.returnLid());
        objectBtreeIndexFile.insert(new StringKey(objectLabel), objectId.returnLid());
    }

    public int getIndexType() {
        return indexType;
    }

    public void insertInQIDBTree(Quadruple q, QID qid) throws IteratorException, ConstructPageException, ConvertException, InsertException, LeafDeleteException, IndexInsertRecException, NodeNotMatchException, LeafInsertRecException, PinPageException, IOException, UnpinPageException, DeleteRecException, KeyTooLongException, KeyNotMatchException, IndexSearchException {
        LID subjectId = q.getSubject().returnLid();
        LID predicateId = q.getPredicate().returnLid();
        LID objectId = q.getObject().returnLid();
        float confidence = q.getValue();
        List sopkeyList = Arrays.asList(subjectId, objectId, predicateId);
        List spkeyList = Arrays.asList(subjectId, predicateId);
        List ospkeyList = Arrays.asList(objectId, subjectId, predicateId);
        List opKeyList = Arrays.asList(objectId, predicateId);
        QIDIndexOptions qidIndexOptions = new QIDIndexOptions();

        KeyClassManager keyClassManager = qidIndexOptions.indexKeyClassManagerForIndex(1);
        KeyClass keyClass = qidIndexOptions.getKeyClassForIndexOption(keyClassManager, 1, q);
        sopQidBtreeFile.insert(keyClass, qid);

        KeyClassManager keyClassManager2 = qidIndexOptions.indexKeyClassManagerForIndex(2);
        KeyClass keyClass2 = qidIndexOptions.getKeyClassForIndexOption(keyClassManager2, 2, q);
        spQidBtreeFile.insert(keyClass2, qid);

        KeyClassManager keyClassManager3 = qidIndexOptions.indexKeyClassManagerForIndex(3);
        KeyClass keyClass3 = qidIndexOptions.getKeyClassForIndexOption(keyClassManager3, 3, q);
        ospQidBtreeFile.insert(keyClass3, qid);

        KeyClassManager keyClassManager4 = qidIndexOptions.indexKeyClassManagerForIndex(4);
        KeyClass keyClass4 = qidIndexOptions.getKeyClassForIndexOption(keyClassManager4, 4, q);
        opQidBtreeFile.insert(keyClass4, qid);
    }

    public void closeQIDBTreeFiles() throws HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
        if (sopQidBtreeFile != null) {
            sopQidBtreeFile.close();
        }
        if (spQidBtreeFile != null) {
            spQidBtreeFile.close();
        }
        if (ospQidBtreeFile != null) {
            ospQidBtreeFile.close();
        }
        if (opQidBtreeFile != null) {
            opQidBtreeFile.close();
        }
    }

    public void setIndexType(int indexoption) {
        this.indexType = indexoption;
    }

    /**
     * Open an existing rdf database
     *
     * @param name Database name
     */
    public void rdfcloseDB()
            throws PageUnpinnedException, InvalidFrameNumberException, HashEntryNotFoundException, ReplacerException {
        try {

            if (this.subjectBtreeIndexFile != null) {
                this.subjectBtreeIndexFile.close();
            }
            if (predicateBtreeIndexFile != null) {
                predicateBtreeIndexFile.close();
            }
            if (objectBtreeIndexFile != null) {
                objectBtreeIndexFile.close();
            }
            this.closeQIDBTreeFiles();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void openrdfDB(String dbname, int type) {
        curr_dbname = new String(dbname);
        try {
            openDB(dbname);
            this.setRDFDBProperties(type);
        } catch (Exception e) {
            System.err.println("" + e);
            e.printStackTrace();
            Runtime.getRuntime().exit(1);
        }
    }

    /**
     * Create a new RDF database
     *
     * @param dbname    Database name
     * @param num_pages Num of pages to allocate for the database
     * @param type      different indexing types to use for the database
     */
    public void openrdfDB(String dbname, int num_pages, int type) {
        curr_dbname = new String(dbname);
        try {
            openDB(dbname, num_pages);
            this.setRDFDBProperties(type);
        } catch (Exception e) {

            System.err.println("" + e);
            e.printStackTrace();
            Runtime.getRuntime().exit(1);
        }
    }

    public LabelHeapFile getQueryEntityLabelHeapFile() {
        return QueryUtils.getQueryEntityLabelHeapFile();
    }
}
