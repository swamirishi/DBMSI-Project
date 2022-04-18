package tests;

import btree.AddFileEntryException;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import qiterator.QuadrupleUtils;
import qiterator.QuadrupleUtilsException;
import qiterator.QuadrupleUtilsTest;
import quadrupleheap.Quadruple;

import java.io.IOException;

import static tests.RDFDBTest.quadrupleInitTest;

public class TestingEntities {
    static Quadruple q1;
    static Quadruple q2;

    String dbpath = "file";
    SystemDefs sysdef = new SystemDefs(dbpath, 8193, 100, "Clock");
    public static RDFDB rdfdb;


    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
    public static boolean assertEquals(double reportedValue, double expectedValue){
        return (reportedValue == expectedValue);
    }

    private static void init() throws Exception {
        rdfdb = new RDFDB();
        rdfdb.setRDFDBProperties(1);
//        EID subjectID = rdfdb.insertEntity("Dhruv");
//        EID subjectID2 = rdfdb.insertEntity("Dhruv");

    }

    public static void main(String[] args) throws Exception {
        init();
//        if(QuadrupleUtilsTest.checkQuadrupleCompare()) System.out.println("TEST 1 status: PASSED");
    }

    private static boolean checkQuadrupleCompare() throws Exception, QuadrupleUtilsException {
        QuadrupleUtils.rdfdb = rdfdb;
        rdfdb.insertQuadruple(q1.getQuadrupleByteArray());
        rdfdb.insertQuadruple(q2.getQuadrupleByteArray());

        int reportedValue = QuadrupleUtils.CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);

        System.out.println(reportedValue);

        return true;
    }
}
