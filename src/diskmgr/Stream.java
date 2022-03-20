package diskmgr;

import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import global.*;
import heap.*;
import iterator.SortException;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleSort;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.THFPage;
import quadrupleheap.TScan;

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
    }


    public Quadruple getNext() throws Exception {
        LIDBTreeFile<Void> subjectBtreeIndexFile = rdfDB.getSubjectBtreeIndexFile();
        LIDBTreeFile<Void> predicateBtreeIndexFile = rdfDB.getPredicateBtreeIndexFile();
        LIDBTreeFile<Void> objectBtreeIndexFile = rdfDB.getObjectBtreeIndexFile();
        QIDBTreeFile<List<?>> qidBtreeFile = rdfDB.getQidBtreeFile();
        queryInLabelBTreeFile(subjectBtreeIndexFile);
        return quadrupleSort.get_next();
    }

    private void queryInLabelBTreeFile(LIDBTreeFile<Void> labelBtreeIndexFile) {
    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        quadrupleSort.close();
    }
}

