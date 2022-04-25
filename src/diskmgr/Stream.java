package diskmgr;

import basicpatternheap.BasicPattern;
import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import btree.StringKey;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import bufmgr.PageNotReadException;
import global.*;
import heap.*;
import index.IndexException;
import index.IndexUtils;
import index.UnknownIndexTypeException;
import index.label.LIDIndexScan;
import index.quadraple.QIDIndexScan;
import iterator.*;
import iterator.interfaces.IteratorI;
import labelheap.Label;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleSort;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.THFPage;
import quadrupleheap.TScan;
import utils.supplier.keyclass.FloatKeyClassManager;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.IOException;
import java.util.*;

import static global.AttrType.*;
import static global.GlobalConst.INVALID_PAGE;

public class Stream extends IteratorI<BasicPattern> {
    public static QuadrupleSort quadrupleSort;
    public static RDFDB rdfDB;
    public static String subjectFilter;
    public static String predicateFilter;
    public static String objectFilter;
    public static Float confidenceFilter;

//    LIDIndexScan<Void> subjectIndexScan;
//    LIDIndexScan<Void> predicateIndexScan;
//    LIDIndexScan<Void> objectIndexScan;

    public static final int maxLabelLen = 200;
    public int orderType;
    public TScan tScan;
    public QID qid = new QID();

    public Stream(RDFDB rdfdb, int orderType, String subjectFil, String predicateFil,
                  String objectFil, Double confidenceFil) throws Exception {
        rdfDB = rdfdb;
        QuadrupleUtils.rdfdb = rdfdb;
        this.orderType = orderType;

        tScan = new TScan(rdfDB.getQuadrupleHeapFile());
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

//        quadrupleSort = new QuadrupleSort(rdfDB, orderType, attrTypes, len, strSizes,
//                tScan, 3, tupleOrders, 31, 250);

//        subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
//        predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
//        objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
    }


    public Quadruple getNext() throws Exception {
//        return null;
        int option = rdfDB.getIndexType();
        if (option == 6 || option != orderType) {
            return tScan.getNext(qid);
        } else {
            switch (option) {
                case 1:
                    orderSubjectPredicateObject();
                    break;
                case 2:
                    orderPredicateSubjectObject();
                    break;
                case 3:
                    orderSubject();
                    break;
                case 4:
                    orderPredicate();
                    break;
                case 5:
                    orderObject();
                    break;
            }
            return null;
        }
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

        if (filter == null) {
            expr = null;
        }
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
        while (subjectID != null) {
            LIDIndexScan<Void> predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
            LID predicateID = predicateIndexScan.get_next_rid();
            while (predicateID != null) {
                LIDIndexScan<Void> objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
                LID objectID = objectIndexScan.get_next_rid();
                int objectC = 0;
                while (objectID != null) {
                    objectC++;
                    queryInQIDBTreeFile(subjectID, predicateID, objectID, Optional.empty());
                    objectID = objectIndexScan.get_next_rid();
                }
                predicateID = predicateIndexScan.get_next_rid();
            }
            subjectID = subjectIndexScan.get_next_rid();
        }
        subjectIndexScan.close();
    }

    private void queryInQIDBTreeFile(LID subjectID, LID predicateID, LID objectID, Optional<Float> confidenceValue) throws Exception {
        int indexOption = rdfDB.getIndexType();
        KeyClass filter = null;
        List<KeyClassManager> list = null;
        IDListKeyClassManager idListKeyClassManager;
        StringKey key;
        list = Arrays.asList(LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier(), LIDKeyClassManager.getSupplier());
        idListKeyClassManager = new IDListKeyClassManager(list, 20, 10);
        key = (StringKey) idListKeyClassManager.getKeyClass(Arrays.asList(subjectID, predicateID, objectID));
        filter = key;

        QIDIndexScan<List<?>> qidIndexScan = initializeQIDScan(filter, list);
        getQuadruplesFromScanAndHeapFile(qidIndexScan);
    }

    private void getQuadruplesFromScanAndHeapFile(QIDIndexScan<List<?>> qidIndexScan) throws Exception {
        Quadruple q = qidIndexScan.get_next();
        while (q != null) {
            String predicateLabel = rdfDB.getPredicateLabelHeapFile().getRecord(q.getPredicate().returnLid()).getLabel();
            String objectLabel = rdfDB.getEntityLabelHeapFile().getRecord(q.getObject().returnLid()).getLabel();
            String subjectLabel = rdfDB.getEntityLabelHeapFile().getRecord(q.getSubject().returnLid()).getLabel();
            float confidenceValue = q.getValue();
            if ((subjectFilter == null || subjectFilter.equals(subjectLabel))
                    && (objectFilter == null || objectFilter.equals(objectLabel))
                    && (predicateFilter == null || predicateFilter.equals(predicateLabel))
                    && (confidenceFilter == null || confidenceValue >= confidenceFilter)) {

                getLabelFromHeapFile(q);
            }
            q = qidIndexScan.get_next();
        }
    }

