package quadrupleheap;

import java.io.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.dpageinfo.QIDDPageInfoSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.QIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.QIDSupplier;

/**
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public class TScan extends ScanI<QID,Quadruple> {
    @Override
    protected HFilePageSupplier<QID, Quadruple> getHFilePageSupplier() {
        return QIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    protected DPageInfoSupplier<Quadruple> getDPageInfoSupplier() {
        return QIDDPageInfoSupplier.getSupplier();
    }
    
    @Override
    protected IDSupplier<QID> getIDSupplier() {
        return QIDSupplier.getSupplier();
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
    public TScan(HFile<QID,Quadruple> hf) throws InvalidTupleSizeException, IOException {
        super(hf);
    }
}

