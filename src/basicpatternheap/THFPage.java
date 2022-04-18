package basicpatternheap;

import diskmgr.Page;
import global.BPID;
import heap.interfaces.HFilePage;

/** Class quadruple heap file page.
 * The design assumes that records are kept compacted when
 * deletions are performed.
 */

public class THFPage extends HFilePage<BPID,BasicPattern> {
    public THFPage() {
        super();
    }

    public THFPage(Page page) {
        super(page);
    }

    @Override
    protected BPID getID() {
        return new BPID();
    }

    @Override
    protected BasicPattern getTuple(byte[] record, int offset, int length) {
        return new BasicPattern(record,offset,length);
    }
}
