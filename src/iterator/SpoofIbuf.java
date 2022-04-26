package iterator;

import heap.*;          
import global.*;
import diskmgr.*;
import bufmgr.*;
import iterator.interfaces.SpoofIbufI;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.*;

public class SpoofIbuf extends SpoofIbufI<RID,Tuple> {
    @Override
    public IDSupplier<RID> getIDSupplier() {
        return RIDSupplier.getSupplier();
    }
    
    @Override
    public TupleSupplier<Tuple> getTupleSupplier() {
        return RIDTupleSupplier.getSupplier();
    }
}


