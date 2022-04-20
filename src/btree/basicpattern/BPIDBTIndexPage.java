/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */


package btree.basicpattern;

import btree.*;
import btree.interfaces.BTIndexPageI;
import diskmgr.Page;
import global.PageId;
import global.BPID;
import global.RID;
import heap.Tuple;
import basicpatternheap.BasicPattern;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.keydataentry.BPIDKeyDataEntrySupplier;
import utils.supplier.keydataentry.RIDKeyDataEntrySupplier;
import utils.supplier.leafdata.LeafDataSupplier;
import utils.supplier.leafdata.BPIDLeafDataSupplier;
import utils.supplier.leafdata.RIDLeafDataSupplier;

import java.io.IOException;

/**
 * A BTIndexPage is an index page on a B+ tree.  It holds abstract 
 * {key, PageId} pairs; it doesn't know anything about the keys 
 * (their lengths or their types), instead relying on the abstract
 * interface in BT.java
 * See those files for our {key,data} pairing interface and implementation.
 */
public class BPIDBTIndexPage extends BTIndexPageI<BPID, BasicPattern>{
    
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
    
    /** pin the page with pageno, and get the corresponding BTIndexPage,
   * also it sets the type of node to be NodeType.INDEX.
   *@param pageno Input parameter. To specify which page number the
   *  BTIndexPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *   Input parameter.
   *@exception IOException error from the lower layer
   *@exception ConstructPageException error when BTIndexpage constructor
   */
  public BPIDBTIndexPage(PageId pageno, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(pageno, keyType);
    }
  
  /**associate the BTIndexPage instance with the Page instance,
   * also it sets the type of node to be NodeType.INDEX.
   *@param page input parameter. To specify which page  the
   *  BTIndexPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException error when BTIndexpage constructor
   */
  public BPIDBTIndexPage(Page page, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(page, keyType);
    }
  
  /* new a page, associate the BTIndexPage instance with the Page instance,
   * also it sets the type of node to be NodeType.INDEX.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException error when BTIndexpage constructor
   */
  public BPIDBTIndexPage(int keyType)
    throws IOException, 
	   ConstructPageException
    {
      super(keyType);
    }    
  
  
  /** It inserts a <key, pageNo> value into the index page,
   *@key  the key value in <key, pageNO>. Input parameter. 
   *@pageNo the pageNo  in <key, pageNO>. Input parameter.
   *@return It returns the rid where the record is inserted;
   null if no space left.
   *@exception IndexInsertRecException error when insert
   */
   public BPID insertKey(KeyClass key, PageId pageNo)
      throws  IndexInsertRecException
    {
        return super.insertKey(key,pageNo);
    }
  
  /*  OPTIONAL: fullDeletekey 
   * This is optional, and is only needed if you want to do full deletion.
   * Return its QID.  delete key may != key.  But delete key <= key,
   * and the delete key is the first biggest key such that delete key <= key 
   *@param key the key used to search. Input parameter.
   *@exception IndexFullDeleteException if no record deleted or failed by
   * any reason
   *@return  QID of the record deleted. Can not return null.
   */
  protected BPID deleteKey(KeyClass key)
    throws IndexFullDeleteException
    {
      return super.deleteKey(key);
    } // end of deleteKey
  
  
  
  /* 
   * This function encapsulates the search routine to search a
   * BTIndexPage by B++ search algorithm 
   *@param key  the key value used in search algorithm. Input parameter. 
   *@return It returns the page_no of the child to be searched next.
   *@exception IndexSearchException Index search failed;
   */
  protected PageId getPageNoByKey(KeyClass key)
    throws IndexSearchException
    {
      return super.getPageNoByKey(key);
      
    } // getPageNoByKey
  
  
  
  /**  Iterators. 
   * One of the two functions: getFirst and getNext
   * which  provide an iterator interface to the records on a BTIndexPage.
   *@param rid It will be modified and the first rid in the index page
   * will be passed out by itself. Input and Output parameter. 
   *@return return the first KeyDataEntry in the index page.
   *null if NO MORE RECORD
   *@exception IteratorException  iterator error
   */
  public BPIDKeyDataEntry getFirst(BPID rid)
    throws IteratorException
    {
      
      return (BPIDKeyDataEntry) super.getFirst(rid);
      
    } // end of getFirst
  
  
  /**Iterators.  
   * One of the two functions: get_first and get_next which  provide an
   * iterator interface to the records on a BTIndexPage.
   *@param rid It will be modified and next rid will be passed out by itself.
   *         Input and Output parameter.
   *@return return the next KeyDataEntry in the index page. 
   *null if no more record
   *@exception IteratorException iterator error
   */
  public BPIDKeyDataEntry getNext (BPID rid)
    throws  IteratorException 
    {
      return (BPIDKeyDataEntry) super.getNext(rid);
    } // end of getNext
  
  
  /** Left Link 
   *  You will recall that the index pages have a left-most
   *  pointer that is followed whenever the search key value
   *  is less than the least key value in the index node. The
   *  previous page pointer is used to implement the left link.
   *@return It returns the left most link. 
   *@exception IOException error from the lower layer
   */
  protected PageId getLeftLink() 
    throws IOException
    {
      return super.getLeftLink();
    }
  
  
  
  /*It is used in full delete
   *@param key the key is used to search. Input parameter. 
   *@param pageNo It returns the pageno of the sibling. Input and Output
   *       parameter.
   *@return 0 if no sibling; -1 if left sibling; 1 if right sibling.
   *@exception IndexFullDeleteException delete failed
   */  
  
  protected int  getSibling(KeyClass key, PageId pageNo)
    throws IndexFullDeleteException
    {
      
      return super.getSibling(key,pageNo);
    } // end of getSibling  
  
  
  /* find the position for old key by findKeyData, 
   *  where the  newKey will be returned .
   *@newKey It will replace certain key in index page. Input parameter.
   *@oldKey It helps us to find which key will be replaced by
   * the newKey. Input parameter. 
   *@return false if no key was found; true if success.
   *@exception IndexFullDeleteException delete failed
   */
  
  protected boolean adjustKey(KeyClass newKey, KeyClass oldKey)
    throws IndexFullDeleteException
    {
      return super.adjustKey(newKey, oldKey);
    } // end of findKeyData     
  
  
  /* find a key  by B++ algorithm, 
   * but returned key may not equal the key passed in.
   *@param key input parameter.
   *@return return that key if found; otherwise return null;
   *@exception  IndexSearchException index search failed 
   * 
   */
  protected KeyClass findKey(KeyClass key)
    throws IndexSearchException
    {
        return super.findKey(key);
    }
  
  
  /*It is used in full delete
   *@param indexPage the sibling page of this. Input parameter.
   *@param parentIndexPage the parant of indexPage and this. Input parameter.
   *@param direction -1 if "this" is left sibling of indexPage ; 
   *      1 if "this" is right sibling of indexPage. Input parameter.
   *@param deletedKey the key which was already deleted, and cause 
   *     redistribution. Input parameter.
   *@exception RedistributeException Redistribution failed
   *@return true if redistrbution success. false if we can not redistribute them.
   */
  protected boolean redistribute(BPIDBTIndexPage indexPage, BPIDBTIndexPage parentIndexPage,
                                 int direction, KeyClass deletedKey)
    throws RedistributeException
    {
      return super.redistribute(indexPage,parentIndexPage,direction,deletedKey);
      
    } // end of redistribute  
    
    @Override
    protected BPID getID() {
        return new BPID();
    }
    
    @Override
    protected BasicPattern getTuple(byte[] record, int offset, int length) {
        return new BasicPattern(record,offset,length);
    }
}
