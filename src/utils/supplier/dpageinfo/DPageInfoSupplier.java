package utils.supplier.dpageinfo;

import heap.InvalidTupleSizeException;
import heap.Tuple;
import heap.interfaces.DPageInfo;

import java.io.IOException;

public interface DPageInfoSupplier<T extends Tuple> {
    public DPageInfo<T> getDataPageInfo(T tuple) throws InvalidTupleSizeException, IOException;
    public DPageInfo<T> getDataPageInfo();
}
