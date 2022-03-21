package utils.supplier.btfile;

import btree.AddFileEntryException;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import global.ID;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Tuple;
import heap.interfaces.HFile;
import utils.supplier.keyclass.KeyClassManager;

import java.io.IOException;

public interface BTreeFileSupplier<I extends ID, T extends Tuple,K> {
    public BTreeFileI<I,T,K> getBTreeFile(String name, KeyClassManager<K> keyClassManager) throws ConstructPageException, GetFileEntryException, PinPageException, IOException, AddFileEntryException;
}
