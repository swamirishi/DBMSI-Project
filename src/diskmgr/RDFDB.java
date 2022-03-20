package diskmgr;

import btree.*;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.*;
import heap.*;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RDFDB {

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

    private int subjectCount = 0;
    private int objectCount = 0;

    public RDFDB(int type) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        try {
            indexType = type;
            quadrupleHeapFile = new QuadrupleHeapFile(quadrupleHeapFileName);
            entityLabelHeapFile = new LabelHeapFile(entityLabelFileName);
            predicateLabelHeapFile = new LabelHeapFile(predicateLabelFileName);

            initializeLabelBTreeFiles();
            initializeIndexesAsPerType();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeIndexesAsPerType() throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        List<KeyClassManager> list = Arrays.asList(LIDKeyClassManager.getSupplier(),LIDKeyClassManager.getSupplier());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(list,20,10);
        QIDBTreeFile<List<?>> qtf = new QIDBTreeFile<List<?>>(qidBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return idListKeyClassManager;
            }
        };
        this.qidBtreeFile = qtf;
    }

    private void initializeLabelBTreeFiles() throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        subjectBtreeIndexFile = new LIDBTreeFile<Void>(subjectBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };

        predicateBtreeIndexFile = new LIDBTreeFile<Void>(predicateBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };

        objectBtreeIndexFile = new LIDBTreeFile<Void>(objectBTreeFileName, AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
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
    public EID insertEntity(String entityLabel) {
        try {
//            LID lid = getLIDFromHeapFileScan(entityLabel);
//            if (lid.getPageNo().pid == INVALID_PAGE) {
            LID lid = entityLabelHeapFile.insertRecord(new Label(entityLabel).getLabelByteArray());
//            }
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteEntity(String entityLabel) {
        try {
            LID lid = getLIDFromHeapFileScan(entityLabel);
            return entityLabelHeapFile.deleteRecord(lid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PID insertPredicate(String predicateLabel) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException, InvalidTypeException {
        LID lid = predicateLabelHeapFile.insertRecord(new Label(predicateLabel).getLabelByteArray());
        return lid.returnPid();
    }

    public boolean deletePredicate(String predicateLabel) throws Exception {
        LID lid = getLIDFromHeapFileScan(predicateLabel);
        return predicateLabelHeapFile.deleteRecord(lid);
    }

    //Need to return QID. Change type void to QID
    public QID insertQuadruple(byte[] quadruplePtr) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        return quadrupleHeapFile.insertRecord(quadruplePtr);
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

    private LID getLIDFromHeapFileScan(String inputLabel) throws InvalidTupleSizeException, IOException {
        LID lid = new LID();
        LScan scan = (LScan) entityLabelHeapFile.openScan();
        boolean isFound = false;
        while (true) {
            Label label = scan.getNext(lid);
            if (label != null && Arrays.equals(inputLabel.getBytes(), label.getLabelByteArray())) {
                System.out.println(inputLabel);
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            lid.getPageNo().pid = -1;
        }
        return lid;
    }

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException {
        QID qid = new QID();
        TScan scan = (TScan) quadrupleHeapFile.openScan();
        boolean isFound = false;
        while (true) {
            Quadruple quadruple = scan.getNext(qid);
            if (quadruple != null && Arrays.equals(inputData, quadruple.getQuadrupleByteArray())) {
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            qid.pageNo.pid = -1;
        }
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

        switch (indexType){
            case 1:
                keyList = Arrays.asList(subjectId, objectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 2:
                keyList = Arrays.asList(objectId, subjectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 3:
                keyList = Arrays.asList(confidence, subjectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 4:
                keyList = Arrays.asList(confidence, objectId);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 5:
                keyList = Arrays.asList(predicateId, subjectId, objectId);
                qidBtreeFile.insert(keyList, qid);
                break;
        }
    }
}
