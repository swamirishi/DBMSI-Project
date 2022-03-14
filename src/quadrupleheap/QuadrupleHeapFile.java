package quadrupleheap;

import java.io.*;
import diskmgr.*;
import bufmgr.*;
import global.*;
import heap.*;
import heap.interfaces.HFile;

/**  This heapfile implementation is directory-based. We maintain a
 *  directory of info about the data pages (which are of type THFPage
 *  when loaded into memory).  The directory itself is also composed
 *  of THFPages, with each record being of type DataPageInfo
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



public class QuadrupleHeapFile extends HFile<QID,Quadruple,THFPage,QuadrapleDataPageInfo,TScan> {
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
    public QuadrupleHeapFile(String name) throws HFException, HFBufMgrException, HFDiskMgrException, IOException {
        super(name);
    }
    
    @Override
    protected QuadrapleDataPageInfo getDataPageInfo() {
        return new QuadrapleDataPageInfo();
    }
    
    @Override
    protected QuadrapleDataPageInfo getDataPageInfo(Quadruple tuple) throws InvalidTupleSizeException, IOException {
        return new QuadrapleDataPageInfo(tuple);
    }
    
    @Override
    protected THFPage getHeapFilePage() {
        return new THFPage();
    }
    
    @Override
    protected THFPage getHeapFilePage(THFPage heapFilePage) {
        return new THFPage(heapFilePage);
    }
    
    @Override
    protected TScan getScan() throws InvalidTupleSizeException, IOException {
        return new TScan(this);
    }
    
    @Override
    protected QID getID() {
        return new QID();
    }
    
    @Override
    protected Quadruple getTuple() {
        return new Quadruple();
    }
}// End of HeapFile
