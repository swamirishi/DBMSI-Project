package global;

import java.io.Serializable;

public class EID implements ID<EID> {
    public int slotNo;
    public PageId pageNo;

    public EID () {
        this(new PageId(),0);
    }

    public String toString(){
        return slotNo + pageNo.toString();
    }

    public int getSlotNo() {
        return slotNo;
    }

    public PageId getPageNo() {
        return pageNo;
    }

    public EID (PageId pageno, int slotno) {
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
    
    /** Write the eid into a byte array at offset
     * @param ary the specified byte array
     * @param offset the offset of byte array to write
     * @exception java.io.IOException I/O errors
     */
    public void writeToByteArray(byte [] ary, int offset)
            throws java.io.IOException {
        Convert.setIntValue ( slotNo, offset, ary);
        Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }


    /** Compares two EID object by value
     * @param eid EID object to be compared to
     * @return true is they are equal
     *         false if not.
     */
    public boolean equals(EID eid) {
        if ((this.pageNo.pid == eid.pageNo.pid) &&(this.slotNo == eid.slotNo))
            return true;
        else
            return false;
    }
}

