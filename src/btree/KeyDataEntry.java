/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree;
import btree.interfaces.DataClass;
import btree.interfaces.IKeyDataEntry;
import btree.interfaces.IndexData;
import global.*;
import utils.supplier.leafdata.RIDLeafDataSupplier;

/** KeyDataEntry: define (key, data) pair.
 */
public class KeyDataEntry extends IKeyDataEntry<RID> {
  /** Class constructor
   */
  public KeyDataEntry( Integer key, PageId pageNo) {
     super(key,pageNo);
  }; 



  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key, PageId pageNo) {
      super(key,pageNo);
  };


  /** Class constructor.
   */
  public KeyDataEntry( String key, PageId pageNo) {
      super(key,pageNo);
  };

  /** Class constructor.
   */
  public KeyDataEntry( Integer key, RID rid) {
     super(key,rid, RIDLeafDataSupplier.getSupplier());
  };

  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key, RID rid){
      super(key,rid, RIDLeafDataSupplier.getSupplier());
  };


  /** Class constructor.
   */
  public KeyDataEntry( String key, RID rid) {
      super(key,rid,RIDLeafDataSupplier.getSupplier());
  }; 

  /** Class constructor.
   */
  public KeyDataEntry( KeyClass key,  DataClass data) {
      super(key,data);
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(KeyDataEntry entry) {
      return super.equals(entry);
  }     
}

