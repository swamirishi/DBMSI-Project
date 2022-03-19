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
 * A BTIndexPage is an index page on a B+ tree.  It holds abstract 
 * {key, PageId} pairs; it doesn't know anything about the keys 
 * (their lengths or their types), instead relying on the abstract
 * interface in BT.java
 * See those files for our {key,data} pairing interface and implementation.
 */
public abstract class BTIndexPageI<I extends ID,T extends Tuple> extends BTSortedPageI<I,T> {
 

  /** pin the page with pageno, and get the corresponding BTIndexPage,
   * also it sets the type of node to be NodeType.INDEX.
   *@param pageno Input parameter. To specify which page number the
   *  BTIndexPage will correspond to.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *   Input parameter.
   *@exception IOException error from the lower layer
   *@exception ConstructPageException error when BTIndexpage constructor
   */
  public BTIndexPageI(PageId pageno, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(pageno, keyType);
      setType(NodeType.INDEX);
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
  public BTIndexPageI(Page page, int keyType)
    throws IOException,
	   ConstructPageException
    {
      super(page, keyType);
      setType(NodeType.INDEX);
    }
  
  /* new a page, associate the BTIndexPage instance with the Page instance,
   * also it sets the type of node to be NodeType.INDEX.
   *@param keyType either AttrType.attrInteger or AttrType.attrString.
   *  Input parameter.
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException error when BTIndexpage constructor
   */
  public BTIndexPageI(int keyType)
    throws IOException, 
	   ConstructPageException
    {
      super(keyType);
      setType(NodeType.INDEX);
    }    
  
  
  /** It inserts a <key, pageNo> value into the index page,
   *@key  the key value in <key, pageNO>. Input parameter. 
   *@pageNo the pageNo  in <key, pageNO>. Input parameter.
   *@return It returns the rid where the record is inserted;
   null if no space left.
   *@exception IndexInsertRecException error when insert
   */
   public I insertKey(KeyClass key, PageId pageNo)
      throws  IndexInsertRecException
    {
      I rid;
      IKeyDataEntry<I> entry;
      try {
        entry = getKeyDataEntrySupplier().getKeyDataEntry( key, pageNo);
        rid=super.insertRecord( entry );
        return rid;
      }
      catch ( Exception e) {       
        throw new IndexInsertRecException(e, "Insert failed");
        
      }
    }
  
  /*  OPTIONAL: fullDeletekey 
   * This is optional, and is only needed if you want to do full deletion.
   * Return its RID.  delete key may != key.  But delete key <= key,
   * and the delete key is the first biggest key such that delete key <= key 
   *@param key the key used to search. Input parameter.
   *@exception IndexFullDeleteException if no record deleted or failed by
   * any reason
   *@return  RID of the record deleted. Can not return null.
   */
  protected I deleteKey(KeyClass key)
    throws IndexFullDeleteException 
    {
      IKeyDataEntry<I>  entry;
      I rid=getID();
      
      
      try {
	
	entry = getFirst(rid);
	
	if (entry == null) 
	  //it is supposed there is at least a record
	  throw new IndexFullDeleteException(null, "No records found");
	
	
	if ( BTI.keyCompare(key, entry.key)<0 )
	  //it is supposed to not smaller than first key
	  throw new IndexFullDeleteException(null, "First key is bigger");
	
	
	while (BTI.keyCompare(key, entry.key) > 0) {
	  entry = getNext(rid );
	  if (entry == null)
            break;
	}
	
	if (entry == null || BTI.keyCompare(key, entry.key) != 0) rid.setSlotNo(rid.getSlotNo()-1);
	else if (BTI.keyCompare(key, entry.key) != 0)
      rid.setSlotNo(rid.getSlotNo()-1); // we want to delete the previous key
	
	deleteSortedRecord(rid);
	return rid;
      }
      catch (Exception e) {
        throw new IndexFullDeleteException(e, "Full delelte failed"); 
      }
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
      IKeyDataEntry<I> entry;
      int i;
      
      try {
	
	for (i=getSlotCnt()-1; i >= 0; i--) {
	  entry= BTI.getEntryFromBytes( getpage(),getSlotOffset(i),
				       getSlotLength(i), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	  
	  if (BTI.keyCompare(key, entry.key) >= 0)
	    {
	      return ((IndexData)entry.data).getData();
	    }
	}
	
	return getPrevPage();
      } 
      catch (Exception e) {
	throw new IndexSearchException(e, "Get entry failed");
      }   
      
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
  public IKeyDataEntry<I> getFirst(I rid)
    throws IteratorException
    {
      
      IKeyDataEntry<I>  entry;
      
      try { 
	rid.setPageNo(getCurPage());
	rid.setSlotNo(0); // begin with first slot
	
	if ( getSlotCnt() == 0) {
	  return null;
	}
	
	entry=BTI.getEntryFromBytes( getpage(),getSlotOffset(0),
				    getSlotLength(0),
				    keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	
	return entry;
      } 
      catch (Exception e) {
	throw new IteratorException(e, "Get first entry failed");
      }
      
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
	
	entry=BTI.getEntryFromBytes(getpage(),getSlotOffset(i),
				   getSlotLength(i),
				   keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	
	return entry;
      } 
      catch (Exception e) {
	throw new IteratorException(e, "Get next entry failed");
      }
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
      return getPrevPage(); 
    }
  
  /** You will recall that the index pages have a left-most
   *  pointer that is followed whenever the search key value
   *  is less than the least key value in the index node. The
   *  previous page pointer is used to implement the left link.
   * The function sets the left link.
   *@param left the PageId of the left link you wish to set. Input parameter.
   *@exception IOException I/O errors
   */ 
  void setLeftLink(PageId left)
    throws IOException
    { 
      setPrevPage(left); 
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
      
      try {
	if (getSlotCnt() == 0) // there is no sibling 
          return 0;
	
	int i;
	IKeyDataEntry<I> entry;
	for (i=getSlotCnt()-1; i >= 0; i--) {
	  entry=BTI.getEntryFromBytes(getpage(), getSlotOffset(i),
				     getSlotLength(i), keyType , NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	  if (BTI.keyCompare(key, entry.key)>=0) {
	    if (i != 0) { 
              entry=BTI.getEntryFromBytes(getpage(), getSlotOffset(i-1),
					 getSlotLength(i-1), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
              pageNo.pid=  ((IndexData)entry.data).getData().pid;
              return -1; //left sibling
	    }
	    else {
              pageNo.pid = getLeftLink().pid;
              return -1; //left sibling
	    }
	  }
	}
	entry=BTI.getEntryFromBytes(getpage(), getSlotOffset(0),
				   getSlotLength(0), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	pageNo.pid=  ((IndexData)entry.data).getData().pid; 
	return 1;  //right sibling
      }
      catch (Exception e) {
	throw new IndexFullDeleteException(e, "Get sibling failed");
      }
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
      
      try {
        
	IKeyDataEntry<I> entry;
	entry =  findKeyData( oldKey );
	if (entry == null) return false;
	
	I rid=deleteKey( entry.key );
	if (rid==null) throw new IndexFullDeleteException(null, "Rid is null");
	
	rid=insertKey( newKey, ((IndexData)entry.data).getData());        
	if (rid==null) throw new IndexFullDeleteException(null, "Rid is null");
	
	return true;
      }
      catch (Exception e) {
        throw new IndexFullDeleteException(e, "Adjust key failed");
      }
    } // end of adjustKey
  
  
  /* find entry for key by B+ tree algorithm, 
   * but entry.key may not equal KeyDataEntry.key returned.
   *@param key input parameter.
   *@return return that entry if found; otherwise return null;
   *@exception  IndexSearchException index search failed
   * 
   */
  IKeyDataEntry<I> findKeyData(KeyClass key)
    throws IndexSearchException
    {
      IKeyDataEntry<I> entry;
      
      try {  
	
        for (int i = getSlotCnt()-1; i >= 0; i--) {
	  entry=BTI.getEntryFromBytes(getpage(),getSlotOffset(i),
				     getSlotLength(i), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	  
	  if (BTI.keyCompare(key, entry.key) >= 0) {
	    return entry;
	  }
        }
        return null;
      }
      catch ( Exception e) {
        throw  new IndexSearchException(e, "finger key data failed");
      }
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
      return findKeyData(key).key;
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
  protected boolean redistribute(BTIndexPageI<I,T> indexPage, BTIndexPageI<I,T> parentIndexPage,
                       int direction, KeyClass deletedKey)
    throws RedistributeException
    {
      
      // assertion: indexPage and parentIndexPage are  pinned
      try {  
	boolean st;
	if (direction==-1) { // 'this' is the left sibling of indexPage
	  if (( getSlotLength(getSlotCnt()-1) + available_space()) > 
	      ( (MAX_SPACE-DPFIXED)/2) ) {
            // cannot spare a record for its underflow sibling
            return false;
	  }
	  else {
            // get its sibling's first record's key 
            I dummyRid=getIdSupplier().getID();
            IKeyDataEntry<I> firstEntry, lastEntry;
            firstEntry=indexPage.getFirst(dummyRid);
            
            // get the entry pointing to the right sibling
	    
            KeyClass splitKey = parentIndexPage.findKey(firstEntry.key);
	    
            // get the leftmost child pointer of the right sibling
            PageId leftMostPageId = indexPage.getLeftLink();
            
            // insert  <splitKey,leftMostPageId>  to its sibling
            indexPage.insertKey( splitKey, leftMostPageId);

            // get the last record of itself
            lastEntry=BTI.getEntryFromBytes(getpage(), getSlotOffset(getSlotCnt()-1),
					   getSlotLength(getSlotCnt()-1), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	    
            // set sibling's leftmostchild to be lastPageId
            indexPage.setLeftLink(((IndexData)(lastEntry.data)).getData() );
	    
            // delete the last record from the old page
            I delRid=getIdSupplier().getID();
            delRid.setPageNo(getCurPage());
            delRid.setSlotNo(getSlotCnt()-1);

            if ( deleteSortedRecord(delRid) ==false )
                   throw new RedistributeException(null, "Delete record failed");

            // adjust the entry pointing to sibling in its parent
            if (deletedKey!=null )
                st = parentIndexPage.adjustKey( lastEntry.key, deletedKey);
	    
            else 
	      st = parentIndexPage.adjustKey( lastEntry.key, splitKey);
            if (st==false)
	      throw new RedistributeException(null, "adjust key failed");
            return true;
	  }
	}
	else { // 'this' is the right sibling of indexPage
	  if ( (getSlotLength(0) + available_space()) > ((MAX_SPACE-DPFIXED)/2) ) {
            // cannot spare a record for its underflow sibling
            return false;
	  }
	  else {
            // get the first record
            IKeyDataEntry<I> firstEntry;
            firstEntry=BTI.getEntryFromBytes( getpage(),
					     getSlotOffset(0),
					     getSlotLength(0), keyType, NodeType.INDEX,getIdSupplier(),getLeafDataSupplier(),getKeyDataEntrySupplier());
	    
            // get its leftmost child pointer
            PageId leftMostPageId = getLeftLink();
	    
            // get the entry in its parent pointing to itself
            KeyClass splitKey;
            splitKey = parentIndexPage.findKey(firstEntry.key);
	    
            // insert <split, leftMostPageId> to its left sibling 
	    
            indexPage.insertKey(splitKey,leftMostPageId);
            
	    
            // set its new leftmostchild
            setLeftLink(((IndexData)(firstEntry.data)).getData());
            
            // delete the first record 
            I delRid=getIdSupplier().getID();
            delRid.setPageNo(getCurPage());
            delRid.setSlotNo(0);
            if (deleteSortedRecord(delRid) == false )
	      throw new RedistributeException(null, "delete record failed");
	    
            // adjust the entry pointing to itself in its parent
            if ( parentIndexPage.adjustKey(firstEntry.key, splitKey) ==false )
	      throw new RedistributeException(null, "adjust key failed");  
            return true;
	  }
	} //else
      } //try
      catch (Exception e){
	throw new RedistributeException(e, "redistribute failed");
      } 
    } // end of redistribute  
};
