package iterator.bp;

import basicpatternheap.BasicPattern;
import global.BPID;
import global.RID;
import heap.Tuple;
import iterator.interfaces.SpoofIbufI;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

public class BPSpoofIbuf extends SpoofIbufI<BPID, BasicPattern> {
    @Override
    public IDSupplier<BPID> getIDSupplier() {
        return BPIDSupplier.getSupplier();
    }
    
    @Override
    public TupleSupplier<BasicPattern> getTupleSupplier() {
        return BPIDTupleSupplier.getSupplier();
    }
    
    
}


