package utils.supplier.leafdata;

import btree.LeafData;
import btree.interfaces.ILeafData;
import global.ID;

public interface LeafDataSupplier<I extends ID> {
    
    public ILeafData<I> getLeafData(I id);
}
