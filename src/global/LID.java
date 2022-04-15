package global;
import java.io.*;

public class LID implements ID<LID> {
    public int slotNo;
    public PageId pageNo;

    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    public PageId getPageNo() {
        return pageNo;
    }

    public void setPageNo(PageId pageNo) {
        this.pageNo = pageNo;
    }

    public LID(PID pid){
        this(pid.getPageNo(),pid.getSlotNo());
    }
    public LID(EID eid){
        this(eid.getPageNo(),eid.getSlotNo());
    }
    public LID () {
        this(new PageId(0),0);
    }

    public LID (PageId pageno, int slotno) {
        pageNo = pageno;
        slotNo = slotno;
    }

    public String toString(){
        return slotNo + pageNo.toString();
    }

    public void copyLid (LID lid) {
        pageNo = lid.pageNo;
        slotNo = lid.slotNo;
    }

    /** Write the lid into a byte array at offset
     * @param ary the specified byte array
     * @param offset the offset of byte array to write
     * @exception java.io.IOException I/O errors
     */
    public void writeToByteArray(byte [] ary, int offset)
            throws java.io.IOException {
        Convert.setIntValue ( slotNo, offset, ary);
        Convert.setIntValue ( pageNo.pid, offset+4, ary);
    }


    /** Compares two LID object by value
     * @param lid LID object to be compared to
     * @return true is they are equal
     *         false if not.
     */
    public boolean equals(LID lid) {
        if ((this.pageNo.pid == lid.pageNo.pid) &&(this.slotNo == lid.slotNo))
            return true;
        else
            return false;
    }

    /**
     * Casts LID object to EID object
     * @return new EID object
     */
    public EID returnEid() {
        return new EID(this.pageNo, this.slotNo);
    }

    /**
     * Casts LID object to PID object
     * @return new PID object
     */
    public PID returnPid() {
        return new PID(this.pageNo, this.slotNo);
    }

//    public NID returnNid() {
//        return new NID(this.pageNo, this.slotNo);
//    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        LID lid = (LID) o;
        
        if (slotNo != lid.slotNo) {
            return false;
        }
        return pageNo != null ? pageNo.equals(lid.pageNo) : lid.pageNo == null;
    }
}

