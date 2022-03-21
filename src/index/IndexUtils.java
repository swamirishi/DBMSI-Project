package index;
import btree.interfaces.BTreeFileI;
import diskmgr.RDFDB;
import global.*;
import btree.*;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import index.label.LIDIndexScan;
import index.quadraple.QIDIndexScan;
import iterator.*;
import labelheap.Label;
import quadrupleheap.Quadruple;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.keyclass.KeyClassManager;
import utils.supplier.keyclass.LIDKeyClassManager;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * IndexUtils class opens an index scan based on selection conditions.
 * Currently only BTree_scan is supported
 */
public class IndexUtils {

  /**
   * BTree_scan opens a BTree scan based on selection conditions
   * @param selects conditions to apply
   * @param indFile the index (BTree) file
   * @return an instance of IndexFileScan (BTreeFileScan)
   * @exception IOException from lower layer
   * @exception UnknownKeyTypeException only int and string keys are supported 
   * @exception InvalidSelectionException selection conditions (selects) not valid
   * @exception KeyNotMatchException Keys do not match
   * @exception UnpinPageException unpin page failed
   * @exception PinPageException pin page failed
   * @exception IteratorException iterator exception
   * @exception ConstructPageException failed to construct a header page
   */
  public static <I extends ID,T extends Tuple,K> IndexFileScan<I> BTree_scan(CondExpr[] selects, IndexFile<I,K> indFile)
    throws IOException, 
	   UnknownKeyTypeException, 
	   InvalidSelectionException,
	   KeyNotMatchException,
	   UnpinPageException,
	   PinPageException,
	   IteratorException,
	   ConstructPageException
    {
      IndexFileScan<I> indScan;
      
      if (selects == null || selects[0] == null) {
	indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(null, null);
	return indScan;
      }
      
      if (selects[1] == null) {
	if (selects[0].type1.attrType != AttrType.attrSymbol && selects[0].type2.attrType != AttrType.attrSymbol) {
	  throw new InvalidSelectionException("IndexUtils.java: Invalid selection condition"); 
	}
	
	KeyClass key;
	
	// symbol = value
	if (selects[0].op.attrOperator == AttrOperator.aopEQ) {
	  if (selects[0].type1.attrType != AttrType.attrSymbol) {
	    key = getValue(selects[0], selects[0].type1, 1);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key, key);
	  }
	  else {
	    key = getValue(selects[0], selects[0].type2, 2);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key, key);
	  }
	  return indScan;
	}
	
	// symbol < value or symbol <= value
	if (selects[0].op.attrOperator == AttrOperator.aopLT || selects[0].op.attrOperator == AttrOperator.aopLE) {
	  if (selects[0].type1.attrType != AttrType.attrSymbol) {
	    key = getValue(selects[0], selects[0].type1, 1);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(null, key);
	  }
	  else {
	    key = getValue(selects[0], selects[0].type2, 2);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(null, key);
	  }
	  return indScan;
	}
	
