package quadrupleheap;

import global.*;
import heap.*;


import java.io.*;

public class QuadrupleTest {

    public static void main(String[] args) throws FieldNumberOutOfBoundException, IOException, ClassNotFoundException {
        System.out.println("Running test 1: quadrupleInitTest");
        if(QuadrupleTest.quadrupleInitTest()) System.out.println("TEST 1 status: PASSED");
        System.out.println("### END OF TESTS ###");
    }

    public static boolean quadrupleInitTest() throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException {
        EID subject = new EID(new PageId(1), 2);
        PID predicate = new PID(new PageId(2), 3);
        EID object = new EID(new PageId(10), 21);
        double value = 1.2;
        Quadruple q = new Quadruple(subject, predicate, object, value);
        LID subjectLid = q.getLIDFld(0);
        if(!assertEquals(subjectLid.getPageNo().pid, 1)) return false;
        if(!assertEquals(subjectLid.getSlotNo(), 2)) return false;
        return true;
    }

    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
}