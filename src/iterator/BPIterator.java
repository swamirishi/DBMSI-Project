package iterator;

import bufmgr.PageNotReadException;
import global.AttrType;
import heap.*;
import index.IndexException;

import java.io.IOException;

public class BPIterator extends FileScan{
    /**
     * constructor
     *
     * @param file_name  heapfile to be opened
     * @param in1
     * @param s1_sizes
     * @param len_in1    number of attributes in the input tuple
     * @param n_out_flds number of fields in the out tuple
     * @param proj_list  shows what input fields go where in the output tuple
     * @param outFilter  select expressions
     * @throws IOException         some I/O fault
     * @throws FileScanException   exception from this class
     * @throws TupleUtilsException exception from this class
     * @throws InvalidRelation     invalid relation
     */
    public BPIterator(String file_name, AttrType[] in1, short[] s1_sizes, short len_in1, int n_out_flds, FldSpec[] proj_list, CondExpr[] outFilter) throws IOException, FileScanException, TupleUtilsException, InvalidRelation {
        super(file_name, in1, s1_sizes, len_in1, n_out_flds, proj_list, outFilter);
    }

    @Override
    public Tuple get_next() throws PageNotReadException, UnknowAttrType, FieldNumberOutOfBoundException, PredEvalException, WrongPermat, InvalidTupleSizeException, JoinsException, IOException, InvalidTypeException {
        return super.get_next();
    }

    @Override
    public void close() {
        super.close();
    }
}
