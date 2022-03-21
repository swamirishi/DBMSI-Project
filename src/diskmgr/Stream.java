package diskmgr;

import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import btree.StringKey;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.*;
import heap.*;
import index.IndexException;
import index.UnknownIndexTypeException;
import index.label.LIDIndexScan;
import index.quadraple.QIDIndexScan;
import iterator.*;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleSort;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.THFPage;
import quadrupleheap.TScan;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.IOException;
import java.util.*;

import static global.AttrType.*;
import static global.GlobalConst.INVALID_PAGE;

public class Stream {
    public static QuadrupleSort quadrupleSort;
    public static RDFDB rdfDB;
    public static String subjectFilter;
    public static String predicateFilter;
    public static String objectFilter;
    public static Float confidenceFilter;

//    LIDIndexScan<Void> subjectIndexScan;
//    LIDIndexScan<Void> predicateIndexScan;
//    LIDIndexScan<Void> objectIndexScan;

    public static final int maxLabelLen = 50;
    public int orderType;

    public Stream(RDFDB rdfdb, int orderType, String subjectFil, String predicateFil,
                  String objectFil, Double confidenceFil) throws Exception {
        rdfDB = rdfdb;
        QuadrupleUtils.rdfdb = rdfdb;
        this.orderType = orderType;

        TScan tScan = new TScan(rdfDB.getQuadrupleHeapFile());
        TupleOrder tupleOrders = new TupleOrder(0);

        AttrType[] attrTypes = Quadruple.headerTypes;
        short len = (short) attrTypes.length;
        short[] strSizes = Quadruple.strSizes;

        QuadrupleSort.subjectFilter = subjectFil;
        QuadrupleSort.predicateFilter = predicateFil;
        QuadrupleSort.objectFilter = objectFil;
        QuadrupleSort.confidenceFilter = confidenceFil == null ? 0 : confidenceFil.floatValue();

        subjectFilter = subjectFil;
        predicateFilter = predicateFil;
        objectFilter = objectFil;
        confidenceFilter = confidenceFil == null ? 0 : confidenceFil.floatValue();

        int indexType = rdfdb.getIndexType();

        quadrupleSort = new QuadrupleSort(rdfDB, orderType, attrTypes, len, strSizes,
                tScan, 3, tupleOrders, 31, 1024);

//        subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
//        predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
//        objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
    }


    public Quadruple getNext() throws Exception {
//        int option = rdfDB.getIndexType();
//        if (option == 6) {
//            return quadrupleSort.get_next();
//        } else {
//            orderSubjectPredicateObject();
//            return null;
//        }
        return quadrupleSort.get_next();
    }

    private LIDIndexScan<Void> initializeLabelScan(String heapFileName, String bTreeFileName, String filter) throws IndexException, InvalidTupleSizeException, IOException, UnknownIndexTypeException, InvalidTypeException {
        AttrType[] attrType = new AttrType[1];
        attrType[0] = new AttrType(AttrType.attrString);
        short[] attrSize = new short[1];
        attrSize[0] = maxLabelLen;

        FldSpec[] projlist = new FldSpec[1];
        RelSpec rel = new RelSpec(RelSpec.outer);
        projlist[0] = new FldSpec(rel, 1);

        CondExpr[] expr = new CondExpr[2];
        expr[0] = new CondExpr();
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
        expr[0].operand2.string = filter;
        expr[0].next = null;
        expr[1] = null;

        LIDIndexScan<Void> iscan = new LIDIndexScan<Void>(new IndexType(IndexType.B_Index),
                heapFileName,
                bTreeFileName,
                attrType,
                attrSize,
                1,
                1,
                projlist,
                expr,
                1,
                false) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
        return iscan;
    }

