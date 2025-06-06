/*
 * @(#) BT.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */
package btree.interfaces;

import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import diskmgr.Page;
import global.*;
import heap.Tuple;
import utils.supplier.btindexpage.BTIndexPageSupplier;
import utils.supplier.btleafpage.BTLeafPageSupplier;
import utils.supplier.btsortedpage.BTSortedPageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.keydataentry.KeyDataEntrySupplier;
import utils.supplier.leafdata.LeafDataSupplier;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**  
 * This file contains, among some debug utilities, the interface to our
 * key and data abstraction.  The BTLeafPage and BTIndexPage code
 * know very little about the various key types.  
 *
 * Essentially we provide a way for those classes to package and 
 * unpackage <key,data> pairs in a space-efficient manner.  That is, the 
 * packaged result contains the key followed immediately by the data value; 
 * No padding bytes are used (not even for alignment). 
 *
 * Furthermore, the BT<*>Page classes need
 * not know anything about the possible AttrType values, since all 
 * their processing of <key,data> pairs is done by the functions 
 * declared here.
 *
 * In addition to packaging/unpacking of <key,value> pairs, we 
 * provide a keyCompare function for the (obvious) purpose of
 * comparing keys of arbitrary type (as long as they are defined 
 * here).
 *
 * For debug utilities, we provide some methods to print any page
 * or the whole B+ tree structure or all leaf pages.
 *
 */
public abstract class BTI implements GlobalConst{
  
  /** It compares two keys.
   *@param  key1  the first key to compare. Input parameter.
   *@param  key2  the second key to compare. Input parameter.
   *@return return negative if key1 less than key2; positive if key1 bigger
   * than  key2; 
   * 0 if key1=key2.
   *@exception KeyNotMatchException key is not IntegerKey or StringKey class
   */  
  public final static int keyCompare(KeyClass key1, KeyClass key2)
    throws KeyNotMatchException
    {
      if ((key1 instanceof IntegerKey) && (key2 instanceof IntegerKey) ) {
	
	return  (((IntegerKey)key1).getKey()).intValue() 
	  - (((IntegerKey)key2).getKey()).intValue();
      }
      else if  ((key1 instanceof StringKey) && (key2 instanceof StringKey)){
        return ((StringKey)key1).getKey().compareTo(((StringKey)key2).getKey());
      }
      else { throw new  KeyNotMatchException(null, "key types do not match");}
    } 
  
  
  /** It gets the length of the key
   *@param key  specify the key whose length will be calculated.
   * Input parameter.
   *@return return the length of the key
   *@exception  KeyNotMatchException  key is neither StringKey nor  IntegerKey 
   *@exception IOException   error  from the lower layer  
   */  
  protected final static int getKeyLength(KeyClass key) 
    throws  KeyNotMatchException, 
	    IOException
    {
      if ( key instanceof StringKey) {
	
	OutputStream out = new ByteArrayOutputStream();
	DataOutputStream outstr = new DataOutputStream (out);
	outstr.writeUTF(((StringKey)key).getKey()); 
	return  outstr.size();
    }
      else if ( key instanceof IntegerKey)
	return 4;
      else throw new KeyNotMatchException(null, "key types do not match"); 
    }
  
  
  /** It gets the length of the data 
   *@param  pageType  NodeType.LEAF or  NodeType.INDEX. Input parameter.
   *@return return 8 if it is of NodeType.LEA; 
   *  return 4 if it is of NodeType.INDEX.
   *@exception NodeNotMatchException pageType is neither NodeType.LEAF
   *  nor NodeType.INDEX.
   */  
  protected final static int getDataLength(short pageType) 
    throws  NodeNotMatchException
    {
      if ( pageType==NodeType.LEAF)
	return 8;
      else if ( pageType==NodeType.INDEX)
	return 4;
      else throw new  NodeNotMatchException(null, "key types do not match"); 
    }
  
