/*  File NID.java   */

package global;

import java.io.*;

/** class NID
 */

public class NID implements ID<NID>{

    /** public int slotNo
     */
    public int slotNo;

    /** public PageId pageNo
     */
    public PageId pageNo = new PageId();

    /**
     * default constructor of class
     */
    public NID () { }

    /**
     *  constructor of class
     */
    public NID (PageId pageno, int slotno)
    {
        pageNo = pageno;
        slotNo = slotno;
    }

    /**
     * make a copy of the given rid
     */
    public void copyNid (NID nid)
    {
        pageNo = nid.pageNo;
        slotNo = nid.slotNo;
    }

    @Override
    public int getSlotNo() {
        return this.slotNo;
    }

    @Override
    public PageId getPageNo() {
        return this.pageNo;
    }

    @Override
    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    @Override
    public void setPageNo(PageId pageId) {
        this.pageNo = pageId;
    }

    /** Write the rid into a byte array at offset
     * @param ary the specified byte array
     * @param offset the offset of byte array to write
     * @exception java.io.IOException I/O errors
     */
    public void writeToByteArray(byte [] ary, int offset)
            throws java.io.IOException
    {
        Convert.setIntValue ( slotNo, offset, ary);
        Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }


    /** Compares two NID object, i.e, this to the rid
     * @param nid NID object to be compared to
     * @return true is they are equal
     *         false if not.
     */
    public boolean equals(NID nid) {

        if ((this.pageNo.pid==nid.pageNo.pid)
                &&(this.slotNo==nid.slotNo))
            return true;
        else
            return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NID nid = (NID) o;

        if (slotNo != nid.slotNo) {
            return false;
        }
        return pageNo != null ? pageNo.equals(nid.pageNo) : nid.pageNo == null;
    }
}
