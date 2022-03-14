/* File HFPage.java */

package heap;

import java.io.*;
import java.lang.*;

import global.*;
import diskmgr.*;
import heap.interfaces.HFilePage;

/** Class heap file page.
 * The design assumes that records are kept compacted when
 * deletions are performed. 
 */

public class HFPage extends HFilePage<RID,Tuple> {
  
  
  
  public HFPage ()   {
      super();
  }
  
  /**
   * Constructor of class HFPage
   * open a HFPage and make this HFpage piont to the given page
   * @param  page  the given page in Page type
   */
  
  public HFPage(Page page)
    {
      super(page);
    }
    
    @Override
    protected RID getID() {
        return new RID();
    }
    
    @Override
    protected Tuple getTuple(byte[] record, int offset, int length) {
        return new Tuple(record,offset,length);
    }
    
}
