package quadrupleheap;

import java.io.*;
import java.lang.*;

import global.*;
import diskmgr.*;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.interfaces.HFilePage;

/** Class quadruple heap file page.
 * The design assumes that records are kept compacted when
 * deletions are performed.
 */

public class THFPage extends HFilePage<QID,Quadruple> {
    public THFPage() {
        super();
    }
    
    public THFPage(Page page) {
        super(page);
    }
    
    @Override
    protected QID getID() {
        return new QID();
    }
    
    @Override
    protected Quadruple getTuple(byte[] record, int offset, int length) {
        return new Quadruple(record,offset,length);
    }
}