  /** It gets the length of the (key,data) pair in leaf or index page. 
   *@param  key    an object of KeyClass.  Input parameter.
   *@param  pageType  NodeType.LEAF or  NodeType.INDEX. Input parameter.
   *@return return the lenrth of the (key,data) pair.
   *@exception  KeyNotMatchException key is neither StringKey nor  IntegerKey 
   *@exception NodeNotMatchException pageType is neither NodeType.LEAF 
   *  nor NodeType.INDEX.
   *@exception IOException  error from the lower layer 
   */ 
  protected final static int getKeyDataLength(KeyClass key, short pageType ) 
    throws KeyNotMatchException, 
	   NodeNotMatchException, 
	   IOException
    {
      return getKeyLength(key) + getDataLength(pageType);
    } 
  
  /** It gets an keyDataEntry from bytes array and position
   *@param from  It's a bytes array where KeyDataEntry will come from. 
   * Input parameter.
   *@param offset the offset in the bytes. Input parameter.
   *@param keyType It specifies the type of key. It can be 
   *               AttrType.attrString or AttrType.attrInteger.
   *               Input parameter. 
   *@param nodeType It specifes NodeType.LEAF or NodeType.INDEX. 
   *                Input parameter.
   *@param length  The length of (key, data) in byte array "from".
   *               Input parameter.
   *@return return a KeyDataEntry object
   *@exception KeyNotMatchException  key is neither StringKey nor  IntegerKey
   *@exception NodeNotMatchException  nodeType is neither NodeType.LEAF 
   *  nor NodeType.INDEX.
   *@exception ConvertException  error from the lower layer 
   */
  protected final static <I extends ID>
      IKeyDataEntry<I> getEntryFromBytes(byte[] from, int offset,
                                     int length, int keyType, short nodeType, IDSupplier<I> idSupplier, LeafDataSupplier<I> leafDataSupplier, KeyDataEntrySupplier<I> keyDataEntrySupplier)
    throws KeyNotMatchException, 
	   NodeNotMatchException, 
	   ConvertException
    {
      KeyClass key;
      DataClass data;
      int n;
      try {
	
	if ( nodeType==NodeType.INDEX ) {
	  n=4;
	  data= new IndexData( Convert.getIntValue(offset+length-4, from));
	}
	else if ( nodeType==NodeType.LEAF) {
	  n=8;
	  
	  I rid=idSupplier.getID();
	  rid.setSlotNo(Convert.getIntValue(offset+length-8, from));
	  rid.setPageNo(new PageId());
	  rid.getPageNo().pid= Convert.getIntValue(offset+length-4, from);
	  data = leafDataSupplier.getLeafData(rid);
	}
	else throw new NodeNotMatchException(null, "node types do not match"); 
	
	if ( keyType== AttrType.attrInteger) {
	  key= new IntegerKey( new Integer 
			       (Convert.getIntValue(offset, from)));
	}
	else if (keyType== AttrType.attrString) {
	  //System.out.println(" offset  "+ offset + "  " + length + "  "+n);
          key= new StringKey( Convert.getStrValue(offset, from, length-n));
	} 
	else 
          throw new KeyNotMatchException(null, "key types do not match");
	
	return keyDataEntrySupplier.getKeyDataEntry(key, data);
	
      } 
      catch ( IOException e) {
	throw new ConvertException(e, "convert faile");
      }
    } 
  
  
  /** It convert a keyDataEntry to byte[].
   *@param  entry specify  the data entry. Input parameter.
   *@return return a byte array with size equal to the size of (key,data). 
   *@exception   KeyNotMatchException  entry.key is neither StringKey nor  IntegerKey
   *@exception NodeNotMatchException entry.data is neither LeafData nor IndexData
   *@exception ConvertException error from the lower layer
   */
  protected final static <I extends ID> byte[] getBytesFromEntry( IKeyDataEntry<I> entry )
    throws KeyNotMatchException, 
	   NodeNotMatchException, 
	   ConvertException
    {
      byte[] data;
      int n, m;
      try{
        n=getKeyLength(entry.key);
        m=n;
        if( entry.data instanceof IndexData )
	  n+=4;
        else if (entry.data instanceof ILeafData )
	  n+=8;
	
        data=new byte[n];
	
        if ( entry.key instanceof IntegerKey ) {
	  Convert.setIntValue( ((IntegerKey)entry.key).getKey().intValue(),
			       0, data);
        }
        else if ( entry.key instanceof StringKey ) {
	  Convert.setStrValue( ((StringKey)entry.key).getKey(),
			       0, data);            
        }
        else throw new KeyNotMatchException(null, "key types do not match");
        
        if ( entry.data instanceof IndexData ) {
	  Convert.setIntValue( ((IndexData)entry.data).getData().pid,
			       m, data);
        }
        else if ( entry.data instanceof ILeafData ) {
	  Convert.setIntValue( ((ILeafData)entry.data).getData().getSlotNo(),
			       m, data);
	  Convert.setIntValue( ((ILeafData)entry.data).getData().getPageNo().pid,
			       m+4, data);
	  
        }
        else throw new NodeNotMatchException(null, "node types do not match");
        return data;
      } 
      catch (IOException e) {
        throw new  ConvertException(e, "convert failed");
      }
    } 
  
