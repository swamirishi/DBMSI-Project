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
    private static final short REC_LEN1 = 150;

    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;

    public static String quadrupleHeapFileName = "quadrupleHeapFile";
    public static String entityLabelFileName = "entityLabelHeapFile";
    public static String predicateLabelFileName = "predicateLabelHeapFile";

    public static String subjectBTreeFileName = "SubjectLabelBTreeIndexFile";
    public static String predicateBTreeFileName = "PredicateLabelBTreeIndexFile";
    public static String objectBTreeFileName = "ObjectLabelBTreeIndexFile";
    public static String qidBTreeFileName = "QIDBTreeIndex";

    private int indexType = 0;

    private LIDBTreeFile<Void> subjectBtreeIndexFile;
    private LIDBTreeFile<Void> predicateBtreeIndexFile;
    private LIDBTreeFile<Void> objectBtreeIndexFile;
    private QIDBTreeFile<List<?>> qidBtreeFile;

    private int subjectCount = 0; //TODO Sure these values are updated correctly?
    private int objectCount = 0;

    public RDFDB() throws HFDiskMgrException, HFException, HFBufMgrException, IOException {
//        quadrupleHeapFile = new QuadrupleHeapFile(quadrupleHeapFileName);
//        entityLabelHeapFile = new LabelHeapFile(entityLabelFileName);
//        predicateLabelHeapFile = new LabelHeapFile(predicateLabelFileName);
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
        List<KeyClassManager> keyClassManagers = null;
        switch (indexType) {
            case 1:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
            case 2:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                break;
            case 3:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier());
                break;
            case 4:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier());
                break;
            case 5:
                keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier());
                break;
        }
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        QIDBTreeFile<List<?>> qtf = new QIDBTreeFile<List<?>>(qidBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return idListKeyClassManager;
            }
        };
        this.qidBtreeFile = qtf;
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
        return quadrupleHeapFile.getRecCnt();
    }

    public int getEntityCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return entityLabelHeapFile.getRecCnt();
    }

    public int getPredicateCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return predicateLabelHeapFile.getRecCnt();
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
            lid = getLIDEntityFromHeapFileScan("entityLabelHeapFile", entityLabel);
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
            this.closeEntityBTreeFile(isSubject);
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeEntityBTreeFile(boolean isSubject) throws HashEntryNotFoundException, InvalidFrameNumberException, PageUnpinnedException, ReplacerException {
//        if(isSubject) {
            subjectBtreeIndexFile.close();
//        }else {
            objectBtreeIndexFile.close();
//        }
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
        } else {
            Quadruple found = quadrupleHeapFile.getRecord(qid);
            if (givenQuadruple.getValue() >= found.getValue()) {
                quadrupleHeapFile.updateRecord(qid, givenQuadruple);
            }
        }
        qidBtreeFile.close();
        return qid;

        //No need to fetch again
//        QID qidFoundQ = getQIDFromHeapFileScan(foundQ.getQuadrupleByteArray());

//        if (thisQuadruple.getValue() > foundQ.getValue()) {

        //if record exist, store only the quadruple with higher confidence!
//            boolean updated = quadrupleHeapFile.updateRecord(qid, thisQuadruple);
//            QID qidThisQuadruple = new QID();
//            if(updated) {
//                qidThisQuadruple = getQIDFromHeapFileScan(thisQuadruple.getQuadrupleByteArray());
//            }

//            return qidThisQuadruple;
//        }

//        return qid;
    }

    public boolean deleteQuadruple(byte[] quadruplePtr) throws Exception {
        QID qid = getQIDFromHeapFileScan(quadruplePtr);
        return quadrupleHeapFile.deleteRecord(qid);
    }

    public Stream openStream(int orderType, String subjectFilter, String predicateFilter,
                             String objectFilter, Double confidenceFilter) {
        Stream stream = null;
        try {
            stream = new Stream(this, orderType, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
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
//        if(this.indexType !=3 || this.indexType)
//        LID lid = new LID();
//        LScan scan = (LScan) getCorresponsingHeapFile(heapFileName).openScan();
//        boolean isFound = false;
//        Label label = scan.getNext(lid);
//        while (label!=null) {
//            if (inputLabel.equals(label.getLabel())) {
////                System.out.println("Found " + inputLabel + " wont create new");
//                isFound = true;
//                break;
//            }
//            label = scan.getNext(lid);
//        }
//        if (!isFound) {
//            lid.getPageNo().pid = -1;
//        }
//        scan.closescan();
//        return lid;
        LID predicateId = IndexUtils.isLabelRecordInBtreeFound(predicateBtreeIndexFile, inputLabel);
        return predicateId;
    }

    LabelHeapFile getCorresponsingHeapFile(String inputLabel) throws HFDiskMgrException, HFException, HFBufMgrException, IOException {
        switch (inputLabel) {
            case "entityLabelHeapFile":
                return this.entityLabelHeapFile;
            case "predicateLabelHeapFile":
                return this.predicateLabelHeapFile;
        }
        return new LabelHeapFile("tempHeapFile");
    }

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException, KeyTooLongException, UnknownKeyTypeException, IndexException, KeyNotMatchException, UnknownIndexTypeException, InvalidTypeException, IteratorException, HashEntryNotFoundException, ConstructPageException, ScanIteratorException, PinPageException, InvalidFrameNumberException, PageUnpinnedException, UnpinPageException, ReplacerException {
        Quadruple q = new Quadruple(inputData, 0, inputData.length);
        List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        List<?> ids = Arrays.asList(q.getSubject().returnLid(), q.getPredicate().returnLid(), q.getObject().returnLid());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        QID qid = IndexUtils.getQIDFromQIDTree(qidBtreeFile, idListKeyClassManager, ids);
        return qid;
    }

    private Pair<QID, Quadruple> getQIDQuadrupleFromHeapFileScan(Quadruple q) throws InvalidTupleSizeException, IOException, KeyTooLongException, UnknownKeyTypeException, IndexException, KeyNotMatchException, UnknownIndexTypeException, InvalidTypeException {
        List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        List<?> ids = Arrays.asList(q.getSubject().returnLid(), q.getPredicate().returnLid(), q.getObject().returnLid());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        Pair<QID, Quadruple> itr = IndexUtils.getQIDQuadrupleFromQIDTree(quadrupleHeapFileName, qidBTreeFileName, idListKeyClassManager, ids, 1);
        return itr;
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

    public QIDBTreeFile<List<?>> getQidBtreeFile() {
        return qidBtreeFile;
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
        List keyList;

        switch (indexType) {
            case 1:
                keyList = Arrays.asList(subjectId, predicateId, objectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 2:
                keyList = Arrays.asList(predicateId, subjectId, objectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 3:
                keyList = Arrays.asList(subjectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 4:
                keyList = Arrays.asList(predicateId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 5:
                keyList = Arrays.asList(objectId);
                qidBtreeFile.insert(keyList, qid);
                break;

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
            if (this.qidBtreeFile != null) {
                qidBtreeFile.close();
                //dup_tree.destroyFile();
            }
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
