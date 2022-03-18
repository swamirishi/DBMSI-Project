package utils.supplier.id;

import global.ID;
import global.PageId;

public interface IDSupplier<I extends ID> {
    public I getID();
    public I getID(PageId pageno, int slotno);
}