  /** 
   * used for debug: to print a page out. The page is either BTIndexPage,
   * or BTLeafPage. 
   *@param pageno the number of page. Input parameter.
   *@param keyType It specifies the type of key. It can be 
   *               AttrType.attrString or AttrType.attrInteger.
   *               Input parameter. 
   *@exception IOException error from the lower layer
   *@exception IteratorException  error for iterator
   *@exception ConstructPageException  error for BT page constructor
   *@exception HashEntryNotFoundException error from the lower layer
   *@exception ReplacerException error from the lower layer
   *@exception PageUnpinnedException error from the lower layer
   *@exception InvalidFrameNumberException error from the lower layer
   */
  
  public static <I extends ID,T extends Tuple>void printPage(PageId pageno, int keyType, IDSupplier<I> idSupplier, KeyDataEntrySupplier<I> keyDataEntrySupplier, BTSortedPageSupplier<I,T> btSortedPageSupplier, BTIndexPageSupplier<I,T> btIndexPageSupplier, BTLeafPageSupplier<I,T> btLeafPageSupplier)
    throws  IOException, 
	    IteratorException, 
	    ConstructPageException,
            HashEntryNotFoundException, 
	    ReplacerException, 
	    PageUnpinnedException, 
            InvalidFrameNumberException
    {
      BTSortedPageI<I,T> sortedPage=btSortedPageSupplier.getBTSortedPage(pageno, keyType);
      int i;
      i=0;
      if ( sortedPage.getType()==NodeType.INDEX ) {
        BTIndexPageI<I,T> indexPage=btIndexPageSupplier.getBTIndexPage(sortedPage, keyType);
        System.out.println("");
        System.out.println("**************To Print an Index Page ********");
        System.out.println("Current Page ID: "+ indexPage.getCurPage().pid);
        System.out.println("Left Link      : "+ indexPage.getLeftLink().pid);
	
        I rid= idSupplier.getID();
	
        for(IKeyDataEntry<I> entry=indexPage.getFirst(rid); entry!=null;
	    entry=indexPage.getNext(rid)){
	  if( keyType==AttrType.attrInteger) 
	    System.out.println(i+" (key, pageId):   ("+ 
			       (IntegerKey)entry.key + ",  "+(IndexData)entry.data+ " )");
	  if( keyType==AttrType.attrString) 
	    System.out.println(i+" (key, pageId):   ("+ 
			       (StringKey)entry.key + ",  "+(IndexData)entry.data+ " )");
	  
	  i++;    
        }
	
        System.out.println("************** END ********");
        System.out.println("");
      }
      else if ( sortedPage.getType()==NodeType.LEAF ) {
        BTLeafPageI<I,T> leafPage=btLeafPageSupplier.getBTLeafPage(sortedPage,keyType);
        System.out.println("");
        System.out.println("**************To Print an Leaf Page ********");
        System.out.println("Current Page ID: "+ leafPage.getCurPage().pid);
        System.out.println("Left Link      : "+ leafPage.getPrevPage().pid);
        System.out.println("Right Link     : "+ leafPage.getNextPage().pid);
	
        I rid= idSupplier.getID();
	
        for(IKeyDataEntry<I> entry=leafPage.getFirst(rid); entry!=null;
	    entry=leafPage.getNext(rid)){
	  if( keyType==AttrType.attrInteger) 
	    System.out.println(i+" (key, [pageNo, slotNo]):   ("+ 
			       (IntegerKey)entry.key+ ",  "+(ILeafData)entry.data+ " )");
	  if( keyType==AttrType.attrString) 
	    System.out.println(i+" (key, [pageNo, slotNo]):   ("+ 
			       (StringKey)entry.key + ",  "+(ILeafData)entry.data);
	  
	  i++;
        }
	
	System.out.println("************** END ********");
	System.out.println("");
      }
      else {
	System.out.println("Sorry!!! This page is neither Index nor Leaf page.");
      }      
      
      SystemDefs.JavabaseBM.unpinPage(pageno, true/*dirty*/);
    }
  
  
  /** For debug. Print the B+ tree structure out
   *@param header  the head page of the B+ tree file
   *@exception IOException error from the lower layer
   *@exception ConstructPageException  error from BT page constructor
   *@exception IteratorException  error from iterator
   *@exception HashEntryNotFoundException  error from lower layer
   *@exception InvalidFrameNumberException  error from lower layer
   *@exception PageUnpinnedException  error from lower layer
   *@exception ReplacerException  error from lower layer
   */
  public static <I extends ID,T extends Tuple> void printBTree(BTreeHeaderPageI<I,T> header,IDSupplier<I> idSupplier,BTSortedPageSupplier<I,T> btSortedPageSupplier,BTIndexPageSupplier<I,T> btIndexPageSupplier)
    throws IOException, 
	   ConstructPageException, 
	   IteratorException,
	   HashEntryNotFoundException,
	   InvalidFrameNumberException,
	   PageUnpinnedException,
	   ReplacerException 
    {
      if(header.get_rootId().pid == INVALID_PAGE) {
	System.out.println("The Tree is Empty!!!");
	return;
      }
      
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("---------------The B+ Tree Structure---------------");
      
      
      System.out.println(1+ "     "+header.get_rootId());
      
      _printTree(header.get_rootId(), "     ", 1, header.get_keyType(),idSupplier,btSortedPageSupplier,btIndexPageSupplier);
      
      System.out.println("--------------- End ---------------");
      System.out.println("");
      System.out.println("");
    }
  
