/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */

package btree.interfaces;

import btree.*;
import diskmgr.Page;
import global.ID;
import global.PageId;
import heap.Tuple;

import java.io.IOException;

/**
 * A BTLeafPage is a leaf page on a B+ tree.  It holds abstract 
 * <key, RID> pairs; it doesn't know anything about the keys 
 * (their lengths or their types), instead relying on the abstract
 * interface consisting of BT.java.
 */
public abstract class BTLeafPageI<I extends ID,T extends Tuple> extends BTSortedPageI<I,T> {
  
  /** pin the page with pageno, and get the corresponding BTLeafPage,
   * also it sets the type to be NodeType.LEAF.
   *@param pageno Input parameter. To specify which page number the
   *  BTLeafPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *    Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException BTLeafPage constructor error
   */
  public BTLeafPageI(PageId pageno, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(pageno, keyType);
      setType(NodeType.LEAF);
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
  public BTLeafPageI(Page page, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(page, keyType);
      setType(NodeType.LEAF);
    }
  
  /**new a page, associate the BTLeafPage instance with the Page instance,
   * also it sets the type to be NodeType.LEAF.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException BTLeafPage constructor error
   */
  public BTLeafPageI(int keyType)
    throws IOException, 
	   ConstructPageException
    {
      super(keyType);
      setType(NodeType.LEAF);
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
  public I insertRecord(KeyClass key, I dataRid)
    throws  LeafInsertRecException
    {
      IKeyDataEntry<I> entry;
      
      try {
        entry = getKeyDataEntrySupplier().getKeyDataEntry(key,dataRid);
	
        return insertRecord(entry);
      }
      catch(Exception e) {
        throw new LeafInsertRecException(e, "insert record failed");
      }
    } // end of insertRecord
  
  
  /**  Iterators. 
   * One of the two functions: getFirst and getNext
   * which  provide an iterator interface to the records on a BTLeafPage.
   *@param rid It will be modified and the first rid in the leaf page
   * will be passed out by itself. Input and Output parameter.
   *@return return the first KeyDataEntry in the leaf page.
   * null if no more record
   *@exception  IteratorException iterator error
   */
  public IKeyDataEntry<I> getFirst(I rid)
    throws  IteratorException
    {
      
      IKeyDataEntry<I>  entry;
      
      try {
        rid.setPageNo(getCurPage());
        rid.setSlotNo(0); // begin with first slot
	
        if ( getSlotCnt() <= 0) {
          return null;
        }

        entry=BTI.getEntryFromBytes(getpage(), getSlotOffset(0), getSlotLength(0),
				   keyType, NodeType.LEAF,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	
        return entry;
      }
      catch (Exception e) {
	throw new IteratorException(e, "Get first entry failed");
      }
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

   public IKeyDataEntry<I> getNext (I rid)
     throws  IteratorException
   {
     IKeyDataEntry<I>  entry;
     int i;
     try{
       rid.setSlotNo(rid.getSlotNo()+1); //must before any return;
       i=rid.getSlotNo();
       
       if ( rid.getSlotNo() >= getSlotCnt())
       {
	 return null;
       }
       
       entry=BTI.getEntryFromBytes(getpage(),getSlotOffset(i), getSlotLength(i),
                  keyType, NodeType.LEAF,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
       
       return entry;
     } 
     catch (Exception e) {
       throw new IteratorException(e,"Get next entry failed");
     }
  }
  
  
  
  /**
   * getCurrent returns the current record in the iteration; it is like
   * getNext except it does not advance the iterator.
   *@param rid  the current rid. Input and Output parameter. But
   *    Output=Input.
   *@return return the current KeyDataEntry
   *@exception  IteratorException iterator error
   */ 
   public IKeyDataEntry<I> getCurrent (I rid)
       throws  IteratorException
   {  
     rid.setSlotNo(rid.getSlotNo()-1);
     return getNext(rid);
   }
  
  
  /** 
   * delete a data entry in the leaf page.
   *@param dEntry the entry will be deleted in the leaf page. Input parameter.
   *@return true if deleted; false if no dEntry in the page
   *@exception LeafDeleteException error when delete
   */
   public boolean delEntry (IKeyDataEntry<I> dEntry)
     throws  LeafDeleteException
    {
      IKeyDataEntry<I>  entry;
      I rid=getIdSupplier().getID();
      
      try {
	for(entry = getFirst(rid); entry!=null; entry=getNext(rid)) 
	  {  
	    if ( entry.equals(dEntry) ) {
	      if ( super.deleteSortedRecord( rid ) == false )
		throw new LeafDeleteException(null, "Delete record failed");
	      return true;
	    }
	    
	 }
	return false;
      } 
      catch (Exception e) {
	throw new LeafDeleteException(e, "delete entry failed");
      }
      
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
  protected boolean redistribute(BTLeafPageI<I,T> leafPage, BTIndexPageI<I,T> parentIndexPage,
                       int direction, KeyClass deletedKey)
    throws LeafRedistributeException
    {
      boolean st;
      // assertion: leafPage pinned
      try {
	if (direction ==-1) { // 'this' is the left sibling of leafPage
	  if ( (getSlotLength(getSlotCnt()-1) + available_space()+ 8 /*  2*sizeof(slot) */) > 
	       ((MAX_SPACE-DPFIXED)/2)) {
            // cannot spare a record for its underflow sibling
            return false;
	  }
	  else {
            // move the last record to its sibling
	    
            // get the last record 
            IKeyDataEntry<I> lastEntry;
            lastEntry=BTI.getEntryFromBytes(getpage(),getSlotOffset(getSlotCnt()-1)
					   ,getSlotLength(getSlotCnt()-1), keyType, NodeType.LEAF,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	    
	    
            //get its sibling's first record's key for adjusting parent pointer
            I dummyRid=getIdSupplier().getID();
            IKeyDataEntry<I> firstEntry;
            firstEntry=leafPage.getFirst(dummyRid);

            // insert it into its sibling            
            leafPage.insertRecord(lastEntry);
            
            // delete the last record from the old page
            I delRid=getIdSupplier().getID();
            delRid.setPageNo(getCurPage());
            delRid.setSlotNo(getSlotCnt()-1);
            if ( deleteSortedRecord(delRid) == false )
	      throw new LeafRedistributeException(null, "delete record failed");

	    
            // adjust the entry pointing to sibling in its parent
            if (deletedKey != null)
                st = parentIndexPage.adjustKey(lastEntry.key, deletedKey);
            else 
                st = parentIndexPage.adjustKey(lastEntry.key,
                                            firstEntry.key);
            if (st == false) 
	      throw new LeafRedistributeException(null, "adjust key failed");
            return true;
	  }
	}
	else { // 'this' is the right sibling of pptr
	  if ( (getSlotLength(0) + available_space()+ 8) > ((MAX_SPACE-DPFIXED)/2)) {
            // cannot spare a record for its underflow sibling
            return false;
	  }
	  else {
            // move the first record to its sibling
	    
            // get the first record
            IKeyDataEntry<I> firstEntry;
            firstEntry=BTI.getEntryFromBytes(getpage(), getSlotOffset(0),
					    getSlotLength(0), keyType,
					    NodeType.LEAF,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	    
            // insert it into its sibling
            I dummyRid=getIdSupplier().getID();
            leafPage.insertRecord(firstEntry);
            

            // delete the first record from the old page
            I delRid=getIdSupplier().getID();
            delRid.setPageNo(getCurPage());
            delRid.setSlotNo(0);
            if ( deleteSortedRecord(delRid) == false) 
	      throw new LeafRedistributeException(null, "delete record failed");  
	    
	    
            // get the current first record of the old page
            // for adjusting parent pointer.
            IKeyDataEntry<I> tmpEntry;
            tmpEntry = getFirst(dummyRid);
         
            
            // adjust the entry pointing to itself in its parent
            st = parentIndexPage.adjustKey(tmpEntry.key, firstEntry.key);
            if( st==false) 
	      throw new LeafRedistributeException(null, "adjust key failed"); 
            return true;
	  }
	}
      }
      catch (Exception e) {
	throw new LeafRedistributeException(e, "redistribute failed");
      } 
    } // end of redistribute
  
} // end of BTLeafPage

    
 





















