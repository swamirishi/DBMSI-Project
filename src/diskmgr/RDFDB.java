package diskmgr;

import btree.BTIndexPage;
import global.*;
import heap.*;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;

import java.io.IOException;
import java.util.Arrays;

public class RDFDB extends DB{
    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;
    private int entityCount = 0;
    private int quadrupleCount = 0;
    private int predicateCount = 0;
    private int subjectCount = 0;
    private int objectCount = 0;

    public RDFDB(){

    }
    public void init(int type) {

//        BTIndexPage btIndexPage = new BTIndexPage();
        try {
            quadrupleHeapFile = new QuadrupleHeapFile("quadrupleHeapFile");
            entityLabelHeapFile = new LabelHeapFile("entityLabelHeapFile");
            predicateLabelHeapFile = new LabelHeapFile("predicateLabelHeapFile");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getQuadrupleCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
//        return quadrupleHeapFile.getRecCnt();
        return quadrupleCount;
    }

    public int getEntityCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
//        return entityLabelHeapFile.getRecCnt();
        return entityCount;
    }

    public int getPredicateCnt() throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
//        return predicateLabelHeapFile.getRecCnt();
        return  predicateCount;
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
            entityCount++;
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteEntity(String entityLabel) {
        try {
            LID lid = getLIDFromHeapFileScan(entityLabel);
            entityCount--;
            return entityLabelHeapFile.deleteRecord(lid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PID insertPredicate(String predicateLabel) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException {
        LID lid = predicateLabelHeapFile.insertRecord(new Label(predicateLabel).getLabelByteArray());
        predicateCount++;
        return lid.returnPid();
    }

    public boolean deletePredicate(String predicateLabel) throws Exception {
        LID lid = getLIDFromHeapFileScan(predicateLabel);
        predicateCount--;
        return predicateLabelHeapFile.deleteRecord(lid);
    }

    //Need to return QID. Change type void to QID
    public QID insertQuadruple(byte[] quadruplePtr) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        quadrupleCount++;
        return quadrupleHeapFile.insertRecord(quadruplePtr);
    }

    public boolean deleteQuadruple(byte[] quadruplePtr) throws Exception {
        QID qid = getQIDFromHeapFileScan(quadruplePtr);
        quadrupleCount--;
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

    public void incrementSubject() {
        subjectCount++;
    }

    public void incrementObject() {
        objectCount++;
    }
}
