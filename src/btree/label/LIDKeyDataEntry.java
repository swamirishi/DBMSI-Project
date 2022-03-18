/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree.label;

import btree.KeyClass;
import btree.interfaces.DataClass;
import btree.interfaces.IKeyDataEntry;
import global.LID;
import global.PageId;
import global.QID;
import utils.supplier.leafdata.LIDLeafDataSupplier;
import utils.supplier.leafdata.QIDLeafDataSupplier;

/** KeyDataEntry: define (key, data) pair.
 */
public class LIDKeyDataEntry extends IKeyDataEntry<LID> {
  /** Class constructor
   */
  public LIDKeyDataEntry(Integer key, PageId pageNo) {
     super(key,pageNo);
  };



  /** Class constructor.
   */
  public LIDKeyDataEntry(KeyClass key, PageId pageNo) {
      super(key,pageNo);
  };


  /** Class constructor.
   */
  public LIDKeyDataEntry(String key, PageId pageNo) {
      super(key,pageNo);
  };

  /** Class constructor.
   */
  public LIDKeyDataEntry(Integer key, LID rid) {
     super(key, rid, LIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public LIDKeyDataEntry(KeyClass key, LID rid){
      super(key,rid, LIDLeafDataSupplier.getSupplier());
  };


  /** Class constructor.
   */
  public LIDKeyDataEntry(String key, LID rid) {
      super(key,rid,LIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public LIDKeyDataEntry(KeyClass key, DataClass data) {
      super(key,data);
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(LIDKeyDataEntry entry) {
      return super.equals(entry);
  }     
}

