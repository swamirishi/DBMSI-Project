package iterator;


import basicpatternheap.BasicPattern;
import heap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import index.IndexException;
import iterator.interfaces.FileScanI;
import utils.supplier.hfile.BPIDHFileSupplier;
import utils.supplier.hfile.HFileSupplier;
import utils.supplier.hfile.RIDHFileSupplier;
import utils.supplier.id.BPIDSupplier;
import utils.supplier.id.IDSupplier;
import utils.supplier.id.RIDSupplier;
import utils.supplier.tuple.BPIDTupleSupplier;
import utils.supplier.tuple.RIDTupleSupplier;
import utils.supplier.tuple.TupleSupplier;


import java.lang.*;
import java.io.*;

/**
 *open a heapfile and according to the condition expression to get
 *output file, call get_next to get all tuples
 */
public class BPFileScan extends FileScanI<BPID, BasicPattern>
{
    private AttrType[] _in1;
    private short in1_len;
    private short[] s_sizes;
    private Heapfile f;
    private Scan scan;
    private Tuple     tuple1;
    private Tuple    Jtuple;
    private int        t1_size;
    private int nOutFlds;
    private CondExpr[]  OutputFilter;
    public FldSpec[] perm_mat;


    @Override
    public IDSupplier<BPID> getIDSupplier() {
        return BPIDSupplier.getSupplier();
    }

    @Override
    public TupleSupplier<BasicPattern> getTupleSupplier() {
        return BPIDTupleSupplier.getSupplier();
    }

    @Override
    public HFileSupplier<BPID, BasicPattern> getHFileSupplier() {
        return BPIDHFileSupplier.getSupplier();
    }

    /**
     *constructor
     *@param file_name heapfile to be opened
     *@param in1[]  array showing what the attributes of the input fields are.
     *@param s1_sizes[]  shows the length of the string fields.
     *@param len_in1  number of attributes in the input tuple
     *@param n_out_flds  number of fields in the out tuple
     *@param proj_list  shows what input fields go where in the output tuple
     *@param outFilter  select expressions
     *@exception IOException some I/O fault
     *@exception FileScanException exception from this class
     *@exception TupleUtilsException exception from this class
     *@exception InvalidRelation invalid relation
     */
    public  BPFileScan (String  file_name,
                      AttrType in1[],
                      short s1_sizes[],
                      short     len_in1,
                      int n_out_flds,
                      FldSpec[] proj_list,
                      CondExpr[]  outFilter
    )
            throws IOException,
            FileScanException,
            TupleUtilsException,
            InvalidRelation
    {
        super(file_name, in1, s1_sizes, len_in1, n_out_flds, proj_list, outFilter);
    }

    /**
     *@return shows what input fields go where in the output tuple
     */
    public FldSpec[] show()
    {
        return perm_mat;
    }

    /**
     *@return the result tuple
     *@exception JoinsException some join exception
     *@exception IOException I/O errors
     *@exception InvalidTupleSizeException invalid tuple size
     *@exception InvalidTypeException tuple type not valid
     *@exception PageNotReadException exception from lower layer
     *@exception PredEvalException exception from PredEval class
     *@exception UnknowAttrType attribute type unknown
     *@exception FieldNumberOutOfBoundException array out of bounds
     *@exception WrongPermat exception for wrong FldSpec argument
     */
    public BasicPattern get_next()
            throws Exception {
        return super.get_next();
    }

    /**
     *implement the abstract method close() from super class Iterator
     *to finish cleaning up
     */
    public void close() throws SortException, IndexException, IOException, JoinsException {
        super.close();
    }

}


