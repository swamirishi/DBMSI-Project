package qiterator;

import diskmgr.RDFDB;
import global.*;
import heap.*;
import quadrupleheap.Quadruple;

import java.io.IOException;

import static tests.RDFDBTest.quadrupleInitTest;

public class QuadrupleUtilsTest {
    static Quadruple q1;
    static Quadruple q2;

    String dbpath = "file";
    SystemDefs sysdef = new SystemDefs(dbpath, 8193, 100, "Clock");
    public static RDFDB rdfdb = new RDFDB(0);

    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
    public static boolean assertEquals(double reportedValue, double expectedValue){
        return (reportedValue == expectedValue);
    }

    private static void init() throws FieldNumberOutOfBoundException, IOException, SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, ClassNotFoundException, InvalidTypeException {
        EID subjectID = rdfdb.insertEntity("Dhruv");
        EID objectID = rdfdb.insertEntity("Agja");
        PID predicateID = rdfdb.insertPredicate("OP");
        Quadruple q1 = quadrupleInitTest(subjectID, objectID, predicateID, 1.0f);
        rdfdb.insertQuadruple(q1.getQuadrupleByteArray());

        subjectID = rdfdb.insertEntity("Abhi");
        objectID = rdfdb.insertEntity("Jindal");
        predicateID = rdfdb.insertPredicate("Pro");
        Quadruple q2 = quadrupleInitTest(subjectID, objectID, predicateID, 2.0f);
        rdfdb.insertQuadruple(q2.getQuadrupleByteArray());
    }

    public static void main(String[] args) throws Exception {
        init();
        if(QuadrupleUtilsTest.checkQuadrupleCompare()) System.out.println("TEST 1 status: PASSED");
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
