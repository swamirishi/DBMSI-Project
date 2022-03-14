package labelheap;

import java.io.*;
import diskmgr.*;
import bufmgr.*;
import global.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;


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

public class LabelHeapFile extends HFile<LID,Label,LHFPage,LabelDataPageInfo,LScan> {
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
    
    @Override
    protected LabelDataPageInfo getDataPageInfo() {
        return new LabelDataPageInfo();
    }
    
    @Override
    protected LabelDataPageInfo getDataPageInfo(Label tuple) throws InvalidTupleSizeException, IOException {
        return new LabelDataPageInfo(tuple);
    }
    
    @Override
    protected LHFPage getHeapFilePage() {
        return new LHFPage();
    }
    
    @Override
    protected LHFPage getHeapFilePage(LHFPage heapFilePage) {
        return new LHFPage(heapFilePage);
    }
    
    @Override
    protected LScan getScan() throws InvalidTupleSizeException, IOException {
        return new LScan(this);
    }
    
    @Override
    protected LID getID() {
        return new LID();
    }
    
    @Override
    protected Label getTuple() {
        return new Label();
    }
}// End of HeapFile
