package iterator;
import heap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import iterator.interfaces.OBufI;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.*;

/**
 *O_buf::Put takes tuples and stores them on the buffer pages that
 *were passed to O_buf::init.  O_buf::flush inserts them enmass into
 *a temporary HeapFile.
 */
public class OBuf extends OBufI<RID,Tuple> {
    @Override
    public TupleSupplier<Tuple> getTupleSupplier() {
        return RIDTupleSupplier.getSupplier();
    }
}



