package index.quadraple;

import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;
import global.*;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import index.IndexException;
import index.UnknownIndexTypeException;
import index.interfaces.IndexScanI;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.UnknownKeyTypeException;
import quadrupleheap.Quadruple;
import utils.supplier.btfile.BTreeFileSupplier;
import utils.supplier.btfile.QIDBTreeFileSupplier;
import utils.supplier.btfile.RIDBTreeFileSupplier;
import utils.supplier.btfilescan.BTFileScanSupplier;
import utils.supplier.btfilescan.QIDBTFileScanSupplier;
import utils.supplier.btfilescan.RIDBTFileScanSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.hfile.QIDHFileSupplier;
import utils.supplier.hfile.RIDHFileSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.QIDHFilePageSupplier;
import utils.supplier.hfilepage.RIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.QIDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.keyclass.IDListKeyClassManager;
import utils.supplier.tuple.QIDTupleSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Index Scan iterator will directly access the required tuple using
 * the provided key. It will also perform selections and projections.
 * information about the tuples and the index are passed to the constructor,
 * then the user calls <code>get_next()</code> to get the tuples.
 */
public abstract class QIDIndexScan<K> extends IndexScanI<QID, Quadruple,K> {
    private KeyClass filter;
    private int indexType;
    @Override
    public IDSupplier<QID> getIDSupplier() {
        return QIDSupplier.getSupplier();
    }
    
    @Override
    public HFilePageSupplier<QID, Quadruple> getHFilePageSupplier() {
        return QIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    public BTFileScanSupplier<QID, Quadruple,K> getBTFileScanSupplier() {
        return new QIDBTFileScanSupplier<K>();
    }
    
    @Override
    public TupleSupplier<Quadruple> getTupleSupplier() {
        return QIDTupleSupplier.getSupplier();
    }
    
    @Override
    public HFileSupplier<QID, Quadruple> getHFileSupplier() {
        return QIDHFileSupplier.getSupplier();
    }
    
    @Override
    public BTreeFileSupplier<QID, Quadruple,K> getBTreeFileSupplier() {
        return new QIDBTreeFileSupplier();
    }
    
    /**
   * class constructor. set up the index scan.
   * @param index type of the index (B_Index, Hash)
   * @param relName name of the input relation
   * @param indName name of the input index
   * @param types array of types in this relation
   * @param str_sizes array of string sizes (for attributes that are string)
   * @param noInFlds number of fields in input tuple
   * @param noOutFlds number of fields in output tuple
   * @param outFlds fields to project
   * @param selects conditions to apply, first one is primary
   * @param fldNum field number of the indexed field
   * @param indexOnly whether the answer requires only the key or the tuple
   * @exception IndexException error from the lower layer
   * @exception InvalidTypeException tuple type not valid
   * @exception InvalidTupleSizeException tuple size not valid
   * @exception UnknownIndexTypeException index type unknown
   * @exception IOException from the lower layer
   */
  public QIDIndexScan(
	   IndexType     index,        
	   final String  relName,  
	   final String  indName,  
	   AttrType      types[],      
	   short         str_sizes[],     
	   int           noInFlds,          
	   int           noOutFlds,         
	   FldSpec       outFlds[],     
	   CondExpr      selects[],  
	   final int     fldNum,
	   final boolean indexOnly,
       KeyClass filter, int indexType
                     )
    throws IndexException, 
	   InvalidTypeException,
	   InvalidTupleSizeException,
	   UnknownIndexTypeException,
	   IOException
  {
    super(index, relName, indName, types, str_sizes, noInFlds, noOutFlds, outFlds, selects, fldNum, indexOnly);
    this.filter = filter;
    this.indexType = indexType;
  }
  @Override
  public boolean eval(CondExpr p[], Tuple t1, Tuple t2, AttrType in1[],
                      AttrType in2[]){

      if(this.filter==null){
          return true;
      }
      Quadruple q = (Quadruple)t1;
      LID subjectId = q.getSubject().returnLid();
      LID predicateId = q.getPredicate().returnLid();
      LID objectId = q.getObject().returnLid();
      float confidence = q.getValue();
      List<?> keyList = null;
      KeyClass key =null;
      try {
          switch (this.indexType){
              case 1:
                  keyList = Arrays.asList(subjectId, predicateId, objectId);
                  break;
              case 2:
                  keyList = Arrays.asList(predicateId, subjectId, objectId);
                  break;
              case 3:
                  keyList = Arrays.asList(subjectId);
                  break;
              case 4:
                  keyList = Arrays.asList(predicateId);
                  break;
              case 5:
                  keyList = Arrays.asList(objectId);
                  break;
          }
          key = ((IDListKeyClassManager)getKeyClassManager()).getKeyClass(keyList);
          return filter.equals(key);
      } catch (KeyTooLongException e) {
          e.printStackTrace();
      } catch (KeyNotMatchException e) {
          e.printStackTrace();
      }

      return false;
  }
  /**
   * returns the next tuple.
   * if <code>index_only</code>, only returns the key value 
   * (as the first field in a tuple)
   * otherwise, retrive the tuple and returns the whole tuple
   * @return the tuple
   * @exception IndexException error from the lower layer
   * @exception UnknownKeyTypeException key type unknown
   * @exception IOException from the lower layer
   */
  public Quadruple get_next()
    throws IndexException, 
	   UnknownKeyTypeException,
	   IOException
  {
    return super.get_next();
  }
  
  /**
   * Cleaning up the index scan, does not remove either the original
   * relation or the index from the database.
   * @exception IndexException error from the lower layer
   * @exception IOException from the lower layer
   */
  public void close() throws IOException, IndexException
  {
    super.close();
  }
}

