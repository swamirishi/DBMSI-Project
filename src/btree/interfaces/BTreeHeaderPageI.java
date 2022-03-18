/*
 * @(#) BTIndexPage.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *         Author: Xiaohu Li (xioahu@cs.wisc.edu)
 *
 */


package btree.interfaces;

import btree.ConstructPageException;
import diskmgr.Page;
import global.ID;
import global.PageId;
import global.SystemDefs;
import heap.HFPage;
import heap.Tuple;
import heap.interfaces.HFilePage;

import java.io.IOException;

/**
 * Intefrace of a B+ tree index header page.
 * Here we use a HFPage as head page of the file
 * Inside the headpage, Logicaly, there are only seven
 * elements inside the head page, they are
 * magic0, rootId, keyType, maxKeySize, deleteFashion,
 * and type(=NodeType.BTHEAD)
 */
public abstract class BTreeHeaderPageI<I extends ID,T extends Tuple> extends HFilePage<I,T> {

protected void setPageId(PageId pageno)
  throws IOException
  {
    setCurPage(pageno);
  }

protected PageId getPageId()
  throws IOException
  {
    return getCurPage();
  }

/** set the magic0
 *@param magic  magic0 will be set to be equal to magic
 */
protected void set_magic0( int magic )
  throws IOException
  {
    setPrevPage(new PageId(magic));
  }


/** get the magic0
 */
protected int get_magic0()
  throws IOException
  {
    return getPrevPage().pid;
  };

/** set the rootId
 */
protected void  set_rootId( PageId rootID )
  throws IOException
  {
    setNextPage(rootID);
  };

/** get the rootId
 */
protected PageId get_rootId()
  throws IOException
  {
    return getNextPage();
  }

/** set the key type
 */
protected void set_keyType( short key_type )
  throws IOException
  {
    setSlot(3, (int)key_type, 0);
  }

/** get the key type
 */
protected short get_keyType()
  throws IOException
  {
    return   (short)getSlotLength(3);
  }

/** get the max keysize
 */
protected void set_maxKeySize(int key_size )
  throws IOException
  {
    setSlot(1, key_size, 0);
  }

/** set the max keysize
 */
protected int get_maxKeySize()
  throws IOException
  {
    return getSlotLength(1);
  }

/** set the delete fashion
 */
protected void set_deleteFashion(int fashion )
  throws IOException
  {
    setSlot(2, fashion, 0);
  }

/** get the delete fashion
 */
protected int get_deleteFashion()
  throws IOException
  {
    return getSlotLength(2);
  }



/** pin the page with pageno, and get the corresponding SortedPage
 */
public BTreeHeaderPageI(PageId pageno)
  throws ConstructPageException
  {
    super();
    try {

SystemDefs.JavabaseBM.pinPage(pageno, this, false/*Rdisk*/);
    }
    catch (Exception e) {
throw new ConstructPageException(e, "pinpage failed");
    }
  }

/**associate the SortedPage instance with the Page instance */
public BTreeHeaderPageI(Page page) {
  
  super(page);
}


/**new a page, and associate the SortedPage instance with the Page instance
 */
public BTreeHeaderPageI()
  throws ConstructPageException
  {
    super();
    try{
Page apage=new Page();
PageId pageId=SystemDefs.JavabaseBM.newPage(apage,1);
if (pageId==null)
throw new ConstructPageException(null, "new page failed");
this.init(pageId, apage);

    }
    catch (Exception e) {
throw new ConstructPageException(e, "construct header page failed");
    }
  }

} // end of BTreeHeaderPage
