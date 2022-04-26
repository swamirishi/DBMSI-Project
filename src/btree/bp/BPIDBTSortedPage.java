/*
 * @(#) SortedPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *
 *      by Xiaohu Li (xiaohu@cs.wisc.edu)
 */

package btree.bp;

import basicpatternheap.BasicPattern;
import btree.ConstructPageException;
import btree.InsertRecException;
import btree.interfaces.BTSortedPageI;
import diskmgr.Page;
import global.BPID;
import global.PageId;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.keydataentry.BPIDKeyDataEntrySupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.leafdata.BPIDLeafDataSupplier;
import utils.supplier.leafdata.LeafDataSupplier;

import java.io.IOException;

/**
 * BTsortedPage class 
 * just holds abstract records in sorted order, based 
 * on how they compare using the key interface from BT.java.
 */
public class BPIDBTSortedPage extends BTSortedPageI<BPID, BasicPattern> {

  
  int keyType; //it will be initialized in BTFile
    @Override
    public LeafDataSupplier<BPID> getLeafDataSupplier() {
        return BPIDLeafDataSupplier.getSupplier();
    }
    
    @Override
    public IDSupplier<BPID> getIdSupplier() {
        return BPIDSupplier.getSupplier();
    }
    
    @Override
    public KeyDataEntrySupplier<BPID> getKeyDataEntrySupplier() {
        return BPIDKeyDataEntrySupplier.getSupplier();
    }
    
    /** pin the page with pageno, and get the corresponding SortedPage
   *@param pageno input parameter. To specify which page number the
   *  BTSortedPage will correspond to.
   *@param keyType input parameter. It specifies the type of key. It can be
   *               AttrType.attrString or AttrType.attrInteger.
   *@exception ConstructPageException  error for BTSortedPage constructor
   */
  public BPIDBTSortedPage(PageId pageno, int keyType)
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
  public BPIDBTSortedPage(Page page, int keyType) {
    
    super(page,keyType);
  }
  
  
  /**new a page, and associate the SortedPage instance with the Page instance
   *@param keyType input parameter. It specifies the type of key. It can be
   *               AttrType.attrString or AttrType.attrInteger.
   *@exception  ConstructPageException error for BTSortedPage constructor
   */
  public BPIDBTSortedPage(int keyType)
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
   *@exception InsertRecException error when insert
   */
   protected BPID insertRecord(BPIDKeyDataEntry entry)
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
    protected BPID getID() {
        return new BPID();
    }
    
    @Override
    protected BasicPattern getTuple(byte[] record, int offset, int length) {
        return new BasicPattern(record,offset,length);
    }
}




