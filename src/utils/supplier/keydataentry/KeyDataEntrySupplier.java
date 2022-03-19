package utils.supplier.keydataentry;

import btree.KeyClass;
import btree.interfaces.DataClass;
import btree.interfaces.IKeyDataEntry;
import global.ID;
import global.PageId;

public interface KeyDataEntrySupplier<I extends ID> {
    public IKeyDataEntry<I> getKeyDataEntry(KeyClass key, DataClass data);
    public IKeyDataEntry<I> getKeyDataEntry(KeyClass key, PageId pageNo);
    public IKeyDataEntry<I> getKeyDataEntry(KeyClass key, I id);
}
