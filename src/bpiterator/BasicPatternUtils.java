package bpiterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.AttrType;
import global.LID;
import global.NID;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.Tuple;
import iterator.UnknowAttrType;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;

import java.io.IOException;
import java.util.HashMap;

import static qiterator.QuadrupleUtils.CompareQuadrupleWithQuadruple;

/**
 * some useful method when processing Quadruple
 */
public class BasicPatternUtils {
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

	public static int CompareBPWithBP(AttrType fldType,
									  Tuple t1, int t1_fld_no,
									  Tuple t2, int t2_fld_no) throws Exception {
		double t1_r, t2_r;
		boolean status = OK;

		NID nid_bp1 = ((BasicPattern)t1).getNode(t1_fld_no);
		NID nid_bp2 = ((BasicPattern)t2).getNode(t2_fld_no);

		LID lid_bp1 = nid_bp1.returnLid();
		LID lid_bp2 = nid_bp2.returnLid();

        /*
            Following maps store attribute no with its LID object. Ex : (1, subjectLid) (2, predicateLid)
        * */

		LabelHeapFile labelHeapFileQ1 = rdfdb.getEntityLabelHeapFile();
		LabelHeapFile labelHeapFileQ2 = rdfdb.getEntityLabelHeapFile();

		Label labelQ1 = new Label();
		Label labelQ2 = new Label();

		switch (fldType.attrType) {

			case AttrType.attrLID:


					try {
						labelQ1 = labelHeapFileQ1.getRecord(lid_bp1);
					}
					catch (Exception e){
						e.printStackTrace();
					}
					try {
						labelQ2 = labelHeapFileQ2.getRecord(lid_bp2);
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
				t1_r = ((BasicPattern)t1).getValue();
				t2_r = ((BasicPattern)t2).getValue();
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
	public static int CompareBPWithValue(AttrType fldType,
										 Tuple t1, int t1_fld_no,
										 Tuple value)
			throws Exception {
		return CompareBPWithBP(fldType, t1, t1_fld_no, value, t1_fld_no);
	}

//	public static int compareQuadrupleWithQuadrupleAsPerOrderType(Quadruple q1, Quadruple q2, int orderType) throws Exception {
//		int res;
//		switch (orderType){
//
//			case 1:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
//				if(res == 0){
//					res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
//					if(res == 0){
//						res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
//						if(res == 0){
//							res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//						}
//					}
//				}
//				return res;
//
//
//			case 2:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
//				if(res == 0){
//					res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
//					if(res == 0){
//						res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
//						if(res == 0){
//							res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//						}
//					}
//				}
//				return res;
//
//
//			case 3:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 1, q2, 1);
//				if(res == 0){
//					res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//				}
//				return res;
//
//
//			case 4:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 2, q2, 2);
//				if(res == 0){
//					res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//				}
//				return res;
//
//
//
//			case 5:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrLID), q1, 3, q2, 3);
//				if(res == 0){
//					res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//				}
//				return res;
//
//
//			case 6:
//				res = CompareQuadrupleWithQuadruple(new AttrType(AttrType.attrReal), q1, 4, q2, 4);
//				return res;
//
//		}
//		return Integer.MIN_VALUE;
//	}

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
	public static String getLabelStringFromBPUsingId(BasicPattern basicPattern, int fldno)
			throws IOException,
            QuadrupleUtilsException, FieldNumberOutOfBoundException {
		LID lid_nid1 = basicPattern.getNode(fldno).returnLid();
		String result = "null";

		try{
			result = rdfdb.getEntityLabelHeapFile().getRecord(lid_nid1).getLabel();
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
