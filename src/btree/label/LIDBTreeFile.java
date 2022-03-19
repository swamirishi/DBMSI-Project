/*
 * @(#) bt.java   98/03/24
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu).
 *
 */

package btree.label;

import btree.*;
import btree.interfaces.BTreeFileI;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.LID;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;
import utils.supplier.btfilescan.BTFileScanSupplier;
import utils.supplier.btfilescan.LIDBTFileScanSupplier;
import utils.supplier.btfilescan.QIDBTFileScanSupplier;
import utils.supplier.btheaderpage.BTreeHeaderPageSupplier;
import utils.supplier.btheaderpage.LIDBTreeHeaderPageSupplier;
import utils.supplier.btheaderpage.QIDBTreeHeaderPageSupplier;
import utils.supplier.btindexpage.BTIndexPageSupplier;
import utils.supplier.btindexpage.LIDBTIndexPageSupplier;
import utils.supplier.btindexpage.QIDBTIndexPageSupplier;
import utils.supplier.btleafpage.BTLeafPageSupplier;
import utils.supplier.btleafpage.LIDBTLeafPageSupplier;
import utils.supplier.btleafpage.QIDBTLeafPageSupplier;
import utils.supplier.btsortedpage.BTSortedPageSupplier;
import utils.supplier.btsortedpage.LIDBTSortedPageSupplier;
import utils.supplier.btsortedpage.QIDBTSortedPageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.LIDSupplier;
import utils.supplier.id.QIDSupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.keydataentry.LIDKeyDataEntrySupplier;
import utils.supplier.keydataentry.QIDKeyDataEntrySupplier;
import utils.supplier.leafdata.LIDLeafDataSupplier;
import utils.supplier.leafdata.LeafDataSupplier;
import utils.supplier.leafdata.QIDLeafDataSupplier;

import java.io.IOException;

/** btfile.java
 * This is the main definition of class BTreeFile, which derives from 
 * abstract base class IndexFile.
 * It provides an insert/delete interface.
 */
public class LIDBTreeFile extends BTreeFileI<LID, Label> {
	@Override
	public BTreeHeaderPageSupplier<LID, Label> getBTreeHeaderPageSupplier() {
		return LIDBTreeHeaderPageSupplier.getSupplier();
	}
	
	@Override
	public BTSortedPageSupplier<LID, Label> getBTSortedPageSupplier() {
		return LIDBTSortedPageSupplier.getSupplier();
	}
	
	@Override
	public BTIndexPageSupplier<LID, Label> getBTIndexPageSupplier() {
		return LIDBTIndexPageSupplier.getSupplier();
	}
	
	@Override
	public BTLeafPageSupplier<LID, Label> getBTLeafPageSupplier() {
		return LIDBTLeafPageSupplier.getSupplier();
	}
	
	@Override
	public IDSupplier<LID> getIDSupplier() {
		return LIDSupplier.getSupplier();
	}
	
	@Override
	public LeafDataSupplier<LID> getLeafDataSupplier() {
		return LIDLeafDataSupplier.getSupplier();
	}
	
	@Override
	public KeyDataEntrySupplier<LID> getKeyDataEntrySupplier() {
		return LIDKeyDataEntrySupplier.getSupplier();
	}
	
	@Override
	public BTFileScanSupplier<LID, Label> getBTFileScanSupplier() {
		return LIDBTFileScanSupplier.getSupplier();
	}
	
