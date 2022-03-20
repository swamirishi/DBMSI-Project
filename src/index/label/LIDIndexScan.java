package index.label;

import global.AttrType;
import global.IndexType;
import global.LID;
import global.QID;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import index.IndexException;
import index.UnknownIndexTypeException;
import index.interfaces.IndexScanI;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.UnknownKeyTypeException;
import labelheap.Label;
import quadrupleheap.Quadruple;
import utils.supplier.btfile.BTreeFileSupplier;
import utils.supplier.btfile.LIDBTreeFileSupplier;
import utils.supplier.btfile.QIDBTreeFileSupplier;
import utils.supplier.btfilescan.BTFileScanSupplier;
import utils.supplier.btfilescan.LIDBTFileScanSupplier;
import utils.supplier.btfilescan.QIDBTFileScanSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.hfile.LIDHFileSupplier;
import utils.supplier.hfile.QIDHFileSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.LIDHFilePageSupplier;
import utils.supplier.hfilepage.QIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.LIDSupplier;
import utils.supplier.id.QIDSupplier;
import utils.supplier.tuple.LIDTupleSupplier;
import utils.supplier.tuple.QIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

/**
 * Index Scan iterator will directly access the required tuple using
 * the provided key. It will also perform selections and projections.
 * information about the tuples and the index are passed to the constructor,
 * then the user calls <code>get_next()</code> to get the tuples.
 */
public class LIDIndexScan extends IndexScanI<LID, Label> {
    @Override
    public IDSupplier<LID> getIDSupplier() {
        return LIDSupplier.getSupplier();
    }
    
    @Override
    public HFilePageSupplier<LID, Label> getHFilePageSupplier() {
        return LIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    public BTFileScanSupplier<LID, Label> getBTFileScanSupplier() {
        return LIDBTFileScanSupplier.getSupplier();
    }
    
    @Override
    public TupleSupplier<Label> getTupleSupplier() {
        return LIDTupleSupplier.getSupplier();
    }
    
    @Override
    public HFileSupplier<LID, Label> getHFileSupplier() {
        return LIDHFileSupplier.getSupplier();
    }
    
    @Override
    public BTreeFileSupplier<LID, Label> getBTreeFileSupplier() {
        return LIDBTreeFileSupplier.getSupplier();
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
  public LIDIndexScan(
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
	   final boolean indexOnly
                     )
    throws IndexException,
	   InvalidTypeException,
	   InvalidTupleSizeException,
	   UnknownIndexTypeException,
	   IOException
  {
    super(index, relName, indName, types, str_sizes, noInFlds, noOutFlds, outFlds, selects, fldNum, indexOnly);
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
  public Label get_next()
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

