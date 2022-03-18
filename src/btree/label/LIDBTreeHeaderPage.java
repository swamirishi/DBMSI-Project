/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */


package btree.label;

import btree.ConstructPageException;
import btree.interfaces.BTreeHeaderPageI;
import diskmgr.Page;
import global.LID;
import global.PageId;
import global.QID;
import labelheap.Label;
import quadrupleheap.Quadruple;

import java.io.IOException;

/**
 * Intefrace of a B+ tree index header page.
 * Here we use a HFPage as head page of the file
 * Inside the headpage, Logicaly, there are only seven
 * elements inside the head page, they are
 * magic0, rootId, keyType, maxKeySize, deleteFashion,
 * and type(=NodeType.BTHEAD)
 */
public class LIDBTreeHeaderPage extends BTreeHeaderPageI<LID, Label> {

protected void setPageId(PageId pageno)
  throws IOException
  {
    super.setPageId(pageno);
  }

protected PageId getPageId()
  throws IOException
  {
    return super.getPageId();
  }

/** set the magic0
 *@param magic  magic0 will be set to be equal to magic
 */
protected void set_magic0( int magic )
  throws IOException
  {
    super.set_magic0(magic);
  }


/** get the magic0
 */
protected int get_magic0()
  throws IOException
  {
    return super.get_magic0();
  };

/** set the rootId
 */
protected void  set_rootId( PageId rootID )
  throws IOException
  {
    super.set_rootId(rootID);
  };

/** get the rootId
 */
protected PageId get_rootId()
  throws IOException
  {
    return super.get_rootId();
  }

/** set the key type
 */
protected void set_keyType( short key_type )
  throws IOException
  {
    super.set_keyType(key_type);
  }

/** get the key type
 */
protected short get_keyType()
  throws IOException
  {
    return super.get_keyType();
  }

/** get the max keysize
 */
protected void set_maxKeySize(int key_size )
  throws IOException
  {
    super.set_maxKeySize(key_size);
  }

/** set the max keysize
 */
protected int get_maxKeySize()
  throws IOException
  {
    return super.get_maxKeySize();
  }

/** set the delete fashion
 */
protected void set_deleteFashion(int fashion )
  throws IOException
  {
    super.set_deleteFashion(fashion);
  }

/** get the delete fashion
 */
protected int get_deleteFashion()
  throws IOException
  {
    return super.get_deleteFashion();
  }



/** pin the page with pageno, and get the corresponding SortedPage
 */
public LIDBTreeHeaderPage(PageId pageno)
  throws ConstructPageException
  {
    super(pageno);
  }

/**associate the SortedPage instance with the Page instance */
public LIDBTreeHeaderPage(Page page) {
  
  super(page);
}

  @Override
  protected LID getID() {
    return new LID();
  }

  @Override
  protected Label getTuple(byte[] record, int offset, int length) {
    return new Label(record,offset,length);
  }

  /**new a page, and associate the SortedPage instance with the Page instance
 */
public LIDBTreeHeaderPage()
  throws ConstructPageException
  {
    super();
  }

} // end of BTreeHeaderPage
