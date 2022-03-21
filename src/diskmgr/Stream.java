package diskmgr;

import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.*;
import heap.*;
import index.IndexException;
import index.UnknownIndexTypeException;
import index.label.LIDIndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.SortException;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleSort;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.THFPage;
import quadrupleheap.TScan;
import utils.supplier.keyclass.KeyClassManager;

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

    LIDIndexScan<Void> subjectIndexScan;
    LIDIndexScan<Void> predicateIndexScan;
    LIDIndexScan<Void> objectIndexScan;

    public static final int maxLabelLen = 150;

    public Stream(RDFDB rdfdb, int orderType, String subjectFil, String predicateFil,
                  String objectFil, Double confidenceFil) throws Exception {
        rdfDB = rdfdb;
        QuadrupleUtils.rdfdb = rdfdb;

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

        subjectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.subjectBTreeFileName, subjectFilter);
        predicateIndexScan = initializeLabelScan(RDFDB.predicateLabelFileName, RDFDB.predicateBTreeFileName, predicateFilter);
        objectIndexScan = initializeLabelScan(RDFDB.entityLabelFileName, RDFDB.objectBTreeFileName, objectFilter);
    }


    public Quadruple getNext() throws Exception {
        return quadrupleSort.get_next();
    }

    private void queryInIndexFiles() {
        int indexOption = rdfDB.getIndexType();
        switch (indexOption){

        }
    }
//
//    private void queryInLabelBTreeFile(LIDBTreeFile<Void> labelBtreeIndexFile, ) {
//    }

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
        expr[1]=null;

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

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        quadrupleSort.close();
    }
}

