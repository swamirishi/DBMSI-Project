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
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.dpageinfo.RIDDPageInfoSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.RIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;

public class Scan extends ScanI<RID,Tuple> {
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
    protected HFilePageSupplier<RID, Tuple> getHFilePageSupplier() {
        return RIDHFilePageSupplier.getSupplier();
    }
    
    @Override
    protected DPageInfoSupplier<Tuple> getDPageInfoSupplier() {
        return RIDDPageInfoSupplier.getSupplier();
    }
    
    @Override
    protected IDSupplier<RID> getIDSupplier() {
        return RIDSupplier.getSupplier();
    }
}
