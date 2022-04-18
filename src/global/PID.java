package global;

import java.io.Serializable;

public class PID implements ID<PID> {
    public int slotNo;
    public PageId pageNo;

    public PID () {
        this(new PageId(),0);
    }

    public PID (PageId pageno, int slotno) {
        pageNo = pageno;
        slotNo = slotno;
    }

    public void copyEid (PID pid) {
        pageNo = pid.pageNo;
        slotNo = pid.slotNo;
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
    
    /** Write the pid into a byte array at offset
     * @param ary the specified byte array
     * @param offset the offset of byte array to write
     * @exception java.io.IOException I/O errors
     */
    public void writeToByteArray(byte [] ary, int offset)
            throws java.io.IOException {
        Convert.setIntValue ( slotNo, offset, ary);
        Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }

    public String toString(){
        return slotNo + pageNo.toString();
    }


    /** Compares two PID object by value
     * @param pid PID object to be compared to
     * @return true is they are equal
     *         false if not.
     */
    public boolean equals(PID pid) {
        if ((this.pageNo.pid == pid.pageNo.pid) &&(this.slotNo == pid.slotNo))
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
        
        PID pid = (PID) o;
        
        if (slotNo != pid.slotNo) {
            return false;
        }
        return pageNo != null ? pageNo.equals(pid.pageNo) : pid.pageNo == null;
    }
    
    /**
     * Casts EID object to LID object
     * @return new LID object
     */
    public LID returnLid() {
        return new LID(this.pageNo, this.slotNo);
    }
    
    public NID returnNid() {
        return new NID(this.pageNo, this.slotNo);
    }
    
}


