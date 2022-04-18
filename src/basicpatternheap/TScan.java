package basicpatternheap;

import global.BPID;
import heap.InvalidTupleSizeException;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import utils.supplier.dpageinfo.BPIDDPageInfoSupplier;
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.hfilepage.BPIDHFilePageSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;

import java.io.IOException;

/**
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public class TScan extends ScanI<BPID, BasicPattern> {
    @Override
    protected HFilePageSupplier<BPID, BasicPattern> getHFilePageSupplier() {
        return BPIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    protected DPageInfoSupplier<BasicPattern> getDPageInfoSupplier() {
        return BPIDDPageInfoSupplier.getSupplier();
    }
    
    @Override
    protected IDSupplier<BPID> getIDSupplier() {
        return BPIDSupplier.getSupplier();
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
    public TScan(HFile<BPID,BasicPattern> hf) throws InvalidTupleSizeException, IOException {
        super(hf);
    }
}

