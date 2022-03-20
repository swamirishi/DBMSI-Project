package diskmgr;

import btree.*;
import btree.label.LIDBTreeFile;
import global.*;
import heap.*;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;
import utils.supplier.keyclass.KeyClassManager;
import java.io.IOException;
import java.util.Arrays;

public class RDFDB {

    private static final short REC_LEN1 = 150;

    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;

    private static String subjectBTreeFileName = "SubjectLabelBTreeIndexFile";
    private static String predicateBTreeFileName = "PredicateLabelBTreeIndexFile";
    private static String objectBTreeFileName = "ObjectLabelBTreeIndexFile";
    private int indexType = 0;

    private LIDBTreeFile<Void> subjectBtreeIndexFile;
    private LIDBTreeFile<Void> predicateBtreeIndexFile;
    private LIDBTreeFile<Void> objectBtreeIndexFile;

    private int subjectCount = 0;
    private int objectCount = 0;

    public RDFDB(int type) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        try {
            indexType = type;
            quadrupleHeapFile = new QuadrupleHeapFile("quadrupleHeapFile");
            entityLabelHeapFile = new LabelHeapFile("entityLabelHeapFile");
            predicateLabelHeapFile = new LabelHeapFile("predicateLabelHeapFile");

            initializeLabelBTreeFiles();
            initializeIndexesAsPerType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeIndexesAsPerType(int type) {
        switch (type){
            case 1:
                break;
            case 2:
            case 3:
            case 4:
            case 5:
        }
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

    public LIDBTreeFile<Void> getBtreeIndexFile1() {
        return subjectBtreeIndexFile;
    }

    public LIDBTreeFile<Void> getBtreeIndexFile2() {
        return predicateBtreeIndexFile;
    }

    public LIDBTreeFile<Void> getBtreeIndexFile3() {
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
}
