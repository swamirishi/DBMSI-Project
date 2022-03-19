package labelheap;

import java.io.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.dpageinfo.LIDDPageInfoSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.LIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.LIDSupplier;

/**
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public class LScan extends ScanI<LID,Label> {
    @Override
    protected HFilePageSupplier<LID, Label> getHFilePageSupplier() {
        return LIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    protected DPageInfoSupplier<Label> getDPageInfoSupplier() {
        return LIDDPageInfoSupplier.getSupplier();
    }
    
    @Override
    protected IDSupplier<LID> getIDSupplier() {
        return LIDSupplier.getSupplier();
    }
    
    /**
     * The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from hf
     *
     * @param hf A HeapFile object
     * @throws InvalidTupleSizeException Invalid tuple size
     * @throws IOException               I/O errors
     */
    public LScan(HFile<LID,Label> hf) throws InvalidTupleSizeException, IOException {
        super(hf);
    }
}


