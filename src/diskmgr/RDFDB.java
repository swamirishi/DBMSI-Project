package diskmgr;

import btree.AddFileEntryException;
import btree.BTIndexPage;
import btree.ConstructPageException;
import btree.GetFileEntryException;
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

public class RDFDB{

    private static final short REC_LEN1 = 150;

    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;

    private LIDBTreeFile<Void> btreeIndexFile1;
    private LIDBTreeFile<Void> btreeIndexFile2;
    private LIDBTreeFile<Void> btreeIndexFile3;

    private int subjectCount = 0;
    private int objectCount = 0;

    public RDFDB(int type) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException {
//        BTIndexPage btIndexPage = new BTIndexPage();
        try {
            SystemDefs.MINIBASE_RESTART_FLAG = true;
            quadrupleHeapFile = new QuadrupleHeapFile("quadrupleHeapFile");
            entityLabelHeapFile = new LabelHeapFile("entityLabelHeapFile");
            predicateLabelHeapFile = new LabelHeapFile("predicateLabelHeapFile");

            btreeIndexFile1 = new LIDBTreeFile<Void>("BTreeIndexFile1", AttrType.attrString, REC_LEN1, 1/*delete*/) {
                @Override
                public KeyClassManager<Void> getKeyClassManager() {
                    return null;
                }
            };

            btreeIndexFile2 = new LIDBTreeFile<Void>("BTreeIndexFile2", AttrType.attrString, REC_LEN1, 1/*delete*/) {
                @Override
                public KeyClassManager<Void> getKeyClassManager() {
                    return null;
                }
            };

            btreeIndexFile3 = new LIDBTreeFile<Void>("BTreeIndexFile3", AttrType.attrString, REC_LEN1, 1/*delete*/) {
                @Override
                public KeyClassManager<Void> getKeyClassManager() {
                    return null;
                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return btreeIndexFile1;
    }

    public LIDBTreeFile<Void> getBtreeIndexFile2() {
        return btreeIndexFile2;
    }

    public LIDBTreeFile<Void> getBtreeIndexFile3() {
        return btreeIndexFile3;
    }
}
