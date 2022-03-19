package labelheap;

import java.io.*;
import diskmgr.*;
import bufmgr.*;
import global.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.dpageinfo.LIDDPageInfoSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.hfilepage.LIDHFilePageSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.LIDSupplier;
import utils.supplier.scan.LIDScanSupplier;
import utils.supplier.scan.ScanSupplier;
import utils.supplier.tuple.LIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;


/**  This heapfile implementation is directory-based. We maintain a
 *  directory of info about the data pages (which are of type LHFPage
 *  when loaded into memory).  The directory itself is also composed
 *  of LHFPages, with each record being of type DataPageInfo
 *  as defined below.
 *
 *  The first directory page is a header page for the entire database
 *  (it is the one to which our filename is mapped by the DB).
 *  All directory pages are in a doubly-linked list of pages, each
 *  directory entry points to a single data page, which contains
 *  the actual records.
 *
 *  The heapfile data pages are implemented as slotted pages, with
 *  the slots at the front and the records in the back, both growing
 *  into the free space in the middle of the page.
 *
 *  We can store roughly pagesize/sizeof(DataPageInfo) records per
 *  directory page; for any given HeapFile insertion, it is likely
 *  that at least one of those referenced data pages will have
 *  enough free space to satisfy the request.
 */


/** DataPageInfo class : the type of records stored on a directory page.
 *
 * April 9, 1998
 */

public class LabelHeapFile extends HFile<LID,Label> {
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
    
    @Override
    protected ScanSupplier<LID, Label> getScanSupplier() {
        return LIDScanSupplier.getSupplier();
    }
    
    @Override
    protected TupleSupplier<Label> getTupleSupplier() {
        return LIDTupleSupplier.getSupplier();
    }
    
    /**
     * Initialize.  A null name produces a temporary heapfile which will be
     * deleted by the destructor.  If the name already denotes a file, the
     * file is opened; otherwise, a new empty file is created.
     *
     * @param name
     * @throws HFException        heapfile exception
     * @throws HFBufMgrException  exception thrown from bufmgr layer
     * @throws HFDiskMgrException exception thrown from diskmgr layer
     * @throws IOException        I/O errors
     */
    public LabelHeapFile(String name) throws HFException, HFBufMgrException, HFDiskMgrException, IOException {
        super(name);
    }
}// End of HeapFile
