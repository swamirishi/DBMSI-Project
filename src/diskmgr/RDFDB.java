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

public class RDFDB extends DB {
    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;
    private int subjectCount = 0;
    private int objectCount = 0;

    public RDFDB(int type) {
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
            LID lid = getLIDFromHeapFileScan(entityLabel);
            if (lid.getPageNo().pid == INVALID_PAGE) {
                lid = entityLabelHeapFile.insertRecord(entityLabel.getBytes());
            }
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

    public PID insertPredicate(String predicateLabel) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        LID lid = predicateLabelHeapFile.insertRecord(predicateLabel.getBytes());
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
                             String objectFilter, double confidenceFilter) {
        Stream stream = new Stream(this, orderType, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
        System.out.println("Opened new stream: ");
        return stream;
    }

    private LID getLIDFromHeapFileScan(String inputLabel) throws InvalidTupleSizeException, IOException {
        LID lid = new LID();
        LScan scan = entityLabelHeapFile.openScan();
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
            lid.getPageNo().pid = INVALID_PAGE;
        }
        return lid;
    }

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException {
        QID qid = new QID();
        TScan scan = quadrupleHeapFile.openScan();
        boolean isFound = false;
        while (true) {
            Quadruple quadruple = scan.getNext(qid);
            if (quadruple != null && Arrays.equals(inputData, quadruple.getQuadrupleByteArray())) {
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            qid.getPageNo().pid = INVALID_PAGE;
        }
        return qid;
    }

    public QuadrupleHeapFile getQuadrupleHeapFile(){
        return this.quadrupleHeapFile;
    }
}
