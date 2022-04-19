package iterator.interfaces;

import bufmgr.PageNotReadException;
import global.AttrType;
import global.ID;
import global.RID;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import index.IndexException;
import iterator.*;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.tuple.TupleSupplier;

import java.io.IOException;

public abstract class FileScanI<I extends ID, T extends Tuple> extends IteratorI<T> {
    private AttrType[] _in1;
    private short in1_len;
    private short[] s_sizes;
    private HFile<I, T> f;
    private ScanI<I, T> scan;
    private T tuple1;
    private T Jtuple;
    private int t1_size;
    private int nOutFlds;
    private CondExpr[] OutputFilter;
    public FldSpec[] perm_mat;

    public abstract IDSupplier<I> getIDSupplier();

    public abstract TupleSupplier<T> getTupleSupplier();

    public abstract HFileSupplier<I, T> getHFileSupplier();

    /**
     * constructor
     *
     * @param file_name  heapfile to be opened
     * @param in1[]      array showing what the attributes of the input fields are.
     * @param s1_sizes[] shows the length of the string fields.
     * @param len_in1    number of attributes in the input tuple
     * @param n_out_flds number of fields in the out tuple
     * @param proj_list  shows what input fields go where in the output tuple
     * @param outFilter  select expressions
     * @throws IOException         some I/O fault
     * @throws FileScanException   exception from this class
     * @throws TupleUtilsException exception from this class
     * @throws InvalidRelation     invalid relation
     */
    public FileScanI(String file_name, AttrType in1[], short s1_sizes[], short len_in1, int n_out_flds, FldSpec[] proj_list, CondExpr[] outFilter) throws IOException, FileScanException, TupleUtilsException, InvalidRelation {
        _in1 = in1;
        in1_len = len_in1;
        s_sizes = s1_sizes;

        Jtuple = getTupleSupplier().getTuple();
        AttrType[] Jtypes = new AttrType[n_out_flds];
        short[] ts_size;
        ts_size = TupleUtils.setup_op_tuple(Jtuple, Jtypes, in1, len_in1, s1_sizes, proj_list, n_out_flds);

        OutputFilter = outFilter;
        perm_mat = proj_list;
        nOutFlds = n_out_flds;
        tuple1 = getTupleSupplier().getTuple();

        try {
            tuple1.setHdr(in1_len, _in1, s1_sizes);
        } catch (Exception e) {
            throw new FileScanException(e, "setHdr() failed");
        }
        t1_size = tuple1.size();

        try {
            f = getHFileSupplier().getHFile(file_name);

        } catch (Exception e) {
            throw new FileScanException(e, "Create new heapfile failed");
        }

        try {
            scan = f.openScan();
        } catch (Exception e) {
            throw new FileScanException(e, "openScan() failed");
        }
    }

    @Override
    public T get_next() throws IOException, JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, Exception {
        {
            I rid = getIDSupplier().getID();
            ;

            while (true) {
                if ((tuple1 = scan.getNext(rid)) == null) {
                    return null;
                }

                tuple1.setHdr(in1_len, _in1, s_sizes);
                if (PredEval.Eval(OutputFilter, tuple1, null, _in1, null) == true) {
                    Projection.Project(tuple1, _in1, Jtuple, perm_mat, nOutFlds);
                    return Jtuple;
                }
            }
        }
    }

    @Override
    public void close() throws IOException, JoinsException, SortException, IndexException {
        {

            if (!closeFlag) {
                scan.closescan();
                closeFlag = true;
            }
        }
    }
}
