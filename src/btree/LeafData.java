package btree;
import btree.interfaces.DataClass;
import btree.interfaces.ILeafData;
import global.*;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class LeafData extends ILeafData<RID> {
    public LeafData(RID id) {
        super(id);
    }
    
    @Override
    protected RID getID(PageId pageNo, int slotNo) {
        return new RID(pageNo,slotNo);
    }
    
    @Override
    public LeafData copy() {
        return new LeafData(this.getData());
    }
}
