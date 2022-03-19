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

import static global.AttrType.attrLID;
import static global.AttrType.attrReal;
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
        TScan tScan = new TScan(rdfDB.getQuadrupleHeapFile());
        TupleOrder tupleOrders = new TupleOrder(0);
        AttrType attrTypeLID = new AttrType(attrLID);
        AttrType attrTypeFloat = new AttrType(attrReal);
        AttrType[] attrTypes = {attrTypeLID, attrTypeLID, attrTypeLID, attrTypeFloat};
        QuadrupleUtils.rdfdb = rdfdb;
        quadrupleSort = new QuadrupleSort(rdfDB, orderType, attrTypes, (short) 4, new short[4], tScan, 3, tupleOrders, 31, 1024);
        subjectFilter = subjectFil;
        predicateFilter = predicateFil;
        objectFilter = objectFil;
        confidenceFilter = confidenceFil == null ? 0 : confidenceFil.floatValue();
    }


    public Quadruple getNext() throws Exception {
        Quadruple currQuadruple = quadrupleSort.get_next();
        if(currQuadruple==null){
            return null;
        }
        LabelHeapFile entityLabelHeapFile = rdfDB.getEntityLabelHeapFile();
        LabelHeapFile predicateLabelHeapFile = rdfDB.getPredicateLabelHeapFile();

        LID currSubjectID = currQuadruple.getSubject().returnLid();
        LID currObjectID = currQuadruple.getObject().returnLid();
        LID currPredicateID = currQuadruple.getPredicate().returnLid();

        //filters
        //ignoring ordertype

        //subject filter
        if (subjectFilter != null) {
            if (!entityLabelHeapFile.getRecord(currSubjectID).getLabel().equals(subjectFilter)) {
                return getNext();
            }
        }
        //predicate filter
        if (predicateFilter != null) {
            if (!predicateLabelHeapFile.getRecord(currPredicateID).getLabel().equals(predicateFilter)) {
                return getNext();
            }
        }
        //object filter
        if (objectFilter != null) {
            if (!entityLabelHeapFile.getRecord(currObjectID).getLabel().equals(objectFilter)) {
                return getNext();
            }
        }

        //confidence filter
        if (confidenceFilter != 0) {
            if (currQuadruple.getValue() < confidenceFilter) {
                return getNext();
            }
        }

        return currQuadruple;
    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException, SortException, IOException {
        quadrupleSort.close();
    }
}

