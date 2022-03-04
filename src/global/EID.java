package global;

public class EID {
    private int slotNo;
    private PageId pageNo;
    
    public EID() {
        this(new PageId(), 0);
    }
    
    public EID(PageId pageno, int slotno) {
        this.pageNo = pageno;
        this.slotNo = slotno;
    }
    
    public void copy(EID eid) {
        this.setPageNo(eid.getPageNo());
        this.setSlotNo(eid.getSlotNo());
    }
    
    /**
     * Write the eid into a byte array at offset
     *
     * @param ary    the specified byte array
     * @param offset the offset of byte array to write
     * @throws java.io.IOException I/O errors
     */
    public void writeToByteArray(byte[] ary, int offset) throws java.io.IOException {
        Convert.setIntValue(slotNo, offset, ary);
        Convert.setIntValue(pageNo.pid, offset + 4, ary);
    }
    
    /**
     * Compares two EID object by value
     *
     * @param eid EID object to be compared to
     * @return true is they are equal
     * false if not.
     */
    public boolean equals(EID eid) {
        if ((this.pageNo.pid == eid.pageNo.pid) && (this.slotNo == eid.slotNo)) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getSlotNo() {
        return slotNo;
    }
    
    public PageId getPageNo() {
        return pageNo;
    }
    
    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }
    
    public void setPageNo(PageId pageNo) {
        this.pageNo = pageNo;
    }
}

