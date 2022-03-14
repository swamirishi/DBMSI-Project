package heap;

/** JAVA */
/**
 * Scan.java-  class Scan
 *
 */

import java.io.*;
import global.*;
import diskmgr.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;

public class Scan extends ScanI<Tuple,HFPage,RID,DataPageInfo> {
    /**
     * The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from hf
     *
     * @param hf A HeapFile object
     * @throws InvalidTupleSizeException Invalid tuple size
     * @throws IOException               I/O errors
     */
    public Scan(HFile hf) throws InvalidTupleSizeException, IOException {
        super(hf);
    }
    
    @Override
    protected HFPage getHeapFilePage() {
        return new HFPage();
    }
    
    @Override
    protected RID getID() {
        return new RID();
    }
    
    @Override
    protected DataPageInfo getDataPageInfo(Tuple tuple) throws InvalidTupleSizeException, IOException {
        return new DataPageInfo(tuple);
    }
}
