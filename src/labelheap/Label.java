package labelheap;

import global.*;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import quadrupleheap.Quadruple;

import java.io.*;

import static global.GlobalConst.MINIBASE_PAGESIZE;

/*
    The design of the label class could be more simple i.e we could do away with attributes like fldOffset, fldCnt,
    label_offset, etc. but the reason why that is not done is because we need a way to create a Label object of size 12
    as that is the size DataPageInfo class expects. We would want LHFPage to be able to store DataPageInfo objects and
    Label objects
 */

public class Label extends Tuple {
    private String label;

    public Label() {
        super();
    }

    public Label(byte [] aQuadruple, int offset, int length) {
        super(aQuadruple,offset,length);
    }

    public Label(String label) throws IOException {
        setLabel(label);
    }

    public void setLabel(String label) throws IOException {
        this.label = label;
        this.setData(Convert.convertToBytes(label));
        this.setTuple_offset(0);
        this.setFldOffset(new short[]{(short) this.getOffset()});
        this.setTuple_length(this.getData().length);
    }

    public void printLabel(){
        System.out.println("### Printing label ###");
        System.out.println(this.label);
        System.out.println("### END : Printing label ###");
    }

    public String getLabel() {
        return label;
    }

    public int getLength() {
        return super.getLength();
    }

    public byte[] returnLabelByteArray() {
        return super.returnTupleByteArray();
    }

    public byte [] getLabelByteArray() {
        return super.getTupleByteArray();
    }

    public void labelCopy(Label newLabel) {
        super.tupleCopy(newLabel);
    }
}
