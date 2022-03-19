package labelheap;

import global.*;
import heap.FieldNumberOutOfBoundException;
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
    private static final short numberOfFields = 1;
    private static final AttrType stringType = new AttrType(AttrType.attrString);
    private static final AttrType[] headerTypes = new AttrType[]{stringType};
    private static final short[] strLengths = new short[]{50};

    public Label() {
        this(max_size);
    }

    public Label(byte [] labelbyteArray, int offset, int length) {
        super(labelbyteArray,offset,length);
        try {
            this.setHdr(numberOfFields,headerTypes,strLengths);
            this.setAttributes();
        } catch (IOException | InvalidTypeException | InvalidTupleSizeException | FieldNumberOutOfBoundException e) {
            e.printStackTrace();
        }
    }
    private void setAttributes() throws IOException, FieldNumberOutOfBoundException {
        this.label = this.getStrFld(1);
    }
    
    public Label(int size) {
        this(new byte[size],0,size);
        
        //not handled setAttributes here
    }
    public Label(String label) throws IOException, FieldNumberOutOfBoundException {
        this();
        this.setLabel(label);
    }
    
    public Label(Label label) throws InvalidTupleSizeException {
        this(label.getLabelByteArray(),label.getLength(),0);
    }

    public void setLabel(String label) throws FieldNumberOutOfBoundException, IOException {
        this.label = label;
        this.setStrFld(1,label);
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
    
    public void tupleSet(byte[] record,int offset, int length) throws InvalidTupleSizeException {
        super.tupleSet(record,offset,length);
    }
    public void labelSet(byte [] record, int offset, int length) throws InvalidTupleSizeException {
        this.tupleSet(record,offset,length);
    }
}
