/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */
package btree.label;

import btree.ScanDeleteException;
import btree.ScanIteratorException;
import btree.interfaces.BTFileScanI;
import global.LID;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;
import utils.supplier.btleafpage.BTLeafPageSupplier;
import utils.supplier.btleafpage.LIDBTLeafPageSupplier;
import utils.supplier.btleafpage.QIDBTLeafPageSupplier;

import java.io.IOException;

/**
 * BTFileScan implements a search/iterate interface to B+ tree 
 * index files (class BTreeFile).  It derives from abstract base
 * class IndexFileScan.  
 */
public class LIDBTFileScan extends BTFileScanI<LID, Label>
{
    @Override
    public BTLeafPageSupplier<LID, Label> getBTLeafSupplier() {
        return LIDBTLeafPageSupplier.getSupplier();
    }
    
    /**
   * Iterate once (during a scan).  
   *@return null if done; otherwise next KeyDataEntry
   *@exception ScanIteratorException iterator error
   */
  public LIDKeyDataEntry get_next()
    throws ScanIteratorException
    {

    return (LIDKeyDataEntry) super.get_next();
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