	/**
   * Access method to data member.
   * @return  Return a BTreeHeaderPage object that is the header page
   *          of this btree file.
   */
  public LIDBTreeHeaderPage getHeaderPage() {
    return (LIDBTreeHeaderPage) super.getHeaderPage();
  }
//  //TODO: Protected Check
//  protected PageId get_file_entry(String filename)
//    throws GetFileEntryException
//    {
//      return super.get_file_entry(filename);
//    }
//
//
//  //TODO: Protected Check
//  protected Page pinPage(PageId pageno)
//    throws PinPageException
//    {
//      return super.pinPage(pageno);
//    }
//
//  private void add_file_entry(String fileName, PageId pageno)
//    throws AddFileEntryException
//    {
//      try {
//        SystemDefs.JavabaseDB.add_file_entry(fileName, pageno);
//      }
//      catch (Exception e) {
//	e.printStackTrace();
//	throw new AddFileEntryException(e,"");
//      }
//    }

//  private void unpinPage(PageId pageno)
//    throws UnpinPageException
//    {
//      super.unpinPage(pageno);
//    }
//
//  private void freePage(PageId pageno)
//    throws FreePageException
//    {
//      super.freePage(pageno);
//
//    }
//  private void delete_file_entry(String filename)
//    throws DeleteFileEntryException
//    {
//
//      super.delete_file_entry(filename);
//    }
//
//  private void unpinPage(PageId pageno, boolean dirty)
//    throws UnpinPageException
//    {
//        super.unpinPage(pageno, dirty);
//    }
//
  
  
  
  /**  BTreeFile class
   * an index file with given filename should already exist; this opens it.
   *@param filename the B+ tree file name. Input parameter.
   *@exception GetFileEntryException  can not ger the file from DB
   *@exception PinPageException  failed when pin a page
   *@exception ConstructPageException   BT page constructor failed
   */
  public LIDBTreeFile(String filename)
    throws GetFileEntryException,
	   PinPageException,
	   ConstructPageException
    {
      super(filename);
    }
  
  
  /**
   *  if index file exists, open it; else create it.
   *@param filename file name. Input parameter.
   *@param keytype the type of key. Input parameter.
   *@param keysize the maximum size of a key. Input parameter.
   *@param delete_fashion full delete or naive delete. Input parameter.
   *           It is either DeleteFashion.NAIVE_DELETE or
   *           DeleteFashion.FULL_DELETE.
   *@exception GetFileEntryException  can not get file
   *@exception ConstructPageException page constructor failed
   *@exception IOException error from lower layer
   *@exception AddFileEntryException can not add file into DB
   */
  public LIDBTreeFile(String filename, int keytype,
                      int keysize, int delete_fashion)
    throws GetFileEntryException,
	   ConstructPageException,
	   IOException,
	   AddFileEntryException
    {
      super(filename, keytype, keysize, delete_fashion);
      
    }
  
  /** Close the B+ tree file.  Unpin header page.
   *@exception PageUnpinnedException  error from the lower layer
   *@exception InvalidFrameNumberException  error from the lower layer
   *@exception HashEntryNotFoundException  error from the lower layer
   *@exception ReplacerException  error from the lower layer
   */
  public void close()
    throws PageUnpinnedException,
	   InvalidFrameNumberException,
	   HashEntryNotFoundException,
           ReplacerException
    {
      super.close();
    }
  
  /** Destroy entire B+ tree file.
   *@exception IOException  error from the lower layer
   *@exception IteratorException iterator error
   *@exception UnpinPageException error  when unpin a page
   *@exception FreePageException error when free a page
   *@exception DeleteFileEntryException failed when delete a file from DM
   *@exception ConstructPageException error in BT page constructor
   *@exception PinPageException failed when pin a page
   */
  public void destroyFile()
    throws IOException,
	   IteratorException,
	   UnpinPageException,
	   FreePageException,
	   DeleteFileEntryException,
	   ConstructPageException,
	   PinPageException
    {
      super.destroyFile();
    }
  
  
//  private void  _destroyFile(PageId pageno)
//    throws IOException,
//	   IteratorException,
//	   PinPageException,
//           ConstructPageException,
//	   UnpinPageException,
//	   FreePageException
//    {
//
//      super._destroyFile(pageno);
//
//    }
//
//  private void  updateHeader(PageId newRoot)
//    throws   IOException,
//	     PinPageException,
//	     UnpinPageException
//    {
//
//      super.updateHeader(newRoot);
//
//    }
//
  
