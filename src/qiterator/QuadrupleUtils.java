package qiterator;
import diskmgr.RDFDB;
import heap.*;
import global.*;
import iterator.*;
import labelheap.*;

import quadrupleheap.*;

import java.io.*;
import java.lang.*;
import java.util.HashMap;

/**
 * some useful method when processing Quadruple
 */
public class QuadrupleUtils {
    private final static boolean OK = true;
    private final static boolean FAIL = false;

    private final static int SUBJECT_PID_FLD_NO = 1;
    private final static int SUBJECT_SLOT_FLD_NO = 2;
    private final static int PREDICATE_PID_FLD_NO = 3;
    private final static int PREDICATE_SLOT_FLD_NO = 4;
    private final static int OBJECT_PID_FLD_NO = 5;
    private final static int OBJECT_SLOT_FLD_NO = 6;
    private final static int VALUE_FLD_NO = 7;
    public static RDFDB rdfdb;
    private static void populateHashmap(HashMap<Integer, LID> map, LID lid_subject, LID lid_predicate, LID lid_object) {
        map.put(1, lid_subject);
        map.put(2, lid_predicate);
        map.put(3, lid_object);
    }

    /**
     * This function compares a Quadruple with another Quadruple in respective field, and
     * returns:
     * <p>
     * 0        if the two are equal,
     * 1        if the Quadruple is greater,
     * -1        if the Quadruple is smaller,
     *
     * @param fldType   the type of the field being compared.
     * @param t1        one Quadruple.
     * @param t2        another Quadruple.
     * @param t1_fld_no the field numbers in the Quadruples to be compared.
     * @param t2_fld_no the field numbers in the Quadruples to be compared.
     * @return 0        if the two are equal,
     * 1        if the Quadruple is greater,
     * -1        if the Quadruple is smaller,
     * @throws UnknowAttrType          don't know the attribute type
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */

    public static int CompareQuadrupleWithQuadruple(AttrType fldType,
                                                    Quadruple t1, int t1_fld_no,
                                                    Quadruple t2, int t2_fld_no)
			throws IOException,
			UnknowAttrType,
			QuadrupleUtilsException, HFDiskMgrException, HFException, HFBufMgrException, FieldNumberOutOfBoundException {
        double t1_r, t2_r;
        boolean status = OK;


        LID lid_subject1 = t1.getGenericObjectFromByteArray(SUBJECT_PID_FLD_NO, SUBJECT_SLOT_FLD_NO);
        LID lid_predicate1 = t1.getGenericObjectFromByteArray(PREDICATE_PID_FLD_NO, PREDICATE_SLOT_FLD_NO);
        LID lid_object1 = t1.getGenericObjectFromByteArray(OBJECT_PID_FLD_NO, OBJECT_SLOT_FLD_NO);

        LID lid_subject2 = t2.getGenericObjectFromByteArray(SUBJECT_PID_FLD_NO, SUBJECT_SLOT_FLD_NO);
        LID lid_predicate2 = t2.getGenericObjectFromByteArray(PREDICATE_PID_FLD_NO, PREDICATE_SLOT_FLD_NO);
        LID lid_object2 = t2.getGenericObjectFromByteArray(OBJECT_PID_FLD_NO, OBJECT_SLOT_FLD_NO);
        /*
            Following maps store attribute no with its LID object. Ex : (1, subjectLid) (2, predicateLid)
        * */
        HashMap<Integer, LID> t1_map = new HashMap<>();
        HashMap<Integer, LID> t2_map = new HashMap<>();
        populateHashmap(t1_map, lid_subject1, lid_predicate1, lid_object1);
        populateHashmap(t2_map, lid_subject2, lid_predicate2, lid_object2);

		LabelHeapFile f = null;
        switch (fldType.attrType) {

            case AttrType.attrLID:

                if (t1_fld_no == t2_fld_no && t1_fld_no >= 1 && t1_fld_no <= 3) {

                        f = new LabelHeapFile("quadrupleHeapFile");

                        LScan scan1 = null;
                        LScan scan2 = null;

                        if (status == OK) {
                            System.out.println("  - Scan the file \n");

                            try {
                                scan1 = f.openScan();
                                scan2 = f.openScan();

                            } catch (Exception e) {
                                status = FAIL;
                                System.err.println("*** Error opening scan\n");
                                e.printStackTrace();
                            }


                        }

                        if (status == OK) {
                            int len, i = 0;

                            Label label1, label2;

                            boolean done1 = false;
                            boolean done2 = false;

                            while (!done1) {
                                try {
                                    label1 = scan1.getNext(t1_map.get(t1_fld_no));
                                    String data1 = label1.getLabel();

                                    while (!done2) {
                                        try {
                                            label2 = scan2.getNext(t2_map.get(t2_fld_no));
                                            String data2 = label2.getLabel();
                                            return data1.compareTo(data2);
                                        } catch (Exception e) {
                                            status = FAIL;
                                            e.printStackTrace();
                                        }


                                    }


                                } catch (Exception e) {
                                    status = FAIL;
                                    e.printStackTrace();
                                }


                            }
                        }

                    }
            case AttrType.attrReal:                // Compare two floats
                try {
                    t1_r = t1.getDoubleFld(t1_fld_no);
                    t2_r = t2.getDoubleFld(t2_fld_no);
                } catch (FieldNumberOutOfBoundException e) {
                    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
                }
                if (t1_r == t2_r) return 0;
                else if (t1_r < t2_r) return -1;
                return 1;
        }
        return Integer.MIN_VALUE;
    }




