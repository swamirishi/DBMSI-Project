package iterator.bp;

import basicpatternheap.BasicPattern;
import heap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import index.IndexException;
import iterator.*;
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
 * open a heapfile and according to the condition expression to get
 * output file, call get_next to get all tuples
 */
public class BPFileScan extends FileScanI<BPID, BasicPattern> {
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
    public BPFileScan(String file_name,
                      AttrType in1[],
                      short s1_sizes[],
                      short len_in1,
                      int n_out_flds,
                      FldSpec[] proj_list,
                      CondExpr[] outFilter) throws IOException, FileScanException, TupleUtilsException, InvalidRelation {
        super(file_name, in1, s1_sizes, len_in1, n_out_flds, proj_list, outFilter);
    }
    
    /**
     * @return shows what input fields go where in the output tuple
     */
    public FldSpec[] show() {
        return perm_mat;
    }
    
    
}


