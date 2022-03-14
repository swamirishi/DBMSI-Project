package heap;


/** File DataPageInfo.java */


import global.*;
import heap.interfaces.DPageInfo;
import quadrupleheap.Quadruple;
import labelheap.*;

import java.io.*;

/** DataPageInfo class : the type of records stored on a directory page.
*
* April 9, 1998
*/

public class DataPageInfo extends DPageInfo<Tuple> {
  public DataPageInfo() {
    super();
  }
  
  public DataPageInfo(byte[] array) {
    super(array);
  }
  
  public DataPageInfo(Tuple _atuple) throws InvalidTupleSizeException, IOException {
    super(_atuple);
  }
  
  @Override
  public Tuple getTuple(byte[] data, int offset, int size) {
    return new Tuple(data,offset,size);
  }
}






