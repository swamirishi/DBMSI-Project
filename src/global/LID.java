package global;

import java.io.*;

public class LID {
    private int slotNo;
    private PageId pageNo;
    
    public LID(EID eid) {
        this(eid.getPageNo(), eid.getSlotNo());
    }
    
    public LID(PID pid) {
        this(pid.getPageNo(), pid.getSlotNo());
    }
    
    public LID(PageId pageno, int slotno) {
        this.pageNo = pageno;
        this.slotNo = slotno;
    }
    
    public LID() {
        this(new PageId(), 0);
    }
    
    public void copy(LID lid) {
        this.setPageNo(lid.getPageNo());
        this.setSlotNo(lid.getSlotNo());
    }
    
    /**
     * Write the lid into a byte array at offset
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
     * Compares two LID object by value
     *
     * @param lid LID object to be compared to
     * @return true is they are equal
     * false if not.
     */
    public boolean equals(LID lid) {
        if ((this.pageNo.pid == lid.pageNo.pid) && (this.slotNo == lid.slotNo)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Casts LID object to EID object
     *
     * @return new EID object
     */
    public EID returnEid() {
        return new EID(this.pageNo, this.slotNo);
    }
    
    /**
     * Casts LID object to PID object
     *
     * @return new PID object
     */
    public PID returnPid() {
        return new PID(this.pageNo, this.slotNo);
    }
    
    private void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }
    
    private void setPageNo(PageId pageNo) {
        this.pageNo = pageNo;
    }
    
    public int getSlotNo() {
        return slotNo;
    }
    
    public PageId getPageNo() {
        return pageNo;
    }
}

