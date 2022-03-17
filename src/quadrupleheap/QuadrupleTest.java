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
        EID subject = new EID(new PageId(1), -2);
        PID predicate = new PID(new PageId(2), 3);
        EID object = new EID(new PageId(10), 21);
        double value = 1.2;

        Quadruple q = new Quadruple(subject, predicate, object, value);
        System.out.println(q.getDoubleFld(7));
        if(!assertEquals(q.getIntFld(1), 1)) return false;
        if(!assertEquals(q.getIntFld(2), -2)) return false;
        if(!assertEquals(q.getDoubleFld(7), 1.2)) return false;
        return true;
    }

    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
    public static boolean assertEquals(double reportedValue, double expectedValue){
        return (reportedValue == expectedValue);
    }
}