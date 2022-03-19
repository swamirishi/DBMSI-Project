/*
 * @(#) SortedPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *
 *      by Xiaohu Li (xiaohu@cs.wisc.edu)
 */

package btree;

import java.io.*;
import java.lang.*;

import btree.interfaces.BTSortedPageI;
import global.*;
import diskmgr.*;
import heap.*;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.keydataentry.RIDKeyDataEntrySupplier;
import utils.supplier.leafdata.LeafDataSupplier;
import utils.supplier.leafdata.RIDLeafDataSupplier;

/**
 * BTsortedPage class 
 * just holds abstract records in sorted order, based 
 * on how they compare using the key interface from BT.java.
 */
public class BTSortedPage  extends BTSortedPageI<RID,Tuple> {

  
  int keyType; //it will be initialized in BTFile
    @Override
    public LeafDataSupplier<RID> getLeafDataSupplier() {
        return RIDLeafDataSupplier.getSupplier();
    }
    
    @Override
    public IDSupplier<RID> getIdSupplier() {
        return RIDSupplier.getSupplier();
    }
    
    @Override
    public KeyDataEntrySupplier<RID> getKeyDataEntrySupplier() {
        return RIDKeyDataEntrySupplier.getSupplier();
    }
    
    /** pin the page with pageno, and get the corresponding SortedPage
   *@param pageno input parameter. To specify which page number the
   *  BTSortedPage will correspond to.
   *@param keyType input parameter. It specifies the type of key. It can be 
   *               AttrType.attrString or AttrType.attrInteger. 
   *@exception  ConstructPageException  error for BTSortedPage constructor
   */
  public BTSortedPage(PageId pageno, int keyType) 
    throws ConstructPageException 
    { 
      super(pageno,keyType);
    }
  
  /**associate the SortedPage instance with the Page instance 
   *@param page input parameter. To specify which page  the
   *  BTSortedPage will correspond to.
   *@param keyType input parameter. It specifies the type of key. It can be 
   *               AttrType.attrString or AttrType.attrInteger. 
   */
  public BTSortedPage(Page page, int keyType) {
    
    super(page,keyType);
  }  
  
  
  /**new a page, and associate the SortedPage instance with the Page instance
   *@param keyType input parameter. It specifies the type of key. It can be 
   *               AttrType.attrString or AttrType.attrInteger. 
   *@exception  ConstructPageException error for BTSortedPage constructor
   */ 
  public BTSortedPage(int keyType) 
    throws ConstructPageException
    {
      super(keyType);
    }  
  
  /**
   * Performs a sorted insertion of a record on an record page. The records are
   *  sorted in increasing key order.
   *  Only the  slot  directory is  rearranged.  The  data records remain in
   *  the same positions on the  page.
   * 
   *@param entry the entry to be inserted. Input parameter.
   *@return its rid where the entry was inserted; null if no space left.
   *@exception  InsertRecException error when insert
   */
   protected RID insertRecord( KeyDataEntry entry)
          throws InsertRecException 
   {
     return super.insertRecord(entry);
   } // end of insertRecord
  /** How many records are in the page
   *@exception IOException I/O errors
   */
  public int numberOfRecords()
    throws IOException
    {
      return getSlotCnt();
    }
    
    @Override
    protected RID getID() {
        return new RID();
    }
    
    @Override
    protected Tuple getTuple(byte[] record, int offset, int length) {
        return new Tuple(record,offset,length);
    }
}




