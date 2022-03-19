/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */

package btree.quadraple;

import btree.*;
import btree.interfaces.BTLeafPageI;
import diskmgr.Page;
import global.PageId;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.QIDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.keydataentry.QIDKeyDataEntrySupplier;
import utils.supplier.keydataentry.RIDKeyDataEntrySupplier;
import utils.supplier.leafdata.LeafDataSupplier;
import utils.supplier.leafdata.QIDLeafDataSupplier;
import utils.supplier.leafdata.RIDLeafDataSupplier;

import java.io.IOException;

/**
 * A BTLeafPage is a leaf page on a B+ tree.  It holds abstract 
 * <key, RID> pairs; it doesn't know anything about the keys 
 * (their lengths or their types), instead relying on the abstract
 * interface consisting of BT.java.
 */
public class QIDBTLeafPage extends BTLeafPageI<QID, Quadruple> {
    
    @Override
    public LeafDataSupplier<QID> getLeafDataSupplier() {
        return QIDLeafDataSupplier.getSupplier();
    }
    
    @Override
    public IDSupplier<QID> getIdSupplier() {
        
        return QIDSupplier.getSupplier();
    }
    
    @Override
    public KeyDataEntrySupplier<QID> getKeyDataEntrySupplier() {
        return QIDKeyDataEntrySupplier.getSupplier();
    }
  /** pin the page with pageno, and get the corresponding BTLeafPage,
   * also it sets the type to be NodeType.LEAF.
   *@param pageno Input parameter. To specify which page number the
   *  BTLeafPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *    Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException BTLeafPage constructor error
   */
  public QIDBTLeafPage(PageId pageno, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(pageno,keyType);
    }
  
  /**associate the BTLeafPage instance with the Page instance,
   * also it sets the type to be NodeType.LEAF.
   *@param page  input parameter. To specify which page  the
   *  BTLeafPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException BTLeafPage constructor error
   */
  public QIDBTLeafPage(Page page, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(page, keyType);
    }
  
  /**new a page, associate the BTLeafPage instance with the Page instance,
   * also it sets the type to be NodeType.LEAF.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException BTLeafPage constructor error
   */
  public QIDBTLeafPage(int keyType)
    throws IOException, 
	   ConstructPageException
    {
      super(keyType);
    }  
  

  
  /** insertRecord
   * READ THIS DESCRIPTION CAREFULLY. THERE ARE TWO RIDs
   * WHICH MEAN TWO DIFFERENT THINGS.
   * Inserts a key, rid value into the leaf node. This is
   * accomplished by a call to SortedPage::insertRecord()
   *  Parameters:
   *@param key - the key value of the data record. Input parameter.
   *@param dataRid - the rid of the data record. This is
   *               stored on the leaf page along with the
   *               corresponding key value. Input parameter.
   *
   *@return - the rid of the inserted leaf record data entry,
   *           i.e., the <key, dataRid> pair.
   *@exception LeafInsertRecException error when insert
   */   
  public QID insertRecord(KeyClass key, QID dataRid)
    throws  LeafInsertRecException
    {
      return super.insertRecord(key,dataRid);
    } // end of insertRecord
  
  
  /**  Iterators. 
   * One of the two functions: getFirst and getNext
   * which  provide an iterator interface to the records on a BTLeafPage.
   *@param rid It will be modified and the first rid in the leaf page
   * will be passed out by itself. Input and Output parameter.
   *@return return the first KeyDataEntry in the leaf page.
   * null if no more record
   *@exception IteratorException iterator error
   */
  public QIDKeyDataEntry getFirst(QID rid)
    throws  IteratorException
    {
      return (QIDKeyDataEntry) super.getFirst(rid);
    } // end of getFirst

 
   /**Iterators.  
    * One of the two functions: getFirst and getNext which  provide an
    * iterator interface to the records on a BTLeafPage.
    *@param rid It will be modified and the next rid will be passed out 
    *by itself. Input and Output parameter.
    *@return return the next KeyDataEntry in the leaf page. 
    *null if no more record.
    *@exception IteratorException iterator error
    */

   public QIDKeyDataEntry getNext (QID rid)
     throws  IteratorException
   {
     return (QIDKeyDataEntry) super.getNext(rid);
  }
  
  
  
  /**
   * getCurrent returns the current record in the iteration; it is like
   * getNext except it does not advance the iterator.
   *@param rid  the current rid. Input and Output parameter. But
   *    Output=Input.
   *@return return the current KeyDataEntry
   *@exception  IteratorException iterator error
   */ 
   public QIDKeyDataEntry getCurrent (QID rid)
       throws  IteratorException
   {  
     return (QIDKeyDataEntry) super.getCurrent(rid);
   }
  
  
  /** 
   * delete a data entry in the leaf page.
   *@param dEntry the entry will be deleted in the leaf page. Input parameter.
   *@return true if deleted; false if no dEntry in the page
   *@exception LeafDeleteException error when delete
   */
   public boolean delEntry (QIDKeyDataEntry dEntry)
     throws  LeafDeleteException
    {
      return super.delEntry(dEntry);
      
    } // end of delEntry

  /*used in full delete 
   *@param leafPage the sibling page of this. Input parameter.
   *@param parentIndexPage the parant of leafPage and this. Input parameter.
   *@param direction -1 if "this" is left sibling of leafPage ; 
   *      1 if "this" is right sibling of leafPage. Input parameter.
   *@param deletedKey the key which was already deleted, and cause 
   *        redistribution. Input parameter.
   *@exception LeafRedistributeException
   *@return true if redistrbution success. false if we can not redistribute them.
   */
  protected boolean redistribute(QIDBTLeafPage leafPage, QIDBTIndexPage parentIndexPage,
                                 int direction, KeyClass deletedKey)
    throws LeafRedistributeException
    {
      return super.redistribute(leafPage,parentIndexPage,direction,deletedKey);
    } // end of redistribute
    
    @Override
    protected QID getID() {
        return new QID();
    }
    
    @Override
    protected Quadruple getTuple(byte[] record, int offset, int length) {
        return new Quadruple(record,offset,length);
    }
} // end of BTLeafPage

    
 





















