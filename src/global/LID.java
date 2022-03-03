package global;
import java.io.*;

public class LID {
    public int slotNo;
    public PageId pageNo = new PageId();

    public LID () {

    }

    public LID (PageId pageno, int slotno) {
        pageNo = pageno;
        slotNo = slotno;
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
}

