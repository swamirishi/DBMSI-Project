package labelheap;

import java.io.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;

/**
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public class LScan extends ScanI<Label,LHFPage,LID,LabelDataPageInfo> {
    /**
     * The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from hf
     *
     * @param hf A HeapFile object
     * @throws InvalidTupleSizeException Invalid tuple size
     * @throws IOException               I/O errors
     */
    public LScan(HFile hf) throws InvalidTupleSizeException, IOException {
        super(hf);
    }
    
    @Override
    protected LHFPage getHeapFilePage() {
        return new LHFPage();
    }
    
    @Override
    protected LID getID() {
        return new LID();
    }
    
    @Override
    protected LabelDataPageInfo getDataPageInfo(Label tuple) throws InvalidTupleSizeException, IOException {
        return new LabelDataPageInfo(tuple);
    }
}


