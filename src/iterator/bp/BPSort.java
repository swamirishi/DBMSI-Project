package iterator.bp;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.BPID;
import global.TupleOrder;
import iterator.SortException;
import iterator.interfaces.IteratorI;
import iterator.interfaces.SortI;
import utils.supplier.hfile.BPIDHFileSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.iterator.pnode.BPIDPNodeSupplier;
import utils.supplier.iterator.pnode.PNodeSupplier;
import utils.supplier.iterator.pnodesplaypq.BPIDPNodeSPlayPQSupplier;
import utils.supplier.iterator.pnodesplaypq.pnodeSplayPQSupplier;
import utils.supplier.iterator.spoofIBuf.BPIDSpoofIBufSupplier;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;
import utils.supplier.iterator.obuf.BPIDOBufSupplier;
import utils.supplier.iterator.obuf.OBufSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

/**
 * The Sort class sorts a file. All necessary information are passed as 
 * arguments to the constructor. After the constructor call, the user can
 * repeatly call <code>get_next()</code> to get tuples in sorted order.
 * After the sorting is done, the user should call <code>close()</code>
 * to clean up.
 */
public class BPSort extends SortI<BPID, BasicPattern>
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
    public BPSort(AttrType[] in,
                  short len_in,
                  short[] str_sizes,
                  IteratorI<BasicPattern> am,
                  int sort_fld,
                  TupleOrder sort_order,
                  int sort_fld_len,
                  int n_pages) throws IOException, SortException {
        super(in, len_in, str_sizes, am, sort_fld, sort_order, sort_fld_len, n_pages);
    }
    
    @Override
    protected SpoofIBufSupplier<BPID, BasicPattern> getSpoofIBufSupplier() {
        return BPIDSpoofIBufSupplier.getSupplier();
    }
    
    @Override
    protected TupleSupplier<BasicPattern> getTupleSupplier() {
        return BPIDTupleSupplier.getSupplier();
    }
    
    @Override
    protected pnodeSplayPQSupplier<BasicPattern> getPnodeSplayPQSupplier() {
        return BPIDPNodeSPlayPQSupplier.getSupplier();
    }
    
    @Override
    protected OBufSupplier<BPID, BasicPattern> getOBufSupplier() {
        return BPIDOBufSupplier.getSupplier();
    }
    
    @Override
    protected PNodeSupplier<BasicPattern> getPNodeSupplier() {
        return BPIDPNodeSupplier.getSupplier();
    }
    
    @Override
    protected HFileSupplier<BPID, BasicPattern> getHFileSupplier() {
        return BPIDHFileSupplier.getSupplier();
    }
}


