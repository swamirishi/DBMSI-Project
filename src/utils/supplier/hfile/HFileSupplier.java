package utils.supplier.hfile;

import global.ID;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Tuple;
import heap.interfaces.HFile;

import java.io.IOException;

public interface HFileSupplier<I extends ID, T extends Tuple> {
    public HFile<I,T> getHFile(String name) throws IOException, HFException, HFBufMgrException, HFDiskMgrException;
}