  private static <I extends ID,T extends Tuple> void _printTree(PageId currentPageId, String prefix, int i,
				 int keyType,IDSupplier<I> idSupplier,BTSortedPageSupplier<I,T> btSortedPageSupplier,BTIndexPageSupplier<I,T> btIndexPageSupplier)
    throws IOException, 
	   ConstructPageException, 
	   IteratorException,
	   HashEntryNotFoundException,
	   InvalidFrameNumberException,
	   PageUnpinnedException,
	   ReplacerException 
    {
      
      BTSortedPageI<I,T> sortedPage=btSortedPageSupplier.getBTSortedPage(currentPageId, keyType);
      prefix=prefix+"       ";
      i++;
      if( sortedPage.getType()==NodeType.INDEX) {  
	BTIndexPageI<I,T> indexPage=btIndexPageSupplier.getBTIndexPage(sortedPage, keyType);
	
	System.out.println(i+prefix+ indexPage.getPrevPage());
	_printTree( indexPage.getPrevPage(), prefix, i, keyType,idSupplier,btSortedPageSupplier,btIndexPageSupplier);
	
	I rid= idSupplier.getID();
	for( IKeyDataEntry<I> entry=indexPage.getFirst(rid); entry!=null;
	     entry=indexPage.getNext(rid)) {
	  System.out.println(i+prefix+(IndexData)entry.data);
	  _printTree( ((IndexData)entry.data).getData(), prefix, i, keyType,idSupplier,btSortedPageSupplier,btIndexPageSupplier);
	}
      }
      SystemDefs.JavabaseBM.unpinPage(currentPageId , true/*dirty*/);
    }
  
  
  
