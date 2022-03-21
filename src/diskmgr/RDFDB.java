package diskmgr;

import btree.AddFileEntryException;
import btree.BTIndexPage;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.label.LIDBTreeFile;
import global.*;
import heap.*;
import heap.interfaces.HFile;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;
import utils.supplier.keyclass.KeyClassManager;

import java.io.IOException;
import java.util.Arrays;

import static global.GlobalConst.INVALID_PAGE;

public class RDFDB{

    private static final short REC_LEN1 = 150;

    private QuadrupleHeapFile quadrupleHeapFile;
    private LabelHeapFile entityLabelHeapFile;
    private LabelHeapFile predicateLabelHeapFile;

    private LIDBTreeFile<Void> btreeIndexFile1;
    private LIDBTreeFile<Void> btreeIndexFile2;
    private LIDBTreeFile<Void> btreeIndexFile3;

    private int subjectCount = 0; //TODO Sure these values are updated correctly?
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
            LID lid = getLIDFromHeapFileScan("entityLabelHeapFile", entityLabel);
            if (lid.getPageNo().pid == INVALID_PAGE) {
                lid = entityLabelHeapFile.insertRecord(new Label(entityLabel).getLabelByteArray());
            }
            return lid.returnEid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteEntity(String entityLabel) {
        try {
            LID lid = getLIDFromHeapFileScan("entityLabelHeapFile", entityLabel);
            return entityLabelHeapFile.deleteRecord(lid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PID insertPredicate(String predicateLabel) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException, InvalidTypeException {
        try {
            LID lid = getLIDFromHeapFileScan("predicateLabelHeapFile", predicateLabel);
            if (lid.getPageNo().pid == INVALID_PAGE) {
                lid = predicateLabelHeapFile.insertRecord(new Label(predicateLabel).getLabelByteArray());
            }
            return lid.returnPid();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deletePredicate(String predicateLabel) throws Exception {
        LID lid = getLIDFromHeapFileScan("predicateLabelHeapFile", predicateLabel);
        return predicateLabelHeapFile.deleteRecord(lid);
    }

    //Need to return QID. Change type void to QID
    public QID insertQuadruple(byte[] quadruplePtr) throws Exception {
        Quadruple thisQuadruple = new Quadruple(quadruplePtr, 0, quadruplePtr.length);
        QID qid = new QID();
        Quadruple foundQ = null;
        boolean found = false;
        QuadrupleUtils.rdfdb = this;
        try {
            TScan tScan = new TScan(getQuadrupleHeapFile());
            foundQ = tScan.getNext(qid);
            while (foundQ != null) {
                foundQ.setHdr();
                AttrType attrType = new AttrType(AttrType.attrLID);
                if(QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 1, thisQuadruple)==0){
                    if(QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 2, thisQuadruple)==0)
                        if(QuadrupleUtils.CompareQuadrupleWithValue(attrType, foundQ, 3, thisQuadruple)==0) {
                            found = true;
                            break;
                        }
                }
                foundQ = tScan.getNext(qid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!found)
            return quadrupleHeapFile.insertRecord(quadruplePtr);

        //No need to fetch again
//        QID qidFoundQ = getQIDFromHeapFileScan(foundQ.getQuadrupleByteArray());

        if(thisQuadruple.getValue() > foundQ.getValue()) {

            //if record exist, store only the quadruple with higher confidence!
            boolean updated = quadrupleHeapFile.updateRecord(qid, thisQuadruple);
            QID qidThisQuadruple = new QID();
            if(updated) {
                qidThisQuadruple = getQIDFromHeapFileScan(thisQuadruple.getQuadrupleByteArray());
            }

            return qidThisQuadruple;
        }

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
            stream = new Stream(this, orderType, subjectFilter, predicateFilter, objectFilter, confidenceFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Opened new stream: ");
        return stream;
    }

    private LID getLIDFromHeapFileScan(String heapFileName, String inputLabel) throws InvalidTupleSizeException, IOException, HFDiskMgrException, HFException, HFBufMgrException {
        LID lid = new LID();
        LScan scan = (LScan) getCorresponsingHeapFile(heapFileName).openScan();
        boolean isFound = false;
        Label label = scan.getNext(lid);
        while (label!=null) {
            if (inputLabel.equals(label.getLabel())) {
                System.out.println("Found " + inputLabel + " wont create new");
                isFound = true;
                break;
            }
            label = scan.getNext(lid);
        }
        if (!isFound) {
            lid.getPageNo().pid = -1;
        }
        return lid;
    }

    LabelHeapFile getCorresponsingHeapFile(String inputLabel) throws HFDiskMgrException, HFException, HFBufMgrException, IOException {
        switch (inputLabel){
            case "entityLabelHeapFile": return this.entityLabelHeapFile;
            case "predicateLabelHeapFile": return this.predicateLabelHeapFile;
        }
        return new LabelHeapFile("tempHeapFile");
    }

    private QID getQIDFromHeapFileScan(byte[] inputData) throws InvalidTupleSizeException, IOException {
        QID qid = new QID();
        TScan scan = (TScan) quadrupleHeapFile.openScan();
        boolean isFound = false;
        Quadruple quadruple = scan.getNext(qid);
        while (quadruple != null) {
            if (Arrays.equals(inputData, quadruple.getQuadrupleByteArray())) {
                isFound = true;
                break;
            }
            quadruple = scan.getNext(qid);
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
