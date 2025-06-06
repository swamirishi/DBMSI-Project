package btree;

import btree.interfaces.IKeyDataEntry;
import global.ID;

/**
 * Base class for a index file scan
 */
public abstract class IndexFileScan<I extends ID>
{
  /**
   * Get the next record.
   * @exception ScanIteratorException error when iterating through the records
   * @return the KeyDataEntry, which contains the key and data
   */
  abstract public IKeyDataEntry<I> get_next()
    throws ScanIteratorException;

  /** 
   * Delete the current record.
   * @exception ScanDeleteException delete current record failed
   */
   abstract public void delete_current()
     throws ScanDeleteException;

  /**
   * Returns the size of the key
   * @return the keysize
   */
  abstract public int keysize();
}
