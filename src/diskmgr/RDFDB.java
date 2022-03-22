package diskmgr;

import btree.*;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.*;
import heap.*;
import heap.interfaces.HFile;
import index.IndexException;
import index.IndexUtils;
import index.UnknownIndexTypeException;
import iterator.UnknownKeyTypeException;
import javafx.util.Pair;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;
import utils.supplier.keyclass.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static global.GlobalConst.INVALID_PAGE;

public class RDFDB extends DB {

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

    public RDFDB(int type) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        try {
            indexType = type;
            quadrupleHeapFile = new QuadrupleHeapFile(quadrupleHeapFileName);
            entityLabelHeapFile = new LabelHeapFile(entityLabelFileName);
            predicateLabelHeapFile = new LabelHeapFile(predicateLabelFileName);
            List<KeyClassManager> keyClassManagers = null;
            initializeLabelBTreeFiles();
            switch (indexType) {
                case 1:
                    keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                    break;
                case 2:
                    keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                    break;
                case 3:
                    keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(),FloatKeyClassManager.getSupplier());
                    break;
                case 4:
                    keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(),FloatKeyClassManager.getSupplier());
                    break;
                case 5:
                    keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(),FloatKeyClassManager.getSupplier());
                    break;
            }
            initializeIndexesAsPerType(keyClassManagers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeIndexesAsPerType(List<KeyClassManager> keyClassManagers) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
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
    public EID insertEntity(String entityLabel, boolean isSubject) {
        try {
            LID lid = getLIDEntityFromHeapFileScan("entityLabelHeapFile", entityLabel);
            if (lid == null) {
                lid = entityLabelHeapFile.insertRecord(new Label(entityLabel).getLabelByteArray());

                //updating subject/object counters
                if (isSubject)
                    subjectCount++;
                else
                    objectCount++;

                if (isSubject) {
                    subjectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                } else {
                    objectBtreeIndexFile.insert(new StringKey(entityLabel), lid);
                }
            }
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            LID lid = getLIDPredicateFromHeapFileScan("predicateLabelHeapFile", predicateLabel);
            if (lid == null) {
                lid = predicateLabelHeapFile.insertRecord(new Label(predicateLabel).getLabelByteArray());
                predicateBtreeIndexFile.insert(new StringKey(predicateLabel), lid);
            }
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
//        Quadruple thisQuadruple = new Quadruple(quadruplePtr, 0, quadruplePtr.length);
//        QID qid = new QID();
//        Quadruple foundQ = null;
//        boolean found = false;
//        QuadrupleUtils.rdfdb = this;
//        try {
//            TScan tScan = new TScan(getQuadrupleHeapFile());
//            foundQ = tScan.getNext(qid);
//            while (foundQ != null) {
//                foundQ.setHdr();
//                AttrType attrType = new AttrType(AttrType.attrLID);
//                if (QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 1, thisQuadruple) == 0) {
//                    if (QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 2, thisQuadruple) == 0)
//                        if (QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 3, thisQuadruple) == 0) {
//                            found = true;
//                            break;
//                        }
//                }
//                foundQ = tScan.getNext(qid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Quadruple givenQuadruple = new Quadruple(inputData, 0, inputData.length);
        Pair<QID, Quadruple> itr = getQIDQuadrupleFromHeapFileScan(givenQuadruple);
        QID qid;
        if (itr==null) {
            qid = quadrupleHeapFile.insertRecord(inputData);
            this.insertInQIDBTree(givenQuadruple, qid);
        }else{
            qid = itr.getKey();
            Quadruple found = itr.getValue();
            if(givenQuadruple.getValue() >= found.getValue()){
                quadrupleHeapFile.updateRecord(qid, givenQuadruple);
            }
        }
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

    private LID getLIDEntityFromHeapFileScan(String heapFileName, String inputLabel) throws InvalidTupleSizeException, IOException, HFDiskMgrException, HFException, HFBufMgrException, IndexException, UnknownKeyTypeException, UnknownIndexTypeException, InvalidTypeException {
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
        LID subjectId = IndexUtils.isLabelRecordInBtreeFound(heapFileName, subjectBTreeFileName, inputLabel);
        if (subjectId != null) {
            return subjectId;
        }
        LID objectId = IndexUtils.isLabelRecordInBtreeFound(heapFileName, objectBTreeFileName, inputLabel);
        return objectId;
    }

    private LID getLIDPredicateFromHeapFileScan(String heapFileName, String inputLabel) throws InvalidTupleSizeException, IOException, HFDiskMgrException, HFException, HFBufMgrException, IndexException, UnknownKeyTypeException, UnknownIndexTypeException, InvalidTypeException {
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
        LID predicateId = IndexUtils.isLabelRecordInBtreeFound(heapFileName, predicateBTreeFileName, inputLabel);
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

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException, KeyTooLongException, UnknownKeyTypeException, IndexException, KeyNotMatchException, UnknownIndexTypeException, InvalidTypeException {
//        QID qid = new QID();
//        TScan scan = (TScan) quadrupleHeapFile.openScan();
//        boolean isFound = false;
//        Quadruple quadruple = scan.getNext(qid);
//        while (quadruple != null) {
//            if (Arrays.equals(inputData, quadruple.getQuadrupleByteArray())) {
//                isFound = true;
//                break;
//            }
//            quadruple = scan.getNext(qid);
//        }
//        if (!isFound) {
//            qid.pageNo.pid = -1;
//        }
//        scan.closescan();
//        return qid;
        Quadruple q = new Quadruple(inputData, 0, inputData.length);
        List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        List<?> ids = Arrays.asList(q.getSubject().returnLid(), q.getPredicate().returnLid(), q.getObject().returnLid());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        QID qid = IndexUtils.getQIDFromQIDTree(quadrupleHeapFileName, qidBTreeFileName, idListKeyClassManager, ids,1);
        return qid;
    }

    private Pair<QID, Quadruple> getQIDQuadrupleFromHeapFileScan(Quadruple q) throws InvalidTupleSizeException, IOException, KeyTooLongException, UnknownKeyTypeException, IndexException, KeyNotMatchException, UnknownIndexTypeException, InvalidTypeException {
        List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        List<?> ids = Arrays.asList(q.getSubject().returnLid(), q.getPredicate().returnLid(), q.getObject().returnLid());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(keyClassManagers, 20, 10);
        Pair<QID, Quadruple> itr = IndexUtils.getQIDQuadrupleFromQIDTree(quadrupleHeapFileName, qidBTreeFileName, idListKeyClassManager, ids,1);
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
                keyList = Arrays.asList(subjectId,confidence);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 4:
                keyList = Arrays.asList(predicateId,confidence);
                qidBtreeFile.insert(keyList, qid);
                break;
            case 5:
                keyList = Arrays.asList(objectId,confidence);
                qidBtreeFile.insert(keyList, qid);
                break;
            
        }
    }

    public void setIndexType(int indexoption) {
        this.indexType=indexoption;
    }
}
