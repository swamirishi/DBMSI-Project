package utils.supplier.hfilepage;

import global.ID;
import heap.Tuple;
import heap.interfaces.HFilePage;

public interface HFilePageSupplier<I extends ID, T extends Tuple> {
    public HFilePage<I,T> getHFilePage();
    public HFilePage<I,T> getHFilePage(HFilePage<I,T> heapFilePage);
}
