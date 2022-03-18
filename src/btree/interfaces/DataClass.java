package btree.interfaces;

import javax.xml.crypto.Data;

/** DataClass: An abstarct class. It will be extended
 *  to be IndexData and LeafData.
 */ 
public abstract class DataClass{
    public abstract DataClass copy();
}  
