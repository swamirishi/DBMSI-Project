/*
 * @(#) bt.java   98/05/14
 * Copyright (c) 1998 UW.  All Rights Reserved.
 *        Author Xiaohu Li (xiaohu@cs.wisc.edu)
 */
package btree.interfaces;

import btree.IntegerKey;
import btree.KeyClass;
import btree.LeafData;
import btree.StringKey;
import global.ID;
import global.PageId;
import global.RID;
import utils.supplier.leafdata.LeafDataSupplier;

/** KeyDataEntry: define (key, data) pair.
 */
public class IKeyDataEntry<I extends ID> {
   /** key in the (key, data)
    */
   public KeyClass key;
   /** data in the (key, data)
    */
   public DataClass data;
   
  /** Class constructor
   */
  public IKeyDataEntry(Integer key, PageId pageNo) {
     this.key = new IntegerKey(key);
     this.data = new IndexData(pageNo);
  };



  /** Class constructor.
   */
  public IKeyDataEntry(KeyClass key, PageId pageNo) {

     data = new IndexData(pageNo);
     this.key = key==null?null:key.copy();
//     if ( key instanceof IntegerKey )
//        this.key= new IntegerKey(((IntegerKey)key).getKey());
//     else if ( key instanceof StringKey)
//        this.key= new StringKey(((StringKey)key).getKey());
  };


  /** Class constructor.
   */
  public IKeyDataEntry(String key, PageId pageNo) {
     this.key = new StringKey(key);
     this.data = new IndexData(pageNo);
  };

  /** Class constructor.
   */
  public IKeyDataEntry(Integer key, I rid, LeafDataSupplier<I> leafDataSupplier) {
     this.key = new IntegerKey(key);
     this.data = leafDataSupplier.getLeafData(rid);
  };

  /** Class constructor.
   */
  public IKeyDataEntry(KeyClass key, I rid,LeafDataSupplier<I> leafDataSupplier){
     data = leafDataSupplier.getLeafData(rid);
     this.key = key==null?null:key.copy();
//     if ( key instanceof IntegerKey )
//        this.key= new IntegerKey(((IntegerKey)key).getKey());
//     else if ( key instanceof StringKey )
//        this.key= new StringKey(((StringKey)key).getKey());
  };


  /** Class constructor.
   */
  public IKeyDataEntry(String key, I rid,LeafDataSupplier<I> leafDataSupplier) {
     this.key = new StringKey(key);
     this.data = leafDataSupplier.getLeafData(rid);
  };

  /** Class constructor.
   */
  public IKeyDataEntry(KeyClass key, DataClass data) {
      this.key = key.copy();
      this.data = data.copy();
//     if ( key instanceof IntegerKey )
//        this.key= new IntegerKey(((IntegerKey)key).getKey());
//     else if ( key instanceof StringKey )
//        this.key= new StringKey(((StringKey)key).getKey());
//
//     if ( data instanceof IndexData )
//        this.data= new IndexData(((IndexData)data).getData());
//     else if ( data instanceof LeafData )
//        this.data= new LeafData(((LeafData)data).getData());
  }

  /** shallow equal. 
   *  @param entry the entry to check again key. 
   *  @return true, if entry == key; else, false.
   */
  public boolean equals(IKeyDataEntry entry) {
      return this.equals(entry);
//      boolean st1,st2;
//      return this.key.equals(entry.key) && this.;
//      if ( key instanceof IntegerKey )
//         st1= ((IntegerKey)key).getKey().equals
//                  (((IntegerKey)entry.key).getKey());
//      else
//         st1= ((StringKey)key).getKey().equals
//                  (((StringKey)entry.key).getKey());
//
//      if( data instanceof IndexData )
//         st2= ( (IndexData)data).getData().pid==
//              ((IndexData)entry.data).getData().pid ;
//      else
//         st2= ((RID)((LeafData)data).getData()).equals
//                (((RID)((LeafData)entry.data).getData()));
//
//
//      return (st1&&st2);
  }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        IKeyDataEntry<?> that = (IKeyDataEntry<?>) o;
        
        if (key != null ? !key.equals(that.key) : that.key != null) {
            return false;
        }
        return data != null ? data.equals(that.data) : that.data == null;
    }
}