  /** insert record with the given key and rid
   *@param key the key of the record. Input parameter.
   *@param rid the rid of the record. Input parameter.
   *@exception  KeyTooLongException key size exceeds the max keysize.
   *@exception KeyNotMatchException key is not integer key nor string key
   *@exception IOException error from the lower layer
   *@exception LeafInsertRecException insert error in leaf page
   *@exception IndexInsertRecException insert error in index page
   *@exception ConstructPageException error in BT page constructor
   *@exception UnpinPageException error when unpin a page
   *@exception PinPageException error when pin a page
   *@exception NodeNotMatchException  node not match index page nor leaf page
   *@exception ConvertException error when convert between revord and byte
   *             array
   *@exception DeleteRecException error when delete in index page
   *@exception IndexSearchException error when search
   *@exception IteratorException iterator error
   *@exception LeafDeleteException error when delete in leaf page
   *@exception InsertException  error when insert in index page
   */
  public void insert(KeyClass key, LID rid)
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
	   IOException
	   
    {
      super.insert(key, rid);
    }
  
  
  
  
//  private KeyDataEntry  _insert(KeyClass key, LID rid,
//				PageId currentPageId)
//    throws  PinPageException,
//	    IOException,
//	    ConstructPageException,
//	    LeafDeleteException,
//	    ConstructPageException,
//	    DeleteRecException,
//	    IndexSearchException,
//	    UnpinPageException,
//	    LeafInsertRecException,
//	    ConvertException,
//	    IteratorException,
//	    IndexInsertRecException,
//	    KeyNotMatchException,
//	    NodeNotMatchException,
//	    InsertException
//
//    {
//
//
//      return super._insert(key, rid, currentPageId);
//    }
  
  
  
  
  /** delete leaf entry  given its <key, rid> pair.
   *  `rid' is IN the data entry; it is not the id of the data entry)
   *@param key the key in pair <key, rid>. Input Parameter.
   *@param rid the rid in pair <key, rid>. Input Parameter.
   *@return true if deleted. false if no such record.
   *@exception DeleteFashionException neither full delete nor naive delete
   *@exception LeafRedistributeException redistribution error in leaf pages
   *@exception RedistributeException redistribution error in index pages
   *@exception InsertRecException error when insert in index page
   *@exception KeyNotMatchException key is neither integer key nor string key
   *@exception UnpinPageException error when unpin a page
   *@exception IndexInsertRecException  error when insert in index page
   *@exception FreePageException error in BT page constructor
   *@exception RecordNotFoundException error delete a record in a BT page
   *@exception PinPageException error when pin a page
   *@exception IndexFullDeleteException  fill delete error
   *@exception LeafDeleteException delete error in leaf page
   *@exception IteratorException iterator error
   *@exception ConstructPageException error in BT page constructor
   *@exception DeleteRecException error when delete in index page
   *@exception IndexSearchException error in search in index pages
   *@exception IOException error from the lower layer
   *
   */
  public boolean Delete(KeyClass key, LID rid)
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
	    IOException
    {
      return super.Delete(key, rid);
    }
  
  
  
  
  /*
   * findRunStart.
   * Status BTreeFile::findRunStart (const void   lo_key,
   *                                LID          *pstartrid)
   *
   * find left-most occurrence of `lo_key', going all the way left if
   * lo_key is null.
   *
   * Starting record returned in *pstartrid, on page *pppage, which is pinned.
   *
   * Since we allow duplicates, this must "go left" as described in the text
   * (for the search algorithm).
   *@param lo_key  find left-most occurrence of `lo_key', going all
   *               the way left if lo_key is null.
   *@param startrid it will reurn the first rid =< lo_key
   *@return return a BTLeafPage instance which is pinned.
   *        null if no key was found.
   */
  
//  BTLeafPage findRunStart (KeyClass lo_key,
//			   LID startrid)
//    throws IOException,
//	   IteratorException,
//	   KeyNotMatchException,
//	   ConstructPageException,
//	   PinPageException,
//	   UnpinPageException
//    {
//      return super.findRunStart(lo_key, startrid);
//    }
//
  
  
  /*
   *  Status BTreeFile::NaiveDelete (const void *key, const LID rid)
   *
   * Remove specified data entry (<key, rid>) from an index.
   *
   * We don't do merging or redistribution, but do allow duplicates.
   *
   * Page containing first occurrence of key `key' is found for us
   * by findRunStart.  We then iterate for (just a few) pages, if necesary,
   * to find the one containing <key,rid>, which we then delete via
   * BTLeafPage::delUserRid.
   */
  
//  private boolean NaiveDelete ( KeyClass key, LID rid)
//    throws LeafDeleteException,
//	   KeyNotMatchException,
//	   PinPageException,
//	   ConstructPageException,
//	   IOException,
//	   UnpinPageException,
//	   PinPageException,
//	   IndexSearchException,
//	   IteratorException
//    {
//      return super.NaiveDelete(key, rid);
//    }

  
  /*
   * Status BTreeFile::FullDelete (const void *key, const LID rid)
   * 
   * Remove specified data entry (<key, rid>) from an index.
   *
   * Most work done recursively by _Delete
   *
   * Special case: delete root if the tree is empty
   *
   * Page containing first occurrence of key `key' is found for us
   * After the page containing first occurence of key 'key' is found,
   * we iterate for (just a few) pages, if necesary,
   * to find the one containing <key,rid>, which we then delete via
   * BTLeafPage::delUserRid.
   *@return false if no such record; true if succees 
   */
  
//  private boolean FullDelete (KeyClass key,  LID rid)
//    throws IndexInsertRecException,
//	   RedistributeException,
//	   IndexSearchException,
//	   RecordNotFoundException,
//	   DeleteRecException,
//	   InsertRecException,
//	   LeafRedistributeException,
//	   IndexFullDeleteException,
//	   FreePageException,
//	   LeafDeleteException,
//	   KeyNotMatchException,
//	   ConstructPageException,
//	   IOException,
//	   IteratorException,
//	   PinPageException,
//	   UnpinPageException,
//	   IteratorException
//    {
//
//      return super.FullDelete(key, rid);
//    }
//
//  private KeyClass _Delete ( KeyClass key,
//			     LID     rid,
//			     PageId        currentPageId,
//			     PageId        parentPageId)
//    throws IndexInsertRecException,
//	   RedistributeException,
//	   IndexSearchException,
//	   RecordNotFoundException,
//	   DeleteRecException,
//	   InsertRecException,
//	   LeafRedistributeException,
//	   IndexFullDeleteException,
//	   FreePageException,
//	   LeafDeleteException,
//	   KeyNotMatchException,
//	   ConstructPageException,
//	   UnpinPageException,
//	   IteratorException,
//	   PinPageException,
//	   IOException
//    {
//
//      super._Delete(key, rid, currentPageId, parentPageId);
//
//    }
  
  
  
