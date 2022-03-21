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

    public static Quadruple quadrupleInitTest(EID subjectID, EID objectID, PID predicateID, float value) throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException, InvalidTupleSizeException, InvalidTypeException {
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
        EID subjectID = rdfdb.insertEntity("Dhruv", true);
        EID objectID = rdfdb.insertEntity("Agja", false);
        PID predicateID = rdfdb.insertPredicate("OP");
        Quadruple q1 = quadrupleInitTest(subjectID, objectID, predicateID, 1.0f);
        rdfdb.insertQuadruple(q1.getQuadrupleByteArray());

        subjectID = rdfdb.insertEntity("Abhi", true);
        objectID = rdfdb.insertEntity("Jindal", false);
        predicateID = rdfdb.insertPredicate("Pro");
        Quadruple q2 = quadrupleInitTest(subjectID, objectID, predicateID, 2.0f);
        rdfdb.insertQuadruple(q2.getQuadrupleByteArray());

//        Stream s = rdfdb.openStream(1, "Abhi", null, null, 0);
//        System.out.println(rdfdb.getEntityLabelHeapFile().getRecord(s.getNext().getSubject().returnLid()).getLabel());
//        System.out.println(rdfdb.getEntityLabelHeapFile().getRecord(s.getNext().getSubject().returnLid()).getLabel());

        int reportedValue = QuadrupleUtils.CompareQuadrupleWithQuadruple(new AttrType(attrLID), q1, 1, q1, 2);
        TScan tScan = new TScan(rdfdb.getQuadrupleHeapFile());
        TupleOrder tupleOrders = new TupleOrder(0);
        AttrType attrType = new AttrType(attrLID);
        AttrType[] attrTypes = {attrType, attrType, attrType, attrType};
        QuadrupleSort quadrupleSort = new QuadrupleSort(rdfdb, 5, attrTypes, (short) 4, new short[4], tScan, 3, tupleOrders , 31, 10);
        Quadruple q = quadrupleSort.get_next();
        while(q!=null){
            System.out.println("OK");
            System.out.println(q);
            q = quadrupleSort.get_next();
        }


    }
}
