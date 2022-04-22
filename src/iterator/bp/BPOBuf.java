package iterator.bp;

import basicpatternheap.BasicPattern;
import global.BPID;
import global.RID;
import heap.Tuple;
import iterator.interfaces.OBufI;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

/**
 *O_buf::Put takes tuples and stores them on the buffer pages that
 *were passed to O_buf::init.  O_buf::flush inserts them enmass into
 *a temporary HeapFile.
 */
public class BPOBuf extends OBufI<BPID, BasicPattern> {
    @Override
    public TupleSupplier<BasicPattern> getTupleSupplier() {
        return BPIDTupleSupplier.getSupplier();
    }
}



