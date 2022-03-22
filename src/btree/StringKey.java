package btree;

/**  StringKey: It extends the KeyClass.
 *   It defines the string Key.
 */ 
public class StringKey extends KeyClass {

  private String key;

  public String toString(){
     return key;
  }

  /** Class constructor
   *  @param     s   the value of the string key to be set 
   */
  public StringKey(String s) { key = new String(s); }

  /** get a copy of the istring key
  *  @return the reference of the copy 
  */ 
  public String getKey() {return new String(key);}

  /** set the string key value
   */ 
  public void setKey(String s) { key=new String(s);}
  
  @Override
  public StringKey copy() {
    return new StringKey(this.getKey());
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    
    StringKey stringKey = (StringKey) o;
  
    return key != null ? key.equals(stringKey.key) : stringKey.key == null;
  }
  
  public boolean check(StringKey key){
    return this.key.startsWith(key.getKey());
  }
}