  /** For debug. Print all leaf pages of the B+ tree  out
   *@param header  the head page of the B+ tree file
   *@exception IOException  error from the lower layer
   *@exception ConstructPageException error for BT page constructor
   *@exception IteratorException  error from iterator
   *@exception HashEntryNotFoundException  error from lower layer
   *@exception InvalidFrameNumberException  error from lower layer
   *@exception PageUnpinnedException  error from lower layer
   *@exception ReplacerException  error from lower layer
   */
  public static <I extends ID,T extends Tuple>void printAllLeafPages(BTreeHeaderPageI<I,T> header,IDSupplier<I> idSupplier, KeyDataEntrySupplier<I> keyDataEntrySupplier,BTSortedPageSupplier<I,T> btSortedPageSupplier,BTIndexPageSupplier<I,T> btIndexPageSupplier, BTLeafPageSupplier<I,T> btLeafPageSupplier)
    throws IOException, 
	   ConstructPageException, 
	   IteratorException,
	   HashEntryNotFoundException,
	   InvalidFrameNumberException,
	   PageUnpinnedException,
	   ReplacerException 
    {
      if(header.get_rootId().pid == INVALID_PAGE) {
	System.out.println("The Tree is Empty!!!");
	return;
      }
      
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("---------------The B+ Tree Leaf Pages---------------");
      
      
      _printAllLeafPages(header.get_rootId(), header.get_keyType(),idSupplier,keyDataEntrySupplier,btSortedPageSupplier,btIndexPageSupplier,btLeafPageSupplier);
      
      System.out.println("");
      System.out.println("");
      System.out.println("------------- All Leaf Pages Have Been Printed --------");
      System.out.println("");
      System.out.println("");
    }
  
  private static <I extends ID,T extends Tuple> void _printAllLeafPages(PageId currentPageId,  int keyType, IDSupplier<I> idSupplier, KeyDataEntrySupplier<I> keyDataEntrySupplier,BTSortedPageSupplier<I,T> btSortedPageSupplier,BTIndexPageSupplier<I,T> btIndexPageSupplier, BTLeafPageSupplier<I,T> btLeafPageSupplier)
    throws IOException, 
	   ConstructPageException, 
	   IteratorException,
	   InvalidFrameNumberException, 
	   HashEntryNotFoundException,
	   PageUnpinnedException,
	   ReplacerException
    {
      
      BTSortedPageI<I,T> sortedPage=btSortedPageSupplier.getBTSortedPage(currentPageId,keyType);
      
      if( sortedPage.getType()==NodeType.INDEX) {  
	BTIndexPageI<I,T> indexPage=btIndexPageSupplier.getBTIndexPage((Page)sortedPage, keyType);
	
	_printAllLeafPages( indexPage.getPrevPage(),  keyType, idSupplier,keyDataEntrySupplier,btSortedPageSupplier,btIndexPageSupplier,btLeafPageSupplier);
	
	I rid=idSupplier.getID();
	for( IKeyDataEntry<I> entry=indexPage.getFirst(rid); entry!=null;
	     entry=indexPage.getNext(rid)) {
	  _printAllLeafPages( ((IndexData)entry.data).getData(),  keyType,idSupplier,keyDataEntrySupplier,btSortedPageSupplier,btIndexPageSupplier,btLeafPageSupplier);
	}
      }
      
      if( sortedPage.getType()==NodeType.LEAF) {  
	printPage(currentPageId, keyType,idSupplier,keyDataEntrySupplier,btSortedPageSupplier,btIndexPageSupplier,btLeafPageSupplier);
      }
      
      
      SystemDefs.JavabaseBM.unpinPage(currentPageId , true/*dirty*/);
    }
} // end of BT





