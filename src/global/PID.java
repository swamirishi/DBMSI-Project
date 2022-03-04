package global;

public class PID {
    private int slotNo;
    private PageId pageNo;
    
    public PID() {
        this(new PageId(), 0);
    }
    
    public PID(PageId pageno, int slotno) {
        this.pageNo = pageno;
        slotNo = slotno;
    }
    
    public void copy(PID pid) {
        this.setPageNo(pid.getPageNo());
        this.setSlotNo(pid.getSlotNo());
    }
    
    /**
     * Write the pid into a byte array at offset
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
     * Compares two PID object by value
     *
     * @param pid PID object to be compared to
     * @return true is they are equal
     * false if not.
     */
    public boolean equals(PID pid) {
        if ((this.pageNo.pid == pid.pageNo.pid) && (this.slotNo == pid.slotNo)) {
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


