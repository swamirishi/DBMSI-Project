package global;

import java.util.Objects;

public class BPID implements ID {
    public int slotNo;
    public PageId pageNo;

    public BPID () {
        this(new PageId(),0);
    }

    public BPID (PageId pageno, int slotno) {
        pageNo = pageno;
        slotNo = slotno;
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

    /** Write the qid into a byte array at offset
     * @param ary the specified byte array
     * @param offset the offset of byte array to write
     * @exception java.io.IOException I/O errors
     */
    public void writeToByteArray(byte [] ary, int offset)
            throws java.io.IOException {
        Convert.setIntValue ( slotNo, offset, ary);
        Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }


    /** Compares two QID object by value
     * @param qid QID object to be compared to
     * @return true is they are equal
     *         false if not.
     */
    public boolean equals(BPID bpid) {
        if ((this.pageNo.pid == bpid.pageNo.pid) &&(this.slotNo == bpid.slotNo))
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

        BPID bpid = (BPID) o;

        if (slotNo != bpid.slotNo) {
            return false;
        }
        return Objects.equals(pageNo, bpid.pageNo);
    }

    @Override
    public int hashCode() {
        int result = slotNo;
        result = 31 * result + (pageNo != null ? pageNo.hashCode() : 0);
        return result;
    }
}


