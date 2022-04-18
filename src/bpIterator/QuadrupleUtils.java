package bpIterator;

import diskmgr.RDFDB;
import global.AttrType;
import global.LID;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import iterator.UnknowAttrType;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;

import java.io.IOException;
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
													Quadruple t2, int t2_fld_no) throws Exception {
		double t1_r, t2_r;
		boolean status = OK;

		LID lid_subject1 = t1.getSubject().returnLid();
		LID lid_predicate1 = t1.getPredicate().returnLid();
		LID lid_object1 = t1.getObject().returnLid();

		LID lid_subject2 = t2.getSubject().returnLid();
		LID lid_predicate2 = t2.getPredicate().returnLid();
		LID lid_object2 = t2.getObject().returnLid();

        /*
            Following maps store attribute no with its LID object. Ex : (1, subjectLid) (2, predicateLid)
        * */
		HashMap<Integer, LID> t1_map = new HashMap<>();
		HashMap<Integer, LID> t2_map = new HashMap<>();
		populateHashmap(t1_map, lid_subject1, lid_predicate1, lid_object1);
		populateHashmap(t2_map, lid_subject2, lid_predicate2, lid_object2);

		LabelHeapFile labelHeapFileQ1 = getHeapFileUsingField(t1_fld_no);
		LabelHeapFile labelHeapFileQ2 = getHeapFileUsingField(t2_fld_no);
		Label labelQ1 = new Label();
		Label labelQ2 = new Label();

		switch (fldType.attrType) {

			case AttrType.attrLID:


					try {
						labelQ1 = labelHeapFileQ1.getRecord(t1_map.get(t1_fld_no));
					}
					catch (Exception e){
						e.printStackTrace();
					}
					try {
						labelQ2 = labelHeapFileQ2.getRecord(t2_map.get(t2_fld_no));
					}
					catch (Exception e){
						e.printStackTrace();
					}
					if (labelQ1 == null && labelQ2 == null) {
						return 0;
					} else if (labelQ1 == null) {
						return -1;
					} else if (labelQ2 == null) {
						return 1;
					}
					int res = labelQ1.getLabel().compareTo(labelQ2.getLabel());
					if(res == 0)
						return res;
					if(res > 0)
						return 1;
					if(res < 0)
						return -1;


			case AttrType.attrReal:                // Compare two floats
				t1_r = t1.getValue();
				t2_r = t2.getValue();
				if (t1_r == t2_r) return 0;
				else if (t1_r < t2_r) return -1;
				return 1;
		}
		return Integer.MIN_VALUE;
	}

	private static LabelHeapFile getHeapFileUsingField(int fld_no) {
		if(fld_no == 1){
			return rdfdb.getEntityLabelHeapFile();
		}
		else if(fld_no == 2){
			return rdfdb.getPredicateLabelHeapFile();
		}
		else if(fld_no == 3){
			return rdfdb.getEntityLabelHeapFile();
		}
		return null;
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
			throws Exception {
		return CompareQuadrupleWithQuadruple(fldType, t1, t1_fld_no, value, t1_fld_no);
	}

	public static int compareQuadrupleWithQuadrupleAsPerOrderType(Quadruple q1, Quadruple q2, int orderType) throws Exception {
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
	 * This function Compares two Quadruple in all fields
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
			throws Exception {
		int i;

		int val1=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,1,t2,1);
		int val2=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,2,t2,2);
		int val3=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID),t1,3,t2,3);
		int val4=CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal),t1,4,t2,4);

		return val1 == 0 && val2 == 0 && val3 == 0 && val4 == 0;
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
		LID lid_subject = quadruple.getSubject().returnLid();
		LID lid_predicate = quadruple.getPredicate().returnLid();
		LID lid_object = quadruple.getObject().returnLid();
        /*
            Following maps store attribute no with its LID object. Ex : (1, subjectLid) (2, predicateLid)
        * */
		LabelHeapFile labelHeapFileQ1 = getHeapFileUsingField(fldno);
		HashMap<Integer, LID> t_map = new HashMap<>();
		populateHashmap(t_map, lid_subject, lid_predicate, lid_object);

		String result = "null";

		try{
			result = labelHeapFileQ1.getRecord(t_map.get(fldno)).getLabel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/**
	 * set up a Quadruple in specified field from a Quadruple
	 *
	 * @param value     the Quadruple to be set
	 * @param quadruple the given Quadruple
	 * @param fld_no    the field number
	 * @param fldType   the Quadruple attr type
	 * @throws UnknowAttrType          don't know the attribute type
	 * @throws IOException             some I/O fault
	 * @throws QuadrupleUtilsException exception from this class
	 */
	public static void SetValue(Quadruple value, Quadruple quadruple, int fld_no, AttrType fldType)
			throws IOException,
			UnknowAttrType,
            QuadrupleUtilsException, InvalidTupleSizeException, FieldNumberOutOfBoundException {
		int len = quadruple.getQuadrupleByteArray().length;
		value.quadrupleSet(quadruple.getQuadrupleByteArray(), 0, len);
	}
}
