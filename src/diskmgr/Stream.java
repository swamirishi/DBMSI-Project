package diskmgr;

import global.*;
import heap.*;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.THFPage;

import java.io.IOException;
import java.util.*;

import static global.GlobalConst.INVALID_PAGE;

public class Stream {

    /**
     * The heapfile we are using.
     */
    private QuadrupleHeapFile quadrupleHeapFile;

    /**
     * PageId of current directory page (which is itself an HFPage)
     */
    private PageId dirpageId = new PageId();

    /**
     * pointer to in-core data of dirpageId (page is pinned)
     */
    private THFPage dirpage = new THFPage();

    /**
     * record ID of the DataPageInfo struct (in the directory page) which
     * describes the data page where our current record lives.
     */
    private QID datapageQid = new QID();

    /**
     * the actual PageId of the data page with the current record
     */
    private PageId datapageId = new PageId();

    /**
     * in-core copy (pinned) of the same
     */
    private THFPage datapage = new THFPage();

    /**
     * record ID of the current record (from the current data page)
     */
    private QID userqid = new QID();

    /**
     * Status of next user status
     */
    private boolean nextUserStatus;

    //using list instead of collection?
    private final List<Quadruple> quadrupleList = new ArrayList<>();

    private Iterator<Quadruple> iterator;

