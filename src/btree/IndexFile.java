package btree;

import java.io.*;
import global.*;
import utils.supplier.keyclass.KeyClassManager;

/**
 * Contains the enumerated types of state of the scan
 */
class ScanState 
{
  public static final int NEWSCAN = 0; 
  public static final int SCANRUNNING = 1; 
  public static final int SCANCOMPLETE = 2; 
}

/**
 * Base class for a index file
 */
public abstract class IndexFile<I extends ID,K>
{
	public abstract KeyClassManager<K> getKeyClassManager();
	public void insert(final K val,final I rid) throws KeyTooLongException, KeyNotMatchException, IteratorException, NodeNotMatchException, ConstructPageException, LeafInsertRecException, IndexSearchException, UnpinPageException, PinPageException, ConvertException, DeleteRecException, LeafDeleteException, InsertException, IndexInsertRecException, IOException {
		this.insert(getKeyClassManager().getKeyClass(val),rid);
	}
	public void Delete(final K val,final I rid) throws KeyTooLongException, KeyNotMatchException, IteratorException, NodeNotMatchException, ConstructPageException, LeafInsertRecException, IndexSearchException, UnpinPageException, PinPageException, ConvertException, DeleteRecException, LeafDeleteException, InsertException, IndexInsertRecException, IOException, FreePageException, IndexFullDeleteException, RecordNotFoundException, InsertRecException, LeafRedistributeException, DeleteFashionException, RedistributeException {
		this.Delete(getKeyClassManager().getKeyClass(val),rid);
	}
	
  /**
   * Insert entry into the index file.
   * @param data the key for the entry
   * @param rid the rid of the tuple with the key
   * @exception IOException from lower layers
   * @exception KeyTooLongException the key is too long
   * @exception KeyNotMatchException the keys do not match
   * @exception LeafInsertRecException  insert record to leaf page failed 
   * @exception IndexInsertRecException insert record to index page failed
   * @exception ConstructPageException  fail to construct a header page
   * @exception UnpinPageException unpin page failed
   * @exception PinPageException  pin page failed
   * @exception NodeNotMatchException  nodes do not match
   * @exception ConvertException conversion failed (from global package)
   * @exception DeleteRecException delete record failed
   * @exception IndexSearchException index search failed
   * @exception IteratorException  error from iterator 
   * @exception LeafDeleteException delete leaf page failed
   * @exception InsertException insert record failed
   */
  abstract public void insert(final KeyClass data, final I rid)
    throws KeyTooLongException, 
	   KeyNotMatchException, 
	   LeafInsertRecException,   
	   IndexInsertRecException,
	   ConstructPageException, 
	   UnpinPageException,
	   PinPageException, 
	   NodeNotMatchException, 
	   ConvertException,
	   DeleteRecException,
	   IndexSearchException,
	   IteratorException, 
	   LeafDeleteException, 
	   InsertException,
	   IOException;
  
  /**
   * Delete entry from the index file.
   * @param data the key for the entry
   * @param rid the rid of the tuple with the key
   * @exception IOException from lower layers
   * @exception DeleteFashionException delete fashion undefined
   * @exception LeafRedistributeException failed to redistribute leaf page
   * @exception RedistributeException redistrubtion failed
   * @exception InsertRecException insert record failed
   * @exception KeyNotMatchException keys do not match
   * @exception UnpinPageException unpin page failed
   * @exception IndexInsertRecException insert record to index failed
   * @exception FreePageException  free page failed
   * @exception RecordNotFoundException failed to find the record
   * @exception PinPageException pin page failed
   * @exception IndexFullDeleteException full delete on index page failed
   * @exception LeafDeleteException delete leaf page failed
   * @exception IteratorException exception from iterating through records
   * @exception ConstructPageException fail to construct the header page
   * @exception DeleteRecException delete record failed
   * @exception IndexSearchException  index search failed
   */
  abstract public boolean Delete(final KeyClass data, final I rid)
    throws  DeleteFashionException, 
	    LeafRedistributeException,
	    RedistributeException,
	    InsertRecException,
	    KeyNotMatchException, 
	    UnpinPageException, 
	    IndexInsertRecException,
	    FreePageException, 
	    RecordNotFoundException, 
	    PinPageException,
	    IndexFullDeleteException, 
	    LeafDeleteException,
	    IteratorException, 
	    ConstructPageException, 
	    DeleteRecException,
	    IndexSearchException, 
	    IOException;
}
