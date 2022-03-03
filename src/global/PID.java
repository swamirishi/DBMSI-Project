package global;

public class PID {
    public int slotNo;
    public PageId pageNo = new PageId();

    public PID () {

    }

    public PID (PageId pageno, int slotno) {
        pageNo = pageno;
        slotNo = slotno;
    }

    public void copyEid (PID pid) {
        pageNo = pid.pageNo;
        slotNo = pid.slotNo;
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

    /**
     * Casts EID object to LID object
     * @return new LID object
     */
    public LID returnLid() {
        return new LID(this.pageNo, this.slotNo);
    }
}


