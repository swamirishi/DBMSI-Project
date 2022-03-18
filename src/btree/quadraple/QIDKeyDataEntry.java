/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree.quadraple;

import btree.KeyClass;
import btree.interfaces.DataClass;
import btree.interfaces.IKeyDataEntry;
import global.PageId;
import global.QID;
import global.RID;
import utils.supplier.leafdata.QIDLeafDataSupplier;
import utils.supplier.leafdata.RIDLeafDataSupplier;

/** KeyDataEntry: define (key, data) pair.
 */
public class QIDKeyDataEntry extends IKeyDataEntry<QID> {
  /** Class constructor
   */
  public QIDKeyDataEntry(Integer key, PageId pageNo) {
     super(key,pageNo);
  };



  /** Class constructor.
   */
  public QIDKeyDataEntry(KeyClass key, PageId pageNo) {
      super(key,pageNo);
  };


  /** Class constructor.
   */
  public QIDKeyDataEntry(String key, PageId pageNo) {
      super(key,pageNo);
  };

  /** Class constructor.
   */
  public QIDKeyDataEntry(Integer key, QID rid) {
     super(key, rid, QIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public QIDKeyDataEntry(KeyClass key, QID rid){
      super(key,rid, QIDLeafDataSupplier.getSupplier());
  };


  /** Class constructor.
   */
  public QIDKeyDataEntry(String key, QID rid) {
      super(key,rid,QIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public QIDKeyDataEntry(KeyClass key, DataClass data) {
      super(key,data);
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(QIDKeyDataEntry entry) {
      return super.equals(entry);
  }     
}

