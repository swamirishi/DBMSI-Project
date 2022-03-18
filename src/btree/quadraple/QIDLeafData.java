package btree.quadraple;

import btree.interfaces.ILeafData;
import global.PageId;
import global.QID;
import global.QID;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public class QIDLeafData extends ILeafData<QID> {
    public QIDLeafData(QID id) {
        super(id);
    }
    
    @Override
    protected QID getID(PageId pageNo, int slotNo) {
        return new QID(pageNo,slotNo);
    }
    
    @Override
    public QIDLeafData copy() {
        return new QIDLeafData(this.getData());
    }
}