  /** create a scan with given keys
   * Cases:
   *      (1) lo_key = null, hi_key = null
   *              scan the whole index
   *      (2) lo_key = null, hi_key!= null
   *              range scan from min to the hi_key
   *      (3) lo_key!= null, hi_key = null
   *              range scan from the lo_key to max
   *      (4) lo_key!= null, hi_key!= null, lo_key = hi_key
   *              exact match ( might not unique)
   *      (5) lo_key!= null, hi_key!= null, lo_key < hi_key
   *              range scan from lo_key to hi_key
   *@param lo_key the key where we begin scanning. Input parameter.
   *@param hi_key the key where we stop scanning. Input parameter.
   *@exception IOException error from the lower layer
   *@exception KeyNotMatchException key is not integer key nor string key
   *@exception IteratorException iterator error
   *@exception ConstructPageException error in BT page constructor
   *@exception PinPageException error when pin a page
   *@exception UnpinPageException error when unpin a page
   */
  public LIDBTFileScan new_scan(KeyClass lo_key, KeyClass hi_key)
    throws IOException,  
	   KeyNotMatchException, 
	   IteratorException, 
	   ConstructPageException, 
	   PinPageException, 
	   UnpinPageException
	   
    {
      return (LIDBTFileScan) super.new_scan(lo_key, hi_key);
    }
  
}