    public Stream(RDFDB rdfDB, int orderType, String subjectFilter, String predicateFilter,
                  String objectFilter, double confidenceFilter) throws Exception {
        this.quadrupleHeapFile = rdfDB.getQuadrupleHeapFile();

        //get to the first data page.
        firstDataPage();

        Quadruple currQuadruple = null;
        LabelHeapFile entityLabelHeapFile = rdfDB.getEntityLabelHeapFile();
        LabelHeapFile predicateLabelHeapFile = rdfDB.getPredicateLabelHeapFile();
        EID currSubjectID;
        EID currObjectID;
        PID currPredicateID;

        //assuming last record is null.
        try {
            currQuadruple = this.quadrupleHeapFile.getRecord(userqid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        currQuadruple = getNextInternal(userqid);

        do {

            currSubjectID = currQuadruple.getSubject();
            currObjectID = currQuadruple.getObject();
            currPredicateID = currQuadruple.getPredicate();
            String currSubject = entityLabelHeapFile.getRecord(currSubjectID.returnLid()).getLabel();


            //filters
            //ignoring ordertype

            //subject filter
            if (subjectFilter != null) {
                if (entityLabelHeapFile.getRecord(currSubjectID.returnLid()).getLabel().compareTo(subjectFilter) != 0) {
                    currQuadruple = getNextInternal(userqid);
                    continue;
                }
            }
            //predicate filter
            if (predicateFilter != null) {
                if (predicateLabelHeapFile.getRecord(currPredicateID.returnLid()).getLabel().compareTo(predicateFilter) != 0) {
                    currQuadruple = getNextInternal(userqid);
                    continue;
                }
            }
            //object filter
            if (objectFilter != null) {
                if (entityLabelHeapFile.getRecord(currObjectID.returnLid()).getLabel().compareTo(objectFilter) != 0) {
                    currQuadruple = getNextInternal(userqid);
                    continue;
                }
            }

            //confidence filter
            if (confidenceFilter != 0) {
                if (currQuadruple.getValue() < confidenceFilter) {
                    currQuadruple = getNextInternal(userqid);
                    continue;
                }
            }

            //passed all filters => store this quadruple in collection
            currQuadruple = getNextInternal(userqid);
            quadrupleList.add(currQuadruple);

        } while (userqid != null);

        //sort the collection according to the orderType
        quadrupleList.sort(new SortOnOrder(orderType));
        iterator = quadrupleList.iterator();
    }

    public Quadruple getNext() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }


    /**
     * Retrieve the next record in a sequential scan
     *
     * @param qid Record ID of the record
     * @return the aTuple of the retrieved record.
     * @throws InvalidTupleSizeException Invalid tuple size
     * @throws IOException               I/O errors
     */
    public Quadruple getNextInternal(QID qid)
            throws InvalidTupleSizeException,
            IOException, HFBufMgrException, InvalidSlotNumberException {
        Quadruple quadruple = null;

        if (nextUserStatus != true) {
            nextDataPage();
        }

        if (datapage == null)
            return null;

        qid.pageNo.pid = userqid.pageNo.pid;
        qid.slotNo = userqid.slotNo;

        quadruple = datapage.getRecord(qid);

        userqid = datapage.nextRecord(qid);
        if (userqid == null) {
            nextUserStatus = false;
        } else {
            nextUserStatus = true;
        }

        return quadruple;
    }

    /**
     * Closes the Scan object
     */
    public void closeStream() throws HFBufMgrException {
        if (datapage != null) {
            unpinPage(datapageId, false);
        }
        datapageId.pid = 0;
        datapage = null;

        if (dirpage != null) {
            unpinPage(dirpageId, false);
        }
        dirpage = null;

        nextUserStatus = true;
    }

    /**
     * Move to the first data page in the file.
     *
     * @return true if successful
     * false otherwise
     * @throws InvalidTupleSizeException Invalid tuple size
     * @throws IOException               I/O errors
     */
    private boolean firstDataPage()
            throws InvalidTupleSizeException,
            IOException {
        DataPageInfo dpinfo;
        Quadruple quadruple = null;
        Boolean bst;

        /** copy data about first directory page */

        dirpageId.pid = quadrupleHeapFile._firstDirPageId.pid;
        nextUserStatus = true;

        /** get first directory page and pin it */
        try {
            dirpage = new THFPage();
            pinPage(dirpageId, (Page) dirpage, false);
        } catch (Exception e) {
            //    System.err.println("SCAN Error, try pinpage: " + e);
            e.printStackTrace();
        }

        /** now try to get a pointer to the first datapage */
        datapageQid = dirpage.firstRecord();

        if (datapageQid != null) {
            /** there is a datapage record on the first directory page: */

            try {
                quadruple = dirpage.getRecord(datapageQid);
            } catch (Exception e) {
                //	System.err.println("SCAN: Chain Error in Scan: " + e);
                e.printStackTrace();
            }

            dpinfo = new DataPageInfo(quadruple);
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
                } catch (Exception e) {
                    //	System.err.println("SCAN: Error in 1stdatapage 1 " + e);
                    e.printStackTrace();
                }

                try {

                    dirpage = new THFPage();
                    pinPage(nextDirPageId, (Page) dirpage, false);

                } catch (Exception e) {
                    //  System.err.println("SCAN: Error in 1stdatapage 2 " + e);
                    e.printStackTrace();
                }

                /** now try again to read a data record: */

                try {
                    datapageQid = dirpage.firstRecord();
                } catch (Exception e) {
                    //  System.err.println("SCAN: Error in 1stdatapg 3 " + e);
                    e.printStackTrace();
                    datapageId.pid = INVALID_PAGE;
                }

                if (datapageQid != null) {

                    try {

                        quadruple = dirpage.getRecord(datapageQid);
                    } catch (Exception e) {
                        //    System.err.println("SCAN: Error getRecord 4: " + e);
                        e.printStackTrace();
                    }

                    if (quadruple.getLength() != DataPageInfo.size)
                        return false;

                    dpinfo = new DataPageInfo(quadruple);
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

        try {
            nextDataPage();
        } catch (Exception e) {
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
         *    - this->datapage == NULL, this->datapageId, this->datapageQid valid
         *    - first datapage is not yet pinned
         */

    }


    /**
     * Move to the next data page in the file and
     * retrieve the next data page.
     *
     * @return true if successful
     * false if unsuccessful
     */
    private boolean nextDataPage()
            throws InvalidTupleSizeException,
            IOException, HFBufMgrException {
        DataPageInfo dpinfo;
        Quadruple quadruple = null;
        PageId nextDirPageId;
        // ASSERTIONS:
        // - this->dirpageId has Id of current directory page
        // - this->dirpage is valid and pinned
        // (1) if heapfile empty:
        //    - this->datapage==NULL; this->datapageId == INVALID_PAGE
        // (2) if overall first record in heapfile:
        //    - this->datapage==NULL, but this->datapageId valid
        //    - this->datapageQid valid
        //    - current data page unpinned !!!
        // (3) if somewhere in heapfile
        //    - this->datapageId, this->datapage, this->datapageQid valid
        //    - current data page pinned
        // (4)- if the scan had already been done,
        //        dirpage = NULL;  datapageId = INVALID_PAGE

        if (dirpage == null && datapageId.pid == INVALID_PAGE)
            return false;

        if (datapage == null) {
            datapage = new THFPage();
            pinPage(datapageId, (Page) datapage, false);
            userqid = datapage.firstRecord();
            return true;
        }

        // ASSERTIONS:
        // - this->datapage, this->datapageId, this->datapageQid valid
        // - current datapage pinned

        // unpin the current datapage
        unpinPage(datapageId, false /* no dirty */);
        datapage = null;

        // read next datapagerecord from current directory page
        // dirpage is set to NULL at the end of scan. Hence

        if (dirpage == null) {
            return false;
        }

        datapageQid = dirpage.nextRecord(datapageQid);

        if (datapageQid == null) {
            // we have read all datapage records on the current directory page
            // get next directory page
            nextDirPageId = dirpage.getNextPage();

            // unpin the current directory page
            unpinPage(dirpageId, false /* not dirty */);
            dirpage = null;
            datapageId.pid = INVALID_PAGE;

            if (nextDirPageId.pid == INVALID_PAGE)
                return false;
            else {
                // ASSERTION:
                // - nextDirPageId has correct id of the page which is to get

                dirpageId = nextDirPageId;
                dirpage = new THFPage();
                pinPage(dirpageId, (Page) dirpage, false);

                if (dirpage == null)
                    return false;

                try {
                    datapageQid = dirpage.firstRecord();
                } catch (Exception e) {
                    return false;
                }
            }
        }

        // ASSERTION:
        // - this->dirpageId, this->dirpage valid
        // - this->dirpage pinned
        // - the new datapage to be read is on dirpage
        // - this->datapageQid has the Qid of the next datapage to be read
        // - this->datapage, this->datapageId invalid

        // data page is not yet loaded: read its record from the directory page
        try {
            quadruple = dirpage.getRecord(datapageQid);
        } catch (Exception e) {
            System.err.println("HeapFile: Error in Scan" + e);
        }

        if (quadruple.getLength() != DataPageInfo.size)
            return false;

        dpinfo = new DataPageInfo(quadruple);
        datapageId.pid = dpinfo.pageId.pid;

        try {
            datapage = new THFPage();
            pinPage(dpinfo.pageId, (Page) datapage, false);
        } catch (Exception e) {
            System.err.println("HeapFile: Error in Scan" + e);
        }


        // - directory page is pinned
        // - datapage is pinned
        // - this->dirpageId, this->dirpage correct
        // - this->datapageId, this->datapage, this->datapageQid correct

        userqid = datapage.firstRecord();

        if (userqid == null) {
            nextUserStatus = false;
            return false;
        }

        return true;
    }

    /**
     * short cut to access the pinPage function in bufmgr package.
     *
     * @see bufmgr.BufMgr.pinPage
     */
    private void pinPage(PageId pageno, Page page, boolean emptyPage)
            throws HFBufMgrException {

        try {
            SystemDefs.JavabaseBM.pinPage(pageno, page, emptyPage);
        } catch (Exception e) {
            throw new HFBufMgrException(e, "Scan.java: pinPage() failed");
        }

    } // end of pinPage

    /**
     * short cut to access the unpinPage function in bufmgr package.
     *
     * @see bufmgr.unpinPage
     */
    private void unpinPage(PageId pageno, boolean dirty)
            throws HFBufMgrException {

        try {
            SystemDefs.JavabaseBM.unpinPage(pageno, dirty);
        } catch (Exception e) {
            throw new HFBufMgrException(e, "Scan.java: unpinPage() failed");
        }

    } // end of unpinPage

}

