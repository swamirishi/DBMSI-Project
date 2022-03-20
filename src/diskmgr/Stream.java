package diskmgr;

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
//    public static String subjectFilter;
//    public static String predicateFilter;
//    public static String objectFilter;
//    public static Float confidenceFilter;

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

        quadrupleSort = new QuadrupleSort(rdfDB, orderType, attrTypes, len, strSizes,
                tScan, 3, tupleOrders, 31, 1024);
    }


    public Quadruple getNext() throws Exception {
        return quadrupleSort.get_next();
    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        quadrupleSort.close();
    }
}

