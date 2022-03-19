package quadrupleheap;

import global.*;
import heap.*;


import java.io.*;

public class QuadrupleTest {

    static Quadruple q;

    public static void main(String[] args) throws FieldNumberOutOfBoundException, IOException, ClassNotFoundException, InvalidTupleSizeException, InvalidTypeException {
        System.out.println("Running test 1: quadrupleInitTest");
        init();
        if(QuadrupleTest.quadrupleInitTest()) System.out.println("TEST 1 status: PASSED");
        if(QuadrupleTest.checkQuadrupleConstructor()) System.out.println("TEST 1 status: PASSED");
        System.out.println("### END OF TESTS ###");
    }

    private static void init() throws FieldNumberOutOfBoundException, IOException, InvalidTupleSizeException, InvalidTypeException {
        EID subject = new EID(new PageId(1), -2);
        PID predicate = new PID(new PageId(2), 3);
        EID object = new EID(new PageId(10), 21);
        float value = 1.2f;
        q = new Quadruple(subject, predicate, object, value);
    }

    private static boolean checkQuadrupleConstructor() {
        Quadruple tmp = new Quadruple(q.getData(), 0, q.getData().length);
        if(!assertEquals(tmp.getSubject().slotNo, q.getSubject().slotNo)) return false;
        if(!assertEquals(tmp.getSubject().pageNo.pid, q.getSubject().pageNo.pid)) return false;
        if(!assertEquals(tmp.getValue(), q.getValue())) return false;
        return true;
    }

    public static boolean quadrupleInitTest() throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException {

        if(!assertEquals(q.getIntFld(1), 1)) return false;
        if(!assertEquals(q.getIntFld(2), -2)) return false;
        if(!assertEquals(q.getFloFld(7), 1.2f)) return false;
        return true;
    }

    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
    public static boolean assertEquals(double reportedValue, double expectedValue){
        return (reportedValue == expectedValue);
    }
}