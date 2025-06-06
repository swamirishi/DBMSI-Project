package heap.interfaces;

/** JAVA */
/**
 * Scan.java-  class Scan
 *
 */

import java.io.*;
import global.*;
import diskmgr.*;
import heap.HFBufMgrException;
import heap.InvalidTupleSizeException;
import heap.Tuple;
import utils.supplier.dpageinfo.DPageInfoSupplier;
import utils.supplier.hfilepage.HFilePageSupplier;
import utils.supplier.id.IDSupplier;

/**
 * A Scan object is created ONLY through the function openScan
 * of a HeapFile. It supports the getNext interface which will
 * simply retrieve the next record in the heapfile.
 *
 * An object of type scan will always have pinned one directory page
 * of the heapfile.
 */
public abstract class ScanI<I extends ID,T extends Tuple> implements GlobalConst{
    
    /**
     * Note that one record in our way-cool HeapFile implementation is
     * specified by six (6) parameters, some of which can be determined
     * from others:
     */
    
    /** The heapfile we are using. */
    private HFile<I,T>  _hf;
    
    /** PageId of current directory page (which is itself an HFPage) */
    private PageId dirpageId = new PageId();
    
    /** pointer to in-core data of dirpageId (page is pinned) */
    private HFilePage<I,T> dirpage = getHFilePageSupplier().getHFilePage();
    
    /** record ID of the DataPageInfo struct (in the directory page) which
     * describes the data page where our current record lives.
     */
    private I datapageRid = getIDSupplier().getID();
    
    /** the actual PageId of the data page with the current record */
    private PageId datapageId = new PageId();
    
    /** in-core copy (pinned) of the same */
    private HFilePage<I,T> datapage = getHFilePageSupplier().getHFilePage();
    
    /** record ID of the current record (from the current data page) */
    private I userrid = getIDSupplier().getID();
    
    /** Status of next user status */
    private boolean nextUserStatus;
    
    protected abstract HFilePageSupplier<I,T> getHFilePageSupplier();
    protected abstract DPageInfoSupplier<T> getDPageInfoSupplier();
    protected abstract IDSupplier<I> getIDSupplier();
    /** The constructor pins the first directory page in the file
     * and initializes its private data members from the private
     * data member from hf
     *
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     *
     * @param hf A HeapFile object
     */
    public ScanI(HFile<I,T> hf)
            throws InvalidTupleSizeException,
            IOException
    {
        init(hf);
    }
    
    
    
