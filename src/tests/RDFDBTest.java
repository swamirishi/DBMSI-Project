package tests;

import diskmgr.*;
import global.*;
import heap.*;
import qiterator.QuadrupleSort;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.TScan;

import java.io.IOException;

import static global.AttrType.attrLID;
import static quadrupleheap.QuadrupleTest.assertEquals;


public class RDFDBTest {

    public static Quadruple quadrupleInitTest(EID subjectID, EID objectID, PID predicateID, double value) throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException {
//        EID subject = new EID(new PageId(1), 2);
//        PID predicate = new PID(new PageId(2), 3);
//        EID object = new EID(new PageId(10), 21);
//        double value = 1.2;

        Quadruple q = new Quadruple(subjectID, predicateID, objectID, value);
//        LID subjectLid = q.getLIDFld(0);
//        if(!assertEquals(subjectLid.getPageNo().pid, 1)) return false;
//        if(!assertEquals(subjectLid.getSlotNo(), 2)) return false;
        return q;
    }


    public static void main(String[] args) throws Exception {
        String dbpath = "quadrupleHeapFile";
        PageId pgid = new PageId();
        pgid.pid = 0;
        SystemDefs sysdef = new SystemDefs(dbpath, 8193, 100, "Clock");
//        SystemDefs.JavabaseDB.add_file_entry("quadrupleHeapFile", pgid);
        RDFDB rdfdb = new RDFDB(0);
        QuadrupleUtils.rdfdb = rdfdb;
        EID subjectID = rdfdb.insertEntity("Dhruv");
        EID objectID = rdfdb.insertEntity("Agja");
        PID predicateID = rdfdb.insertPredicate("OP");
        Quadruple q1 = quadrupleInitTest(subjectID, objectID, predicateID, 1.0);
        rdfdb.insertQuadruple(q1.returnQuadrupleByteArray());

        subjectID = rdfdb.insertEntity("Abhi");
        objectID = rdfdb.insertEntity("Jindal");
        predicateID = rdfdb.insertPredicate("Pro");
        Quadruple q2 = quadrupleInitTest(subjectID, objectID, predicateID, 2.0);
        rdfdb.insertQuadruple(q2.returnQuadrupleByteArray());

//        Stream s = rdfdb.openStream(1, "Abhi", null, null, 0);
//        System.out.println(rdfdb.getEntityLabelHeapFile().getRecord(s.getNext().getSubject().returnLid()).getLabel());
//        System.out.println(rdfdb.getEntityLabelHeapFile().getRecord(s.getNext().getSubject().returnLid()).getLabel());

        int reportedValue = QuadrupleUtils.CompareQuadrupleWithQuadruple(new AttrType(attrLID), q1, 1, q1, 2);
        TScan tScan = new TScan(rdfdb.getQuadrupleHeapFile());
        TupleOrder tupleOrders = new TupleOrder(0);
        AttrType attrType = new AttrType(attrLID);
        AttrType[] attrTypes = {attrType};
        QuadrupleSort quadrupleSort = new QuadrupleSort(attrTypes, (short) 1, new short[4], tScan, 1, tupleOrders , 31, 10);
        quadrupleSort.get_next();

        System.out.println(reportedValue);

    }
}
