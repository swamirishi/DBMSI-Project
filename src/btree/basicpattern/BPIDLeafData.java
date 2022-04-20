package btree.basicpattern;

import btree.interfaces.ILeafData;
import global.PageId;
import global.BPID;
import global.QID;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class BPIDLeafData extends ILeafData<BPID> {
    public BPIDLeafData(BPID id) {
        super(id);
    }
    
    @Override
    protected BPID getID(PageId pageNo, int slotNo) {
        return new BPID(pageNo,slotNo);
    }
    
    @Override
    public BPIDLeafData copy() {
        return new BPIDLeafData(this.getData());
    }
}
