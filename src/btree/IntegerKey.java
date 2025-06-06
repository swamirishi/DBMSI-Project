package btree;

/**  IntegerKey: It extends the KeyClass.
 *   It defines the integer Key.
 */ 
public class IntegerKey extends KeyClass {

  private Integer key;

  public String toString(){
     return key.toString();
  }

  /** Class constructor
   *  @param     value   the value of the integer key to be set 
   */
  public IntegerKey(Integer value) 
  { 
    key=new Integer(value.intValue());
  }

  /** Class constructor
   *  @param     value   the value of the integer key to be set 
   */
  public IntegerKey(int value) 
  { 
    key=new Integer(value);
  }



  /** get a copy of the integer key
   *  @return the reference of the copy 
   */
  public Integer getKey() 
  {
    return new Integer(key.intValue());
  }

  /** set the integer key value
   */  
  public void setKey(Integer value) 
  { 
    key=new Integer(value.intValue());
  }
  
  @Override
  public IntegerKey copy() {
    return new IntegerKey(this.getKey());
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    
    IntegerKey that = (IntegerKey) o;
  
    return key != null ? key.equals(that.key) : that.key == null;
  }
}