	// symbol > value or symbol >= value
	if (selects[0].op.attrOperator == AttrOperator.aopGT || selects[0].op.attrOperator == AttrOperator.aopGE) {
	  if (selects[0].type1.attrType != AttrType.attrSymbol) {
	    key = getValue(selects[0], selects[0].type1, 1);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key, null);
	  }
	  else {
	    key = getValue(selects[0], selects[0].type2, 2);
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key, null);
	  }
	  return indScan;
	}
	
	// error if reached here
	System.err.println("Error -- in IndexUtils.BTree_scan()");
	return null;
      }
      else {
	// selects[1] != null, must be a range query
	if (selects[0].type1.attrType != AttrType.attrSymbol && selects[0].type2.attrType != AttrType.attrSymbol) {
	  throw new InvalidSelectionException("IndexUtils.java: Invalid selection condition"); 
	}
	if (selects[1].type1.attrType != AttrType.attrSymbol && selects[1].type2.attrType != AttrType.attrSymbol) {
	  throw new InvalidSelectionException("IndexUtils.java: Invalid selection condition"); 
	}
	
	// which symbol is higher??
	KeyClass key1, key2;
	AttrType type;
	
	if (selects[0].type1.attrType != AttrType.attrSymbol) {
	  key1 = getValue(selects[0], selects[0].type1, 1);
	  type = selects[0].type1;
	}
	else {
	  key1 = getValue(selects[0], selects[0].type2, 2);
	  type = selects[0].type2;
	}
	if (selects[1].type1.attrType != AttrType.attrSymbol) {
	  key2 = getValue(selects[1], selects[1].type1, 1);
	}
	else {
	  key2 = getValue(selects[1], selects[1].type2, 2);
	}
	
	switch (type.attrType) {
	case AttrType.attrString:
	  if (((StringKey)key1).getKey().compareTo(((StringKey)key2).getKey()) < 0) {
	    indScan = ((BTreeFileI<I, T,K>)indFile).new_scan(key1, key2);
	  }
	  else {
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key2, key1);
	  }
	  return indScan;
	  
	case AttrType.attrInteger:
	  if (((IntegerKey)key1).getKey().intValue() < ((IntegerKey)key2).getKey().intValue()) {
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key1, key2);
	  }
	  else {
	    indScan = ((BTreeFileI<I,T,K>)indFile).new_scan(key2, key1);
	  }
	  return indScan;
	  
	case AttrType.attrReal:
	  /*
	    if ((FloatKey)key1.getKey().floatValue() < (FloatKey)key2.getKey().floatValue()) {
	    indScan = ((BTreeFile)indFile).new_scan(key1, key2);
	    }
	    else {
	    indScan = ((BTreeFile)indFile).new_scan(key2, key1);
	    }
	    return indScan;
	  */
	default:
	  // error condition
	  throw new UnknownKeyTypeException("IndexUtils.java: Only Integer and String keys are supported so far");	
	}
      } // end of else 
      
    } 

  /**
   * getValue returns the key value extracted from the selection condition.
   * @param cd the selection condition
   * @param type attribute type of the selection field
   * @param choice first (1) or second (2) operand is the value
   * @return an instance of the KeyClass (IntegerKey or StringKey)
   * @exception UnknownKeyTypeException only int and string keys are supported 
   */
  private static KeyClass getValue(CondExpr cd, AttrType type, int choice)
       throws UnknownKeyTypeException
  {
    // error checking
    if (cd == null) {
      return null;
    }
    if (choice < 1 || choice > 2) {
      return null;
    }
    
    switch (type.attrType) {
    case AttrType.attrString:
      if (choice == 1) return new StringKey(cd.operand1.string);
      else return new StringKey(cd.operand2.string);
    case AttrType.attrInteger:
      if (choice == 1) return new IntegerKey(new Integer(cd.operand1.integer));
      else return new IntegerKey(new Integer(cd.operand2.integer));
    case AttrType.attrReal:
      /*
      // need FloatKey class in bt.java
      if (choice == 1) return new FloatKey(new Float(cd.operand.real));
      else return new FloatKey(new Float(cd.operand.real));
      */
    default:
	throw new UnknownKeyTypeException("IndexUtils.java: Only Integer and String keys are supported so far");
    }
    
  }
