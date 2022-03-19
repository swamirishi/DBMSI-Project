/*
 * @(#) BT.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */
package btree.quadraple;

import btree.*;
import btree.interfaces.BTI;
import btree.label.LIDBTreeHeaderPage;
import btree.label.LIDKeyDataEntry;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.PageId;
import utils.supplier.btindexpage.LIDBTIndexPageSupplier;
import utils.supplier.btindexpage.QIDBTIndexPageSupplier;
import utils.supplier.btleafpage.LIDBTLeafPageSupplier;
import utils.supplier.btleafpage.QIDBTLeafPageSupplier;
import utils.supplier.btsortedpage.LIDBTSortedPageSupplier;
import utils.supplier.btsortedpage.QIDBTSortedPageSupplier;
import utils.supplier.id.LIDSupplier;
import utils.supplier.id.QIDSupplier;
import utils.supplier.keydataentry.LIDKeyDataEntrySupplier;
import utils.supplier.keydataentry.QIDKeyDataEntrySupplier;
import utils.supplier.leafdata.LIDLeafDataSupplier;
import utils.supplier.leafdata.QIDLeafDataSupplier;

import java.io.IOException;

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
public class QIDBT extends BTI {

  
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
  protected final static QIDKeyDataEntry getEntryFromBytes(byte[] from, int offset,
                                                           int length, int keyType, short nodeType)
    throws KeyNotMatchException,
	   NodeNotMatchException,
	   ConvertException
    {
      return (QIDKeyDataEntry) BTI.getEntryFromBytes(from, offset, length, keyType, nodeType, QIDSupplier.getSupplier(),
                                                     QIDLeafDataSupplier.getSupplier(), QIDKeyDataEntrySupplier.getSupplier());
    }
  
  
  /** It convert a keyDataEntry to byte[].
   *@param  entry specify  the data entry. Input parameter.
   *@return return a byte array with size equal to the size of (key,data).
   *@exception   KeyNotMatchException  entry.key is neither StringKey nor  IntegerKey
   *@exception NodeNotMatchException entry.data is neither LeafData nor IndexData
   *@exception ConvertException error from the lower layer
   */
  protected final static byte[] getBytesFromEntry( KeyDataEntry entry )
    throws KeyNotMatchException,
	   NodeNotMatchException,
	   ConvertException
    {
      return BTI.getBytesFromEntry(entry);
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
  
  public static void printPage(PageId pageno, int keyType)
    throws  IOException,
	    IteratorException,
	    ConstructPageException,
            HashEntryNotFoundException,
	    ReplacerException,
	    PageUnpinnedException,
            InvalidFrameNumberException
    {
      BTI.printPage(pageno, keyType, QIDSupplier.getSupplier(), QIDKeyDataEntrySupplier.getSupplier(),
                    QIDBTSortedPageSupplier.getSupplier(), QIDBTIndexPageSupplier.getSupplier(), QIDBTLeafPageSupplier.getSupplier());
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
  public static void printBTree(QIDBTreeHeaderPage header)
    throws IOException,
	   ConstructPageException,
	   IteratorException,
	   HashEntryNotFoundException,
	   InvalidFrameNumberException,
	   PageUnpinnedException,
	   ReplacerException
    {
      BTI.printBTree(header,QIDSupplier.getSupplier(),
                     QIDBTSortedPageSupplier.getSupplier(), QIDBTIndexPageSupplier.getSupplier());
    }
  
//  private static void _printTree(PageId currentPageId, String prefix, int i,
//				 int keyType)
//    throws IOException,
//	   ConstructPageException,
//	   IteratorException,
//	   HashEntryNotFoundException,
//	   InvalidFrameNumberException,
//	   PageUnpinnedException,
//	   ReplacerException
//    {
//
//      BTI._printTree(currentPageId,prefix,i,keyType,QIDSupplier.getSupplier(),QIDBTSortedPageSupplier.getSupplier(),QIDBTIndexPageSupplier.getSupplier());
//    }
  
  
  
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
  public static void printAllLeafPages(QIDBTreeHeaderPage header)
    throws IOException, 
	   ConstructPageException, 
	   IteratorException,
	   HashEntryNotFoundException,
	   InvalidFrameNumberException,
	   PageUnpinnedException,
	   ReplacerException 
    {
      BTI.printAllLeafPages(header,QIDSupplier.getSupplier(),QIDKeyDataEntrySupplier.getSupplier(),QIDBTSortedPageSupplier.getSupplier(),QIDBTIndexPageSupplier.getSupplier(),QIDBTLeafPageSupplier.getSupplier());
    }
  
//  private static void _printAllLeafPages(PageId currentPageId,  int keyType)
//    throws IOException,
//	   ConstructPageException,
//	   IteratorException,
//	   InvalidFrameNumberException,
//	   HashEntryNotFoundException,
//	   PageUnpinnedException,
//	   ReplacerException
//    {
//
//      BTI._printAllLeafPages(currentPageId,keyType,QIDSupplier.getSupplier(),QIDKeyDataEntrySupplier.getSupplier(),QIDBTSortedPageSupplier.getSupplier(),QIDBTIndexPageSupplier.getSupplier(),QIDBTLeafPageSupplier.getSupplier());
//    }
} // end of BT





