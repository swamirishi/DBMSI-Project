package qiterator;
import quadrupleheap.*;
import global.*;
import java.io.*;
import java.lang.*;



public class QuadrupleUtils
{
  
  /**
   * This function compares a Quadruple with another Quadruple in respective field, and
   *  returns:
   *
   *    0        if the two are equal,
   *    1        if the Quadruple is greater,
   *   -1        if the Quadruple is smaller,
   *
   *@param    fldType   the type of the field being compared.
   *@param    t1        one Quadruple.
   *@param    t2        another Quadruple.
   *@param    t1_fld_no the field numbers in the Quadruples to be compared.
   *@param    t2_fld_no the field numbers in the Quadruples to be compared. 
   *@exception UnknowAttrType don't know the attribute type
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   *@return   0        if the two are equal,
   *          1        if the Quadruple is greater,
   *         -1        if the Quadruple is smaller,                              
   */
  public static int CompareQuadrupleWithQuadruple(AttrType fldType,
					  Quadruple  t1, int t1_fld_no,
					  Quadruple  t2, int t2_fld_no)

    throws IOException,
	   UnknowAttrType,
	   QuadrupleUtilsException
    {

      LID t1_lid =new LID();
      LID t2_lid=new LID();
      int t1_i,  t2_i;
      float t1_r,  t2_r;
      String t1_s, t2_s;
      LabelHeapFile f=null;


      
    switch (fldType.attrType) 
	{

		case AttrType.attrLID:
		

			if(t1_fld_no==1 && t2_fld_no==1){

				f = new Heapfile("file_1");
			

				t1_lid=t1.getLIDFld(t1_fld_no);
				t2_lid=t2.getLIDFld(t2_fld_no);

				LScan scan1 = null;
				LScan scan2 = null;
    
			    if ( status == OK ) {	
			      System.out.println ("  - Scan the file \n");
			      
			      try {
						scan1 = f.openScan();
						scan2 = f.openScan();

			      	}
			      catch (Exception e) {
						status = FAIL;
						System.err.println ("*** Error opening scan\n");
						e.printStackTrace();
			      	}


				}

				if ( status == OK ) {
			      int len, i = 0;
			      
			      Label label1, label2 ;
			      
			      boolean done1 = false;
			      boolean done2 = false;

			      while (!done1) { 
					try {
					  label1 = scan1.getNext(t1_lid);
					  String data1 = label1.getLabel();

					  while (!done2) { 
						try {
						  label2 = scan2.getNext(t2_lid);
						  String data2 = label2.getLabel();


						  if (data1.equals(data2)){
						  	return 1;
						  }
						  else{
						  	return 0;
						  }


						  if (eid2 == null) {
						    done2 = true;
						    break;
						  	}
						}

						catch (Exception e) {
						  status = FAIL;
						  e.printStackTrace();
						}


					   }



					  if (eid1 == null) {
					    done1 = true;
					    break;
					  	}
					}

					catch (Exception e) {
					  status = FAIL;
					  e.printStackTrace();
					}


				   }
				}

			}
			else if(t1_fld_no==2 && t2_fld_no==2){

				f = new Heapfile("file_1");
				

				t1_lid=t1.getLIDFld(t1_fld_no);
				t2_lid=t2.getLIDFld(t2_fld_no);

				LScan scan1 = null;
				LScan scan2 = null;
    
			    if ( status == OK ) {	
			      System.out.println ("  - Scan the file \n");
			      
			      try {
						scan1 = f.openScan();
						scan2 = f.openScan();

			      	}
			      catch (Exception e) {
						status = FAIL;
						System.err.println ("*** Error opening scan\n");
						e.printStackTrace();
			      	}


				}

				if ( status == OK ) {
			      int len, i = 0;
			      
			      Label label1, label2 ;
			      
			      boolean done1 = false;
			      boolean done2 = false;

			      while (!done1) { 
					try {
					  label1 = scan1.getNext(t1_lid);
					  String data1 = label1.getLabel();

					  while (!done2) { 
						try {
						  label2 = scan2.getNext(t2_lid);
						  String data2 = label2.getLabel();


						  if (data1.equals(data2)){
						  	return 1;
						  }
						  else{
						  	return 0;
						  }


						  if (eid2 == null) {
						    done2 = true;
						    break;
						  	}
						}

						catch (Exception e) {
						  status = FAIL;
						  e.printStackTrace();
						}


					   }



					  if (eid1 == null) {
					    done1 = true;
					    break;
					  	}
					}

					catch (Exception e) {
					  status = FAIL;
					  e.printStackTrace();
					}


				   }
				}

			}
			else if(t1_fld_no==3 && t2_fld_no==3){

				f = new Heapfile("file_1");
				

				t1_lid=t1.getLIDFld(t1_fld_no);
				t2_lid=t2.getLIDFld(t2_fld_no);

				LScan scan1 = null;
				LScan scan2 = null;
    
			    if ( status == OK ) {	
			      System.out.println ("  - Scan the file \n");
			      
			      try {
						scan1 = f.openScan();
						scan2 = f.openScan();

			      	}
			      catch (Exception e) {
						status = FAIL;
						System.err.println ("*** Error opening scan\n");
						e.printStackTrace();
			      	}


				}

				if ( status == OK ) {
			      int len, i = 0;
			      
			      Label label1, label2 ;
			      
			      boolean done1 = false;
			      boolean done2 = false;

			      while (!done1) { 
					try {
					  label1 = scan1.getNext(t1_lid);
					  String data1 = label1.getLabel();

					  while (!done2) { 
						try {
						  label2 = scan2.getNext(t2_lid);
						  String data2 = label2.getLabel();


						  if (data1.equals(data2)){
						  	return 1;
						  }
						  else{
						  	return 0;
						  }


						  if (eid2 == null) {
						    done2 = true;
						    break;
						  	}
						}

						catch (Exception e) {
						  status = FAIL;
						  e.printStackTrace();
						}


					   }



					  if (eid1 == null) {
					    done1 = true;
					    break;
					  	}
					}

					catch (Exception e) {
					  status = FAIL;
					  e.printStackTrace();
					}


				   }
				}

			}
			else{
			

				try {
				    int t1_i = t1.getIntFld(t1_fld_no);
				    int t2_i = t2.getIntFld(t2_fld_no);
				  }catch (FieldNumberOutOfBoundException e){
				    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
				  
				  if (t1_i == t2_i) return  0;
				  if (t1_i <  t2_i) return -1;
				  if (t1_i >  t2_i) return  1;								}


				}

			}

		}
		catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }



	case AttrType.attrInteger:                // Compare two integers.
	  try {
	    t1_i = t1.getIntFld(t1_fld_no);
	    t2_i = t2.getIntFld(t2_fld_no);
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  if (t1_i == t2_i) return  0;
	  if (t1_i <  t2_i) return -1;
	  if (t1_i >  t2_i) return  1;
	  
	case AttrType.attrReal:                // Compare two floats
	  try {
	    t1_r = t1.getFloFld(t1_fld_no);
	    t2_r = t2.getFloFld(t2_fld_no);
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  if (t1_r == t2_r) return  0;
	  if (t1_r <  t2_r) return -1;
	  if (t1_r >  t2_r) return  1;
	  
	case AttrType.attrString:                // Compare two strings
	  try {
	    t1_s = t1.getStrFld(t1_fld_no);
	    t2_s = t2.getStrFld(t2_fld_no);
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  
	  // Now handle the special case that is posed by the max_values for strings...
	  if(t1_s.compareTo( t2_s)>0)return 1;
	  if (t1_s.compareTo( t2_s)<0)return -1;
	  return 0;
	default:
	  
	  throw new UnknowAttrType(null, "Don't know how to handle attrSymbol, attrNull");
	  
	}
    }
  
  
  
  /**
   * This function  compares  Quadruple1 with another Quadruple2 whose
   * field number is same as the Quadruple1
   *
   *@param    fldType   the type of the field being compared.
   *@param    t1        one Quadruple
   *@param    value     another Quadruple.
   *@param    t1_fld_no the field numbers in the Quadruples to be compared.  
   *@return   0        if the two are equal,
   *          1        if the Quadruple is greater,
   *         -1        if the Quadruple is smaller,  
   *@exception UnknowAttrType don't know the attribute type   
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class   
   */            
  public static int CompareQuadrupleWithValue(AttrType fldType,
					  Quadruple  t1, int t1_fld_no,
					  Quadruple  value)
    throws IOException,
	   UnknowAttrType,
	   QuadrupleUtilsException
    {
      return CompareQuadrupleWithQuadruple(fldType, t1, t1_fld_no, value, t1_fld_no);
    }
  
  /**
   *This function Compares two Quadruple inn all fields 
   * @param t1 the first Quadruple
   * @param t2 the secocnd Quadruple
   * @param type[] the field types
   * @param len the field numbers
   * @return  0        if the two are not equal,
   *          1        if the two are equal,
   *@exception UnknowAttrType don't know the attribute type
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   */            
  
  public static boolean Equal(Quadruple t1, Quadruple t2, AttrType types[], int len)
    throws IOException,UnknowAttrType,QuadrupleUtilsException
    {
      int i;
      
      for (i = 1; i <= len; i++)
	if (CompareQuadrupleWithQuadruple(types[i-1], t1, i, t2, i) != 0)
	  return false;
      return true;
    }
  
  /**
   *get the string specified by the field number
   *@param Quadruple the Quadruple 
   *@param fidno the field number
   *@return the content of the field number
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   */
  public static String Value(Quadruple  Quadruple, int fldno)
    throws IOException,
	   QuadrupleUtilsException
    {
      String temp;
      try{
	temp = Quadruple.getStrFld(fldno);
      }catch (FieldNumberOutOfBoundException e){
	throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
      }
      return temp;
    }
  
 
  /**
   *set up a Quadruple in specified field from a Quadruple
   *@param value the Quadruple to be set 
   *@param Quadruple the given Quadruple
   *@param fld_no the field number
   *@param fldType the Quadruple attr type
   *@exception UnknowAttrType don't know the attribute type
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   */  
  public static void SetValue(Quadruple value, Quadruple  Quadruple, int fld_no, AttrType fldType)
    throws IOException,
	   UnknowAttrType,
	   QuadrupleUtilsException
    {
      
      switch (fldType.attrType)
	{
	case AttrType.attrInteger:
	  try {
	    value.setIntFld(fld_no, Quadruple.getIntFld(fld_no));
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  break;
	case AttrType.attrReal:
	  try {
	    value.setFloFld(fld_no, Quadruple.getFloFld(fld_no));
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  break;
	case AttrType.attrString:
	  try {
	    value.setStrFld(fld_no, Quadruple.getStrFld(fld_no));
	  }catch (FieldNumberOutOfBoundException e){
	    throw new QuadrupleUtilsException(e, "FieldNumberOutOfBoundException is caught by QuadrupleUtils.java");
	  }
	  break;
	default:
	  throw new UnknowAttrType(null, "Don't know how to handle attrSymbol, attrNull");
	  
	}
      
      return;
    }
  
  
  /**
   *set up the JQuadruple's attrtype, string size,field number for using join
   *@param JQuadruple  reference to an actual Quadruple  - no memory has been malloced
   *@param res_attrs  attributes type of result Quadruple
   *@param in1  array of the attributes of the Quadruple (ok)
   *@param len_in1  num of attributes of in1
   *@param in2  array of the attributes of the Quadruple (ok)
   *@param len_in2  num of attributes of in2
   *@param t1_str_sizes shows the length of the string fields in S
   *@param t2_str_sizes shows the length of the string fields in R
   *@param proj_list shows what input fields go where in the output Quadruple
   *@param nOutFlds number of outer relation fileds
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   */
  public static short[] setup_op_Quadruple(Quadruple JQuadruple, AttrType[] res_attrs,
				       AttrType in1[], int len_in1, AttrType in2[], 
				       int len_in2, short t1_str_sizes[], 
				       short t2_str_sizes[], 
				       FldSpec proj_list[], int nOutFlds)
    throws IOException,
	   QuadrupleUtilsException
    {
      short [] sizesT1 = new short [len_in1];
      short [] sizesT2 = new short [len_in2];
      int i, count = 0;
      
      for (i = 0; i < len_in1; i++)
        if (in1[i].attrType == AttrType.attrString)
	  sizesT1[i] = t1_str_sizes[count++];
      
      for (count = 0, i = 0; i < len_in2; i++)
	if (in2[i].attrType == AttrType.attrString)
	  sizesT2[i] = t2_str_sizes[count++];
      
      int n_strs = 0; 
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer)
	    res_attrs[i] = new AttrType(in1[proj_list[i].offset-1].attrType);
	  else if (proj_list[i].relation.key == RelSpec.innerRel)
	    res_attrs[i] = new AttrType(in2[proj_list[i].offset-1].attrType);
	}
      
      // Now construct the res_str_sizes array.
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset-1].attrType == AttrType.attrString)
            n_strs++;
	  else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset-1].attrType == AttrType.attrString)
            n_strs++;
	}
      
      short[] res_str_sizes = new short [n_strs];
      count         = 0;
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer && in1[proj_list[i].offset-1].attrType ==AttrType.attrString)
            res_str_sizes[count++] = sizesT1[proj_list[i].offset-1];
	  else if (proj_list[i].relation.key == RelSpec.innerRel && in2[proj_list[i].offset-1].attrType ==AttrType.attrString)
            res_str_sizes[count++] = sizesT2[proj_list[i].offset-1];
	}
      try {
	JQuadruple.setHdr((short)nOutFlds, res_attrs, res_str_sizes);
      }catch (Exception e){
	throw new QuadrupleUtilsException(e,"setHdr() failed");
      }
      return res_str_sizes;
    }
  
 
   /**
   *set up the JQuadruple's attrtype, string size,field number for using project
   *@param JQuadruple  reference to an actual Quadruple  - no memory has been malloced
   *@param res_attrs  attributes type of result Quadruple
   *@param in1  array of the attributes of the Quadruple (ok)
   *@param len_in1  num of attributes of in1
   *@param t1_str_sizes shows the length of the string fields in S
   *@param proj_list shows what input fields go where in the output Quadruple
   *@param nOutFlds number of outer relation fileds
   *@exception IOException some I/O fault
   *@exception QuadrupleUtilsException exception from this class
   *@exception InvalidRelation invalid relation 
   */

  public static short[] setup_op_Quadruple(Quadruple JQuadruple, AttrType res_attrs[],
				       AttrType in1[], int len_in1,
				       short t1_str_sizes[], 
				       FldSpec proj_list[], int nOutFlds)
    throws IOException,
	   QuadrupleUtilsException, 
	   InvalidRelation
    {
      short [] sizesT1 = new short [len_in1];
      int i, count = 0;
      
      for (i = 0; i < len_in1; i++)
        if (in1[i].attrType == AttrType.attrString)
	  sizesT1[i] = t1_str_sizes[count++];
      
      int n_strs = 0; 
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer) 
            res_attrs[i] = new AttrType(in1[proj_list[i].offset-1].attrType);
	  
	  else throw new InvalidRelation("Invalid relation -innerRel");
	}
      
      // Now construct the res_str_sizes array.
      for (i = 0; i < nOutFlds; i++)
	{
	  if (proj_list[i].relation.key == RelSpec.outer
	      && in1[proj_list[i].offset-1].attrType == AttrType.attrString)
	    n_strs++;
	}
      
      short[] res_str_sizes = new short [n_strs];
      count         = 0;
      for (i = 0; i < nOutFlds; i++) {
	if (proj_list[i].relation.key ==RelSpec.outer
	    && in1[proj_list[i].offset-1].attrType ==AttrType.attrString)
	  res_str_sizes[count++] = sizesT1[proj_list[i].offset-1];
      }
     
      try {
	JQuadruple.setHdr((short)nOutFlds, res_attrs, res_str_sizes);
      }catch (Exception e){
	throw new QuadrupleUtilsException(e,"setHdr() failed");
      } 
      return res_str_sizes;
    }
}


static boolean Equal(Quadruple q1,Quadruple q2)
{

	throws IOException,
	   UnknowAttrType,
	   QuadrupleUtilsException


	int val1=CompareQuadrupleWithQuadruple(LID,q1,1,q2,1);
	int val2=CompareQuadrupleWithQuadruple(LID,q1,2,q2,2);
	int val3=CompareQuadrupleWithQuadruple(LID,q1,3,q2,3);
	int val4=CompareQuadrupleWithQuadruple(LID,q1,4,q2,4);

	int sum=val1+val2+val3+val4;

	if(sum==4){
			return true;
	}
	else{

			return false;
	}


}