    /**
     * This function  compares  Quadruple1 with another Quadruple2 whose
     * field number is same as the Quadruple1
     *
     * @param fldType   the type of the field being compared.
     * @param t1        one Quadruple
     * @param value     another Quadruple.
     * @param t1_fld_no the field numbers in the Quadruples to be compared.
     * @return 0        if the two are equal,
     * 1        if the Quadruple is greater,
     * -1        if the Quadruple is smaller,
     * @throws UnknowAttrType          don't know the attribute type
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */
    public static int CompareQuadrupleWithValue(AttrType fldType,
                                                Quadruple t1, int t1_fld_no,
                                                Quadruple value)
			throws IOException,
			UnknowAttrType,
			QuadrupleUtilsException, FieldNumberOutOfBoundException, HFDiskMgrException, HFException, HFBufMgrException {
        return CompareQuadrupleWithQuadruple(fldType, t1, t1_fld_no, value, t1_fld_no);
    }

    public static int compareQuadrupleWithQuadrupleAsPerOrderType(Quadruple q1, Quadruple q2, int orderType) throws UnknowAttrType, FieldNumberOutOfBoundException, QuadrupleUtilsException, HFDiskMgrException, HFException, HFBufMgrException, IOException {
        int res;
        switch (orderType){

            case 1:
                res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
                if(res == 0){
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
                    if(res == 0){
                        res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
                        if(res == 0){
                            res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                        }
                    }
                }
                return res;


            case 2:
                res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
                if(res == 0){
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
                    if(res == 0){
                        res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
                        if(res == 0){
                            res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                        }
                    }
                }
                return res;


            case 3:
                res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
                if(res == 0){
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                }
                return res;


            case 4:
                res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
                if(res == 0){
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                }
                return res;



            case 5:
                res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
                if(res == 0){
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                }
                return res;


            case 6:
                    res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
                return res;

        }
        return Integer.MIN_VALUE;
    }

    /**
     * This function Compares two Quadruple inn all fields
     *
     * @param t1     the first Quadruple
     * @param t2     the secocnd Quadruple
     * @return 0        if the two are not equal,
     * 1        if the two are equal,
     * @throws UnknowAttrType          don't know the attribute type
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */

    public static boolean Equal(Quadruple t1, Quadruple t2)
            throws IOException, UnknowAttrType, QuadrupleUtilsException, FieldNumberOutOfBoundException, HFDiskMgrException, HFException, HFBufMgrException
    {
        int i;

        int val1=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,1,t2,1);
        int val2=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,2,t2,2);
        int val3=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,3,t2,3);
        int val4=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal),t1,4,t2,4);

        if(val1 == 0 && val2 == 0 && val3 == 0 && val4 == 0){
            return true;
        }
        return false;
    }


    /**
     * get the string specified by the field number
     * Field number is the attribute number i.e subject fldno is 1.
     * @param fldno     the field number
     * @return the content of the field number
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */
    public static String getLabelStringFromQuadrupleUsingId(Quadruple quadruple, int fldno)
            throws IOException,
            QuadrupleUtilsException, FieldNumberOutOfBoundException {
        LID lid_subject = quadruple.getGenericObjectFromByteArray(SUBJECT_PID_FLD_NO, SUBJECT_SLOT_FLD_NO);
        LID lid_predicate = quadruple.getGenericObjectFromByteArray(PREDICATE_PID_FLD_NO, PREDICATE_SLOT_FLD_NO);
        LID lid_object = quadruple.getGenericObjectFromByteArray(OBJECT_PID_FLD_NO, OBJECT_SLOT_FLD_NO);
        /*
            Following maps store attribute no with its LID object. Ex : (1, subjectLid) (2, predicateLid)
        * */
        HashMap<Integer, LID> map = new HashMap<>();
        map.put(1, lid_subject);
        map.put(2, lid_predicate);
        map.put(3, lid_object);
        LID requiredLid = map.get(fldno);

        return "OVER_TO_DHRUV";
    }


    /**
     * set up a Quadruple in specified field from a Quadruple
     *
     * @param value     the Quadruple to be set
     * @param Quadruple the given Quadruple
     * @param fld_no    the field number
     * @param fldType   the Quadruple attr type
     * @throws UnknowAttrType          don't know the attribute type
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */
    public static void SetValue(Quadruple value, Quadruple Quadruple, int fld_no, AttrType fldType)
            throws IOException,
            UnknowAttrType,
            QuadrupleUtilsException {

        switch (fldType.attrType) {
            case AttrType.attrLID:
                try {
                    value.setIntFld(fld_no, Quadruple.getIntFld(fld_no));
                } catch (FieldNumberOutOfBoundException e) {
                    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
                }
                break;
            case AttrType.attrReal:
                try {
                    value.setFloFld(fld_no, Quadruple.getFloFld(fld_no));
                } catch (FieldNumberOutOfBoundException e) {
                    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
                }
                break;
                
            default:
                throw new UnknowAttrType(null, "Don't know how to handle attrSymbol, attrNull");

        }

        return;
    }


    /**
     * set up the JQuadruple's attrtype, string size,field number for using join
     *
     * @param JQuadruple   reference to an actual Quadruple  - no memory has been malloced
     * @param res_attrs    attributes type of result Quadruple
     * @param in1          array of the attributes of the Quadruple (ok)
     * @param len_in1      num of attributes of in1
     * @param in2          array of the attributes of the Quadruple (ok)
     * @param len_in2      num of attributes of in2
     * @param t1_str_sizes shows the length of the string fields in S
     * @param t2_str_sizes shows the length of the string fields in R
     * @param proj_list    shows what input fields go where in the output Quadruple
     * @param nOutFlds     number of outer relation fileds
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     */
    public static short[] setup_op_Quadruple(Quadruple JQuadruple, AttrType[] res_attrs,
                                             AttrType in1[], int len_in1, AttrType in2[],
                                             int len_in2, short t1_str_sizes[],
                                             short t2_str_sizes[],
                                             FldSpec proj_list[], int nOutFlds)
            throws IOException,
            QuadrupleUtilsException {
        short[] sizesT1 = new short[len_in1];
        short[] sizesT2 = new short[len_in2];
        int i, count = 0;

        for (i = 0; i < len_in1; i++)
            if (in1[i].attrType == AttrType.attrString)
                sizesT1[i] = t1_str_sizes[count++];

        for (count = 0, i = 0; i < len_in2; i++)
            if (in2[i].attrType == AttrType.attrString)
                sizesT2[i] = t2_str_sizes[count++];

        int n_strs = 0;
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer)
                res_attrs[i] = new AttrType(in1[proj_list[i].offset - 1].attrType);
            else if (proj_list[i].relation.key == RelSpec.innerRel)
                res_attrs[i] = new AttrType(in2[proj_list[i].offset - 1].attrType);
        }

        // Now construct the res_str_sizes array.
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
                n_strs++;
            else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset - 1].attrType == AttrType.attrString)
                n_strs++;
        }

        short[] res_str_sizes = new short[n_strs];
        count = 0;
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
                res_str_sizes[count++] = sizesT1[proj_list[i].offset - 1];
            else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset - 1].attrType == AttrType.attrString)
                res_str_sizes[count++] = sizesT2[proj_list[i].offset - 1];
        }
        try {
            JQuadruple.setHdr((short) nOutFlds, res_attrs, res_str_sizes);
        } catch (Exception e) {
            throw new QuadrupleUtilsException(e, "setHdr() failed");
        }
        return res_str_sizes;
    }


    /**
     * set up the JQuadruple's attrtype, string size,field number for using project
     *
     * @param JQuadruple   reference to an actual Quadruple  - no memory has been malloced
     * @param res_attrs    attributes type of result Quadruple
     * @param in1          array of the attributes of the Quadruple (ok)
     * @param len_in1      num of attributes of in1
     * @param t1_str_sizes shows the length of the string fields in S
     * @param proj_list    shows what input fields go where in the output Quadruple
     * @param nOutFlds     number of outer relation fileds
     * @throws IOException             some I/O fault
     * @throws QuadrupleUtilsException exception from this class
     * @throws InvalidRelation         invalid relation
     */

    public static short[] setup_op_Quadruple(Quadruple JQuadruple, AttrType res_attrs[],
                                             AttrType in1[], int len_in1,
                                             short t1_str_sizes[],
                                             FldSpec proj_list[], int nOutFlds)
            throws IOException,
            QuadrupleUtilsException,
            InvalidRelation {
        short[] sizesT1 = new short[len_in1];
        int i, count = 0;

        for (i = 0; i < len_in1; i++)
            if (in1[i].attrType == AttrType.attrString)
                sizesT1[i] = t1_str_sizes[count++];

        int n_strs = 0;
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer)
                res_attrs[i] = new AttrType(in1[proj_list[i].offset - 1].attrType);

            else throw new InvalidRelation("Invalid relation -innerRel");
        }

        // Now construct the res_str_sizes array.
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer
                    && in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
                n_strs++;
        }

        short[] res_str_sizes = new short[n_strs];
        count = 0;
        for (i = 0; i < nOutFlds; i++) {
            if (proj_list[i].relation.key == RelSpec.outer
                    && in1[proj_list[i].offset - 1].attrType == AttrType.attrString)
                res_str_sizes[count++] = sizesT1[proj_list[i].offset - 1];
        }

        try {
            JQuadruple.setHdr((short) nOutFlds, res_attrs, res_str_sizes);
        } catch (Exception e) {
            throw new QuadrupleUtilsException(e, "setHdr() failed");
        }
        return res_str_sizes;
    }
}
