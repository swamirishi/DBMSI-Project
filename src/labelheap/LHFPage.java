package labelheap;

import java.lang.*;

import global.*;
import diskmgr.*;
import heap.interfaces.HFilePage;

/** Class label heap file page.
 * The design assumes that records are kept compacted when
 * deletions are performed.
 */



public class LHFPage extends HFilePage<LID,Label> {

    
    public LHFPage ()   {
        super();
    }

    /**
     * Constructor of class LHFPage
     * open a LHFPage and make this HFpage piont to the given page
     * @param  page  the given page in Page type
     */

    public LHFPage(Page page)
    {
        super(page);
    }
    
    @Override
    protected LID getID() {
        return new LID();
    }
    
    @Override
    protected Label getTuple(byte[] record, int offset, int length) {
        return new Label(record,offset,length);
    }
    
    /**
     * Constructor of class LHFPage
     * open a existed hfpage
     * @param  apage   a page in buffer pool
     */

    public void openLHFpage(Page apage)
    {
        super.openHFpage(apage);
    }

    /**
     * @return byte array
     */

    public byte [] getLHFpageArray() {
        return super.getHFpageArray();
    }
    
}
