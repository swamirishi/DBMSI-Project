/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */
package btree.quadraple;

import btree.KeyDataEntry;
import btree.ScanDeleteException;
import btree.ScanIteratorException;
import btree.interfaces.BTFileScanI;
import global.QID;
import heap.Tuple;
import quadrupleheap.Quadruple;
import utils.supplier.btleafpage.BTLeafPageSupplier;
import utils.supplier.btleafpage.QIDBTLeafPageSupplier;
import utils.supplier.btleafpage.RIDBTLeafPageSupplier;

import java.io.IOException;

/**
 * BTFileScan implements a search/iterate interface to B+ tree 
 * index files (class BTreeFile).  It derives from abstract base
 * class IndexFileScan.  
 */
public class QIDBTFileScan<K> extends BTFileScanI<QID, Quadruple,K>
{
    @Override
    public BTLeafPageSupplier<QID, Quadruple> getBTLeafSupplier() {
        return QIDBTLeafPageSupplier.getSupplier();
    }
    
    /**
   * Iterate once (during a scan).  
   *@return null if done; otherwise next KeyDataEntry
   *@exception ScanIteratorException iterator error
   */
  public QIDKeyDataEntry get_next()
    throws ScanIteratorException
    {

    return (QIDKeyDataEntry) super.get_next();
  }


  /**
   * Delete currently-being-scanned(i.e., just scanned)
   * data entry.
   *@exception ScanDeleteException  delete error when scan
   */
  public void delete_current() 
    throws ScanDeleteException {

    super.delete_current();
  }
  
  /** max size of the key
   *@return the maxumum size of the key in BTFile
   */
  public int keysize() {
    return super.keysize();
  }  
  
  
  
  /**
  * destructor.
  * unpin some pages if they are not unpinned already.
  * and do some clearing work.
  *@exception IOException  error from the lower layer
  *@exception bufmgr.InvalidFrameNumberException  error from the lower layer
  *@exception bufmgr.ReplacerException  error from the lower layer
  *@exception bufmgr.PageUnpinnedException  error from the lower layer
  *@exception bufmgr.HashEntryNotFoundException   error from the lower layer
  */
  public  void DestroyBTreeFileScan()
    throws  IOException, bufmgr.InvalidFrameNumberException,bufmgr.ReplacerException,
            bufmgr.PageUnpinnedException,bufmgr.HashEntryNotFoundException   
  { 
     super.DestroyBTreeFileScan();
  }




}





