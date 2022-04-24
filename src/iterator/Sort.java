package iterator;

import java.io.*; 
import global.*;
import heap.*;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortI;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.hfile.RIDHFileSupplier;
import utils.supplier.iterator.pnode.PNodeSupplier;
import utils.supplier.iterator.pnode.RIDPNodeSupplier;
import utils.supplier.iterator.pnodesplaypq.RIDPNodeSPlayPQSupplier;
import utils.supplier.iterator.pnodesplaypq.pnodeSplayPQSupplier;
import utils.supplier.iterator.spoofIBuf.RIDSpoofIBufSupplier;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;
import utils.supplier.iterator.obuf.OBufSupplier;
import utils.supplier.iterator.obuf.RIDOBufSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

/**
 * The Sort class sorts a file. All necessary information are passed as 
 * arguments to the constructor. After the constructor call, the user can
 * repeatly call <code>get_next()</code> to get tuples in sorted order.
 * After the sorting is done, the user should call <code>close()</code>
 * to clean up.
 */
public class Sort extends SortI<RID,Tuple>
{
    /**
     * Class constructor, take information about the tuples, and set up
     * the sorting
     *
     * @param in           array containing attribute types of the relation
     * @param len_in       number of columns in the relation
     * @param str_sizes    array of sizes of string attributes
     * @param am           an iterator for accessing the tuples
     * @param sort_fld     the field number of the field to sort on
     * @param sort_order   the sorting order (ASCENDING, DESCENDING)
     * @param sort_fld_len the length of the sort field
     * @param n_pages      amount of memory (in pages) available for sorting
     * @throws IOException   from lower layers
     * @throws SortException something went wrong in the lower layer.
     */
    public Sort(AttrType[] in,
                short len_in,
                short[] str_sizes,
                IteratorI<Tuple> am,
                int sort_fld,
                TupleOrder sort_order,
                int sort_fld_len,
                int n_pages) throws IOException, SortException {
        super(in, len_in, str_sizes, am, sort_fld, sort_order, sort_fld_len, n_pages);
    }
    
    @Override
    protected SpoofIBufSupplier<RID, Tuple> getSpoofIBufSupplier() {
        return RIDSpoofIBufSupplier.getSupplier();
    }
    
    @Override
    protected TupleSupplier<Tuple> getTupleSupplier() {
        return RIDTupleSupplier.getSupplier();
    }
    
    @Override
    protected pnodeSplayPQSupplier<Tuple> getPnodeSplayPQSupplier() {
        return RIDPNodeSPlayPQSupplier.getSupplier();
    }
    
    @Override
    protected OBufSupplier<RID, Tuple> getOBufSupplier() {
        return RIDOBufSupplier.getSupplier();
    }
    
    @Override
    protected PNodeSupplier<Tuple> getPNodeSupplier() {
        return RIDPNodeSupplier.getSupplier();
    }
    
    @Override
    protected HFileSupplier<RID, Tuple> getHFileSupplier() {
        return RIDHFileSupplier.getSupplier();
    }
    
    @Override
    public int compare(AttrType fldType, Tuple t1, int t1_fld_no, Tuple value) throws TupleUtilsException, UnknowAttrType, IOException {
        return TupleUtils.CompareTupleWithValue(fldType, t1, t1_fld_no, value);
    }
}