//  public static <K> QIDIndexScan<K> getQIDIndexScan(String heapFileName, String bTreeFileName, KeyClassManager<K> keyClassManager, K filter, int indexOption) throws KeyTooLongException, KeyNotMatchException {
//	  KeyClass k = keyClassManager.getKeyClass(filter);
//  	AttrType[] attrType = new AttrType[1];
//	  attrType[0] = new AttrType(AttrType.attrString);
//	  short[] attrSize = new short[1];
//	  attrSize[0] = Label.;
//
//	  FldSpec[] projlist = new FldSpec[1];
//	  RelSpec rel = new RelSpec(RelSpec.outer);
//	  projlist[0] = new FldSpec(rel, 1);
//
//	  CondExpr[] expr = new CondExpr[2];
//	  expr[0] = new CondExpr();
//	  expr[0].op = new AttrOperator(AttrOperator.aopEQ);
//	  expr[0].type1 = new AttrType(AttrType.attrSymbol);
//	  expr[0].type2 = new AttrType(AttrType.attrString);
//	  expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
//	  if(k instanceof StringKey){
//		  expr[0].operand2.string = ((StringKey) k).getKey();
//	  }else if(k instanceof IntegerKey){
//		  expr[0].operand1.integer =  ((IntegerKey) k).getKey();
//	  }
//
//
//	  expr[0].next = null;
//	  expr[1] = null;
//	  QIDIndexScan<K> iscan = new QIDIndexScan<K>(new IndexType(IndexType.B_Index),
//	                                                    heapFileName,
//	                                                    bTreeFileName,
//	                                                    attrType,
//	                                                    attrSize,
//	                                                    1,
//	                                                    1,
//	                                                    projlist,
//	                                                    expr,
//	                                                    1,
//	                                                    false,k,) {
//		  @Override
//		  public KeyClassManager<K> getKeyClassManager() {
//			  return keyClassManager;
//		  }
//	  };
//	  return iscan;
//  }
	
	public static <K> QIDIndexScan<K> initializeQIDScan(KeyClass filter, KeyClassManager<K> keyClassManager, int index) throws IndexException, InvalidTupleSizeException, IOException, UnknownIndexTypeException, InvalidTypeException {
		RelSpec rel = new RelSpec(RelSpec.outer);
		FldSpec[] projlist2 = new FldSpec[Quadruple.numberOfFields];
		
		for (int i = 0; i < projlist2.length; i++)
			projlist2[i] = new FldSpec(rel, i + 1);
		
		CondExpr[] expr = new CondExpr[2];
		expr[0] = new CondExpr();
		expr[0].op = new AttrOperator(AttrOperator.aopEQ);
		expr[0].type1 = new AttrType(AttrType.attrSymbol);
		expr[0].type2 = new AttrType(AttrType.attrString);
		expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
		if(filter instanceof StringKey){
			expr[0].operand2.string = ((StringKey) filter).getKey();
		}else if(filter instanceof IntegerKey){
			expr[0].operand2.integer = ((IntegerKey) filter).getKey();
		}
		
		expr[0].next = null;
		expr[1] = null;
		
		IndexType indexType = new IndexType(IndexType.B_Index);
		QIDIndexScan<K> qidScan = new QIDIndexScan<K>(indexType, RDFDB.quadrupleHeapFileName, RDFDB.qidBTreeFileName,
		                                                          Quadruple.headerTypes, Quadruple.strSizes, Quadruple.numberOfFields, Quadruple.numberOfFields, projlist2, null, 1, false, filter, index) {
			@Override
			public KeyClassManager<K> getKeyClassManager() {
				return keyClassManager;
			}
		};
		return qidScan;
	}
	
  public static LIDIndexScan<Void> getLabelBTreeScan(String heapFileName, String bTreeFileName, String filter) throws UnknownIndexTypeException, InvalidTypeException, IndexException, IOException, InvalidTupleSizeException {
	  AttrType[] attrType = new AttrType[1];
	  attrType[0] = new AttrType(AttrType.attrString);
	  short[] attrSize = new short[1];
	  attrSize[0] = Label.MAX_LENGTH;
	
	  FldSpec[] projlist = new FldSpec[1];
	  RelSpec rel = new RelSpec(RelSpec.outer);
	  projlist[0] = new FldSpec(rel, 1);
	
	  CondExpr[] expr = new CondExpr[2];
	  expr[0] = new CondExpr();
	  expr[0].op = new AttrOperator(AttrOperator.aopEQ);
	  expr[0].type1 = new AttrType(AttrType.attrSymbol);
	  expr[0].type2 = new AttrType(AttrType.attrString);
	  expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
	  expr[0].operand2.string = filter;
	  expr[0].next = null;
	  expr[1] = null;
	  LIDIndexScan<Void> iscan = new LIDIndexScan<Void>(new IndexType(IndexType.B_Index),
	                                                    heapFileName,
	                                                    bTreeFileName,
	                                                    attrType,
	                                                    attrSize,
	                                                    1,
	                                                    1,
	                                                    projlist,
	                                                    expr,
	                                                    1,
	                                                    false) {
		  @Override
		  public KeyClassManager<Void> getKeyClassManager() {
			  return null;
		  }
	  };
	  return iscan;
  	
  }
  
  public static boolean isLabelRecordInBtreeFound(String heapFileName, String bTreeFileName, String record) throws UnknownIndexTypeException, InvalidTypeException, IndexException, InvalidTupleSizeException, IOException, UnknownKeyTypeException {
	  LIDIndexScan<Void> iscan = getLabelBTreeScan(heapFileName, bTreeFileName, record);
	  boolean isFound = iscan.get_next()!=null;
	  iscan.close();
	  return isFound;
  }
  
  public static <K> boolean isKeyFoundInQIDBtree(String heapFileName, String bTreeFileName, KeyClassManager<K> keyClassManager, K key, int index) throws KeyTooLongException, KeyNotMatchException, UnknownKeyTypeException, IndexException, IOException, InvalidTypeException, InvalidTupleSizeException, UnknownIndexTypeException {
	  QIDIndexScan<K> iscan = initializeQIDScan(keyClassManager.getKeyClass(key),keyClassManager,index);
	  boolean isFound = iscan.get_next()!=null;
	  iscan.close();
	  return isFound;
  }
  
}
