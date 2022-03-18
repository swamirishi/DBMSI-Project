package btree.label;

import btree.interfaces.ILeafData;
import global.LID;
import global.PageId;
import global.QID;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class LIDLeafData extends ILeafData<LID> {
    public LIDLeafData(LID id) {
        super(id);
    }
    
    @Override
    protected LID getID(PageId pageNo, int slotNo) {
        return new LID(pageNo,slotNo);
    }
    
    @Override
    public LIDLeafData copy() {
        return new LIDLeafData(this.getData());
    }
}