    public void orderSubjectPredicateObject() throws Exception {
        LIDIndexScan<Void> subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
        LID subjectID = subjectIndexScan.get_next_rid();
        int subjectC= 0;
        while (subjectID != null) {
            subjectC++;
            LIDIndexScan<Void> predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
            LID predicateID = predicateIndexScan.get_next_rid();
            int predicateC=0;
            while (predicateID != null) {
                predicateC++;
                LIDIndexScan<Void> objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
                LID objectID = objectIndexScan.get_next_rid();
                int objectC=0;
                while (objectID != null) {
                    objectC++;
                    queryInQIDBTreeFile(subjectID, predicateID, objectID);
                    System.out.println("ObjectCounter: "+objectC);
                    objectID = objectIndexScan.get_next_rid();
                }
                System.out.println("PredicateCounter: "+predicateC);
                predicateID = predicateIndexScan.get_next_rid();
            }
            System.out.println("SubjectCounter: "+subjectC);
            subjectID = subjectIndexScan.get_next_rid();
        }

//        while (subjectID != null) {
//            subjectC++;
//            subjectID = subjectIndexScan.get_next_rid();
//        }
        System.out.println(subjectC);

    }

    private void queryInQIDBTreeFile(LID subjectID, LID predicateID, LID objectID) throws Exception {
        int indexOption = rdfDB.getIndexType();
        KeyClass filter = null;
        List<KeyClassManager> list;
        IDListKeyClassManager idListKeyClassManager;
        StringKey key;
        switch (indexOption) {
            case 1:
                list = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                idListKeyClassManager = new IDListKeyClassManager(list, 20, 10);
                key = (StringKey) idListKeyClassManager.getKeyClass(Arrays.asList(subjectID, predicateID, objectID));
                filter = key;
                break;
            case 2:
                list = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
                idListKeyClassManager = new IDListKeyClassManager(list, 20, 10);
                key = (StringKey) idListKeyClassManager.getKeyClass(Arrays.asList(objectID, subjectID));
                filter = key;
                break;
        }

        QIDIndexScan<List<?>> qidIndexScan = initializeQIDScan(filter);
        getQuadruplesFromScanAndHeapFile(qidIndexScan);
    }

    private void getQuadruplesFromScanAndHeapFile(QIDIndexScan<List<?>> qidIndexScan) throws Exception {
        Quadruple q = qidIndexScan.get_next();
        while (q != null) {
            String predicateLabel = rdfDB.getPredicateLabelHeapFile().getRecord(q.getPredicate().returnLid()).getLabel();
            String objectLabel = rdfDB.getEntityLabelHeapFile().getRecord(q.getObject().returnLid()).getLabel();
            String subjectLabel = rdfDB.getEntityLabelHeapFile().getRecord(q.getSubject().returnLid()).getLabel();

            if (subjectFilter != null && subjectFilter.equals(subjectLabel) && objectFilter != null
                    && objectFilter.equals(objectLabel) && predicateFilter != null && predicateFilter.equals(predicateLabel)) {
                System.out.println(q);
            }
            q = qidIndexScan.get_next();
        }
    }

    private QIDIndexScan<List<?>> initializeQIDScan(KeyClass filter) throws IndexException, InvalidTupleSizeException, IOException, UnknownIndexTypeException, InvalidTypeException {
        RelSpec rel = new RelSpec(RelSpec.outer);
        FldSpec[] projlist2 = new FldSpec[Quadruple.numberOfFields];

        for (int i = 0; i < projlist2.length; i++)
            projlist2[i] = new FldSpec(rel, i + 1);

        CondExpr[] expr = new CondExpr[2];
        expr[0] = new CondExpr();
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
        expr[0].operand2.string = ((StringKey) filter).getKey();
        expr[0].next = null;
        expr[1] = null;

        IndexType indexType = new IndexType(IndexType.B_Index);
        QIDIndexScan<List<?>> qidScan = new QIDIndexScan<List<?>>(indexType, RDFDB.quadrupleHeapFileName, RDFDB.qidBTreeFileName,
                Quadruple.headerTypes, Quadruple.strSizes, 7, 7, projlist2, null, 1, false, filter, rdfDB.getIndexType()) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                List<KeyClassManager> keyClassManagers = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(),
                        LIDKeyClassManager.getSupplier());
                return new IDListKeyClassManager(keyClassManagers, 20, 10);
            }
        };
        return qidScan;
    }

    public void orderPredicateSubjectObject() {

    }

    public void orderSubject() {

    }

    public void orderPredicate() {

    }

    public void orderObject() {

    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        quadrupleSort.close();
    }
}