    private void getLabelFromHeapFile(Quadruple q) throws Exception {
        LID subjectID = q.getSubject().returnLid();
        LID predicateID = q.getPredicate().returnLid();
        LID objectID = q.getObject().returnLid();

        Label subjectLabel = rdfDB.getEntityLabelHeapFile().getRecord(subjectID);
        Label predicateLabel = rdfDB.getPredicateLabelHeapFile().getRecord(predicateID);
        Label objectLabel = rdfDB.getEntityLabelHeapFile().getRecord(objectID);
        System.out.println("Record: " + subjectLabel.getLabel() + " " + predicateLabel.getLabel() + " " + objectLabel.getLabel() + " " + q.getValue());
    }

    private QIDIndexScan<List<?>> initializeQIDScan(KeyClass filter, List<KeyClassManager> list) throws IndexException, InvalidTupleSizeException, IOException, UnknownIndexTypeException, InvalidTypeException {
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
        QIDIndexScan<List<?>> qidScan = new QIDIndexScan<List<?>>(indexType, RDFDB.quadrupleHeapFileName, RDFDB.sopQidBtreeFileName,
                Quadruple.headerTypes, Quadruple.strSizes, 7, 7, projlist2, null, 1, false, filter, rdfDB.getIndexType()) {
            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return new IDListKeyClassManager(list, 20, 10);
            }
        };
        return qidScan;
    }

    public void orderPredicateSubjectObject() throws Exception {
        LIDIndexScan<Void> predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
        LID predicateID = predicateIndexScan.get_next_rid();
        while (predicateID != null) {
            LIDIndexScan<Void> subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
            LID subjectID = subjectIndexScan.get_next_rid();
            while (subjectID != null) {
                LIDIndexScan<Void> objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
                LID objectID = objectIndexScan.get_next_rid();
                while (objectID != null) {
                    queryInQIDBTreeFile(subjectID, predicateID, objectID, Optional.empty());
                    objectID = objectIndexScan.get_next_rid();
                }
                subjectID = subjectIndexScan.get_next_rid();
            }
            predicateID = predicateIndexScan.get_next_rid();
        }

//        while (subjectID != null) {
//            subjectC++;
//            subjectID = subjectIndexScan.get_next_rid();
//        }
        predicateIndexScan.close();
    }

    public void orderSubject() throws Exception {
        LIDIndexScan<Void> subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
        LID subjectID = subjectIndexScan.get_next_rid();
        while (subjectID != null) {
            queryInQIDBTreeFile(subjectID, null, null, Optional.empty());
            subjectID = subjectIndexScan.get_next_rid();
        }
        subjectIndexScan.close();
    }

    public void orderPredicate() throws Exception {
        LIDIndexScan<Void> predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
        LID predicateId = predicateIndexScan.get_next_rid();
        while (predicateId != null) {
            queryInQIDBTreeFile(null, predicateId, null, Optional.empty());
            predicateId = predicateIndexScan.get_next_rid();
        }
        predicateIndexScan.close();
    }

    public void orderObject() throws Exception {
        LIDIndexScan<Void> objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
        LID objectID = objectIndexScan.get_next_rid();
        while (objectID != null) {
            queryInQIDBTreeFile(null, null, objectID, Optional.empty());
            objectID = objectIndexScan.get_next_rid();
        }
        objectIndexScan.close();

    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        tScan.closescan();
    }

    @Override
    public BasicPattern get_next() throws IOException, JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, Exception {
        Quadruple quadruple = this.getNext();
        if(quadruple==null){
            return null;
        }
        BasicPattern basicPattern = new BasicPattern();
        basicPattern.setHdr((short) 2);
        basicPattern.setNode(1, quadruple.getSubject().returnNid());
        basicPattern.setNode(2, quadruple.getObject().returnNid());
        basicPattern.setValue(quadruple.getValue());
        return basicPattern;
    }

    @Override
    public void close() throws IOException, JoinsException, SortException, IndexException, HFBufMgrException {
        this.closeStream();
    }
}

