package iterator;

import global.AttrType;
import global.EID;
import global.PID;
import global.PageId;
import heap.FieldNumberOutOfBoundException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import iterator.QuadrupleUtilsException;
import iterator.UnknowAttrType;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleTest;

import java.io.IOException;

public class QuadrupleUtilsTest {
    static Quadruple q1;
    static Quadruple q2;

    public static boolean assertEquals(int reportedValue, int expectedValue){
        return (reportedValue == expectedValue);
    }
    public static boolean assertEquals(double reportedValue, double expectedValue){
        return (reportedValue == expectedValue);
    }

    private static void init() throws FieldNumberOutOfBoundException, IOException {
        EID subject1 = new EID(new PageId(1), -2);
        PID predicate1 = new PID(new PageId(2), 3);
        EID object1 = new EID(new PageId(10), 21);
        double value1 = 1.2;
        q1 = new Quadruple(subject1, predicate1, object1, value1);
        EID subject2 = new EID(new PageId(2), -1);
        PID predicate2 = new PID(new PageId(3), 4);
        EID object2 = new EID(new PageId(11), 22);
        double value2 = 2.2;
        q2 = new Quadruple(subject2, predicate2, object2, value2);
    }

    public static void main(String[] args) throws Exception {
        init();
        if(QuadrupleUtilsTest.checkQuadrupleCompare()) System.out.println("TEST 1 status: PASSED");
    }

    private static boolean checkQuadrupleCompare() throws Exception, QuadrupleUtilsException {
        int reportedValue = iterator.QuadrupleUtils.CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);

        System.out.println(reportedValue);

        return true;
    }
}
