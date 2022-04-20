/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree.basicpattern;

import btree.KeyClass;
import btree.interfaces.DataClass;
import btree.interfaces.IKeyDataEntry;
import global.PageId;
import global.BPID;
import global.RID;
import utils.supplier.leafdata.BPIDLeafDataSupplier;
import utils.supplier.leafdata.RIDLeafDataSupplier;

/** KeyDataEntry: define (key, data) pair.
 */
public class BPIDKeyDataEntry extends IKeyDataEntry<BPID> {
  /** Class constructor
   */
  public BPIDKeyDataEntry(Integer key, PageId pageNo) {
     super(key,pageNo);
  };



  /** Class constructor.
   */
  public BPIDKeyDataEntry(KeyClass key, PageId pageNo) {
      super(key,pageNo);
  };


  /** Class constructor.
   */
  public BPIDKeyDataEntry(String key, PageId pageNo) {
      super(key,pageNo);
  };

  /** Class constructor.
   */
  public BPIDKeyDataEntry(Integer key, BPID rid) {
     super(key, rid, BPIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public BPIDKeyDataEntry(KeyClass key, BPID rid){
      super(key,rid, BPIDLeafDataSupplier.getSupplier());
  };


  /** Class constructor.
   */
  public BPIDKeyDataEntry(String key, BPID rid) {
      super(key,rid,BPIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public BPIDKeyDataEntry(KeyClass key, DataClass data) {
      super(key,data);
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(BPIDKeyDataEntry entry) {
      return super.equals(entry);
  }     
}

