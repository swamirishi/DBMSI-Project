package utils.supplier.btfile;

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

import java.io.IOException;

public interface BTreeFileSupplier<I extends ID, T extends Tuple> {
    public BTreeFileI<I,T> getBTreeFile(String name) throws ConstructPageException, GetFileEntryException, PinPageException;
}