    /** Retrieve the next record in a sequential scan
     *
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     *
     * @param rid Record ID of the record
     * @return the Tuple of the retrieved record.
     */
    public T getNext(I rid)
            throws InvalidTupleSizeException,
            IOException
    {
        T recptrtuple = null;
        
        if (nextUserStatus != true) {
            nextDataPage();
        }
        
        if (datapage == null)
            return null;
        
        rid.getPageNo().pid = userrid.getPageNo().pid;
        rid.setSlotNo(userrid.getSlotNo());
        
        try {
            recptrtuple = (T) datapage.getRecord(rid);
        }
        
        catch (Exception e) {
            //    System.err.println("SCAN: Error in Scan" + e);
            e.printStackTrace();
        }
        
        userrid = (I) datapage.nextRecord(rid);
        if(userrid == null) nextUserStatus = false;
        else nextUserStatus = true;
        
        return recptrtuple;
    }
    
    
    /** Position the scan cursor to the record with the given rid.
     *
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     * @param rid Record ID of the given record
     * @return 	true if successful,
     *			false otherwise.
     */
    public boolean position(I rid)
            throws InvalidTupleSizeException,
            IOException
    {
        I    nxtrid = getIDSupplier().getID();
        boolean bst;
        
        bst = peekNext(nxtrid);
        
        if (nxtrid.equals(rid)==true)
            return true;
        
        // This is kind lame, but otherwise it will take all day.
        PageId pgid = new PageId();
        pgid.pid = rid.getPageNo().pid;
        
        if (!datapageId.equals(pgid)) {
            
            // reset everything and start over from the beginning
            reset();
            
            bst =  firstDataPage();
            
            if (bst != true)
                return bst;
            
            while (!datapageId.equals(pgid)) {
                bst = nextDataPage();
                if (bst != true)
                    return bst;
            }
        }
        
        // Now we are on the correct page.
        
        try{
            userrid = (I) datapage.firstRecord();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
        if (userrid == null)
        {
            bst = false;
            return bst;
        }
        
        bst = peekNext(nxtrid);
        
        while ((bst == true) && (nxtrid != rid))
            bst = mvNext(nxtrid);
        
        return bst;
    }
    
    
    /** Do all the constructor work
     *
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     *
     * @param hf A HeapFile object
     */
    private void init(HFile hf)
            throws InvalidTupleSizeException,
            IOException
    {
        _hf = hf;
        
        firstDataPage();
    }
    
    
    /** Closes the Scan object */
    public void closescan()
    {
        reset();
    }
    
    
    /** Reset everything and unpin all pages. */
    private void reset()
    {
        
        if (datapage != null) {
            
            try{
                unpinPage(datapageId, false);
            }
            catch (Exception e){
                // 	System.err.println("SCAN: Error in Scan" + e);
                e.printStackTrace();
            }
        }
        datapageId.pid = 0;
        datapage = null;
        
        if (dirpage != null) {
            
            try{
                unpinPage(dirpageId, false);
            }
            catch (Exception e){
                //     System.err.println("SCAN: Error in Scan: " + e);
                e.printStackTrace();
            }
        }
        dirpage = null;
        
        nextUserStatus = true;
        
    }
    
    
    /** Move to the first data page in the file.
     * @exception InvalidTupleSizeException Invalid tuple size
     * @exception IOException I/O errors
     * @return true if successful
     *         false otherwise
     */
    private boolean firstDataPage()
            throws InvalidTupleSizeException,
            IOException
    {
        DPageInfo<T> dpinfo;
        T        rectuple = null;
        Boolean      bst;
        
        /** copy data about first directory page */
        
        dirpageId.pid = _hf.getFirstDirPageId().pid;
        nextUserStatus = true;
        
        /** get first directory page and pin it */
        try {
            dirpage  = getHFilePageSupplier().getHFilePage();
            pinPage(dirpageId, (Page) dirpage, false);
        }
        
        catch (Exception e) {
            //    System.err.println("SCAN Error, try pinpage: " + e);
            e.printStackTrace();
        }
        
        /** now try to get a pointer to the first datapage */
        datapageRid = (I) dirpage.firstRecord();
        
        if (datapageRid != null) {
            /** there is a datapage record on the first directory page: */
            
            try {
                rectuple = (T) dirpage.getRecord(datapageRid);
            }
            
            catch (Exception e) {
                //	System.err.println("SCAN: Chain Error in Scan: " + e);
                e.printStackTrace();
            }
            
            dpinfo = getDPageInfoSupplier().getDataPageInfo(rectuple);
            datapageId.pid = dpinfo.pageId.pid;
            
        } else {
            
            /** the first directory page is the only one which can possibly remain
             * empty: therefore try to get the next directory page and
             * check it. The next one has to contain a datapage record, unless
             * the heapfile is empty:
             */
            PageId nextDirPageId = new PageId();
            
            nextDirPageId = dirpage.getNextPage();
            
            if (nextDirPageId.pid != INVALID_PAGE) {
                
                try {
                    unpinPage(dirpageId, false);
                    dirpage = null;
                }
                
                catch (Exception e) {
                    //	System.err.println("SCAN: Error in 1stdatapage 1 " + e);
                    e.printStackTrace();
                }
                
                try {
                    
                    dirpage = getHFilePageSupplier().getHFilePage();
                    pinPage(nextDirPageId, (Page )dirpage, false);
                    
                }
                
                catch (Exception e) {
                    //  System.err.println("SCAN: Error in 1stdatapage 2 " + e);
                    e.printStackTrace();
                }
                
                /** now try again to read a data record: */
                
                try {
                    datapageRid = (I) dirpage.firstRecord();
                }
                
                catch (Exception e) {
                    //  System.err.println("SCAN: Error in 1stdatapg 3 " + e);
                    e.printStackTrace();
                    datapageId.pid = INVALID_PAGE;
                }
                
                if(datapageRid != null) {
                    
                    try {
                        
                        rectuple = (T) dirpage.getRecord(datapageRid);
                    }
                    
                    catch (Exception e) {
                        //    System.err.println("SCAN: Error getRecord 4: " + e);
                        e.printStackTrace();
                    }
                    
                    if (rectuple.getLength() != DPageInfo.size)
                        return false;
                    
                    dpinfo = getDPageInfoSupplier().getDataPageInfo(rectuple);
                    datapageId.pid = dpinfo.pageId.pid;
                    
                } else {
                    // heapfile empty
                    datapageId.pid = INVALID_PAGE;
                }
            }//end if01
            else {// heapfile empty
                datapageId.pid = INVALID_PAGE;
            }
        }
        
        datapage = null;
        
        try{
            nextDataPage();
        }
        
        catch (Exception e) {
            //  System.err.println("SCAN Error: 1st_next 0: " + e);
            e.printStackTrace();
        }
        
        return true;
        
        /** ASSERTIONS:
         * - first directory page pinned
         * - this->dirpageId has Id of first directory page
         * - this->dirpage valid
         * - if heapfile empty:
         *    - this->datapage == NULL, this->datapageId==INVALID_PAGE
         * - if heapfile nonempty:
         *    - this->datapage == NULL, this->datapageId, this->datapageRid valid
         *    - first datapage is not yet pinned
         */
        
    }
    
    
    /** Move to the next data page in the file and
     * retrieve the next data page.
     *
     * @return 		true if successful
     *			false if unsuccessful
     */
    private boolean nextDataPage()
            throws InvalidTupleSizeException,
            IOException
    {
        DPageInfo<T> dpinfo;
        
        boolean nextDataPageStatus;
        PageId nextDirPageId = new PageId();
        T rectuple = null;
        
        // ASSERTIONS:
        // - this->dirpageId has Id of current directory page
        // - this->dirpage is valid and pinned
        // (1) if heapfile empty:
        //    - this->datapage==NULL; this->datapageId == INVALID_PAGE
        // (2) if overall first record in heapfile:
        //    - this->datapage==NULL, but this->datapageId valid
        //    - this->datapageRid valid
        //    - current data page unpinned !!!
        // (3) if somewhere in heapfile
        //    - this->datapageId, this->datapage, this->datapageRid valid
        //    - current data page pinned
        // (4)- if the scan had already been done,
        //        dirpage = NULL;  datapageId = INVALID_PAGE
        
        if ((dirpage == null) && (datapageId.pid == INVALID_PAGE))
            return false;
        
        if (datapage == null) {
            if (datapageId.pid == INVALID_PAGE) {
                // heapfile is empty to begin with
                
                try{
                    unpinPage(dirpageId, false);
                    dirpage = null;
                }
                catch (Exception e){
                    //  System.err.println("Scan: Chain Error: " + e);
                    e.printStackTrace();
                }
                
            } else {
                
                // pin first data page
                try {
                    datapage  = getHFilePageSupplier().getHFilePage();
                    pinPage(datapageId, (Page) datapage, false);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                
                try {
                    userrid = (I) datapage.firstRecord();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                
                return true;
            }
        }
        
        // ASSERTIONS:
        // - this->datapage, this->datapageId, this->datapageRid valid
        // - current datapage pinned
        
        // unpin the current datapage
        try{
            unpinPage(datapageId, false /* no dirty */);
            datapage = null;
        }
        catch (Exception e){
        
        }
        
        // read next datapagerecord from current directory page
        // dirpage is set to NULL at the end of scan. Hence
        
        if (dirpage == null) {
            return false;
        }
        
        datapageRid = (I) dirpage.nextRecord(datapageRid);
        
        if (datapageRid == null) {
            nextDataPageStatus = false;
            // we have read all datapage records on the current directory page
            
            // get next directory page
            nextDirPageId = dirpage.getNextPage();
            
            // unpin the current directory page
            try {
                unpinPage(dirpageId, false /* not dirty */);
                dirpage = null;
                
                datapageId.pid = INVALID_PAGE;
            }
            
            catch (Exception e) {
            
            }
            
            if (nextDirPageId.pid == INVALID_PAGE)
                return false;
            else {
                // ASSERTION:
                // - nextDirPageId has correct id of the page which is to get
                
                dirpageId = nextDirPageId;
                
                try {
                    dirpage  = getHFilePageSupplier().getHFilePage();
                    pinPage(dirpageId, (Page)dirpage, false);
                }
                
                catch (Exception e){
                
                }
                
                if (dirpage == null)
                    return false;
                
                try {
                    datapageRid = (I) dirpage.firstRecord();
                    nextDataPageStatus = true;
                }
                catch (Exception e){
                    nextDataPageStatus = false;
                    return false;
                }
            }
        }
        
        // ASSERTION:
        // - this->dirpageId, this->dirpage valid
        // - this->dirpage pinned
        // - the new datapage to be read is on dirpage
        // - this->datapageRid has the Rid of the next datapage to be read
        // - this->datapage, this->datapageId invalid
        
        // data page is not yet loaded: read its record from the directory page
        try {
            rectuple = (T) dirpage.getRecord(datapageRid);
        }
        
        catch (Exception e) {
            System.err.println("HeapFile: Error in Scan" + e);
        }
        
        if (rectuple.getLength() != DPageInfo.size)
            return false;
        
        dpinfo = getDPageInfoSupplier().getDataPageInfo(rectuple);
        datapageId.pid = dpinfo.pageId.pid;
        
        try {
            datapage = getHFilePageSupplier().getHFilePage();
            pinPage(dpinfo.pageId, (Page) datapage, false);
        }
        
        catch (Exception e) {
            System.err.println("HeapFile: Error in Scan" + e);
        }
        
        
        // - directory page is pinned
        // - datapage is pinned
        // - this->dirpageId, this->dirpage correct
        // - this->datapageId, this->datapage, this->datapageRid correct
        
        userrid = (I) datapage.firstRecord();
        
        if(userrid == null)
        {
            nextUserStatus = false;
            return false;
        }
        
        return true;
    }
    
    
    private boolean peekNext(I rid) {
        
        rid.getPageNo().pid = userrid.getPageNo().pid;
        rid.setSlotNo(userrid.getSlotNo());
        return true;
        
    }
    
    
    /** Move to the next record in a sequential scan.
     * Also returns the RID of the (new) current record.
     */
    private boolean mvNext(I rid)
            throws InvalidTupleSizeException,
            IOException
    {
        I nextrid;
        boolean status;
        
        if (datapage == null)
            return false;
        
        nextrid = (I) datapage.nextRecord(rid);
        
        if( nextrid != null ){
            userrid.getPageNo().pid = nextrid.getPageNo().pid;
            userrid.setSlotNo(nextrid.getSlotNo());
            return true;
        } else {
            
            status = nextDataPage();
            
            if (status==true){
                rid.getPageNo().pid = userrid.getPageNo().pid;
                rid.setSlotNo(userrid.getSlotNo());
            }
            
        }
        return true;
    }
    
    /**
     * short cut to access the pinPage function in bufmgr package.
     * @see bufmgr.pinPage
     */
    private void pinPage(PageId pageno, Page page, boolean emptyPage)
            throws HFBufMgrException {
        
        try {
            SystemDefs.JavabaseBM.pinPage(pageno, page, emptyPage);
        }
        catch (Exception e) {
            throw new HFBufMgrException(e,"Scan.java: pinPage() failed");
        }
        
    } // end of pinPage
    
    /**
     * short cut to access the unpinPage function in bufmgr package.
     * @see bufmgr.unpinPage
     */
    private void unpinPage(PageId pageno, boolean dirty)
            throws HFBufMgrException {
        
        try {
            SystemDefs.JavabaseBM.unpinPage(pageno, dirty);
        }
        catch (Exception e) {
            throw new HFBufMgrException(e,"Scan.java: unpinPage() failed");
        }
        
    } // end of unpinPage
    
    
}
