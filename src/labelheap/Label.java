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
    private static final short numberOfFields = 1;
    private static final AttrType stringType = new AttrType(AttrType.attrString);
    private static final AttrType[] headerTypes = new AttrType[]{stringType};

    public static final int MAX_LENGTH = 200;
    private static final short[] strLengths = new short[]{MAX_LENGTH};
    private static int LABEL_FLD = 1;
    private boolean hdrSet = false;
    public Label(String label) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this();
        this.setLabel(label);
    }
    public Label() {
        super();
    }

    public Label(byte [] labelbyteArray, int offset, int length) {
        super(labelbyteArray,offset,length);
    }

    public Label(int size) {
        super(size);
    }

    public Label(Label label) throws InvalidTupleSizeException {
        super(label);
    }
    public void setHdrIfNotSet() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        if(!hdrSet){
            this.setHdr();
        }
    }
    public void setLabel(String label){
        try {
            setHdrIfNotSet();
            this.setStrFld(LABEL_FLD,label);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FieldNumberOutOfBoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidTupleSizeException e) {
            throw new RuntimeException(e);
        } catch (InvalidTypeException e) {
            throw new RuntimeException(e);
        }
    }

    public void printLabel(){
        System.out.println("### Printing label ###");
        System.out.println(this.getLabel());
        System.out.println("### END : Printing label ###");
    }

    public String getLabel() {
        try {
            setHdrIfNotSet();
            return super.getStrFld(LABEL_FLD);
        } catch (IOException | FieldNumberOutOfBoundException | InvalidTupleSizeException | InvalidTypeException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] returnLabelByteArray() throws IOException {
        return super.returnTupleByteArray();
    }

    public byte [] getLabelByteArray() throws IOException {
        return super.getTupleByteArray();
    }

    public void labelCopy(Label newLabel) {
        super.tupleCopy(newLabel);
    }

    public void labelSet(byte [] record, int offset, int length) throws InvalidTupleSizeException {
        super.tupleSet(record,offset,length);
    }

    public void setHdr(short numFlds,  AttrType types[], short strSizes[]) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        hdrSet = true;
        super.setHdr(numFlds,types,strSizes);
    }
    public void setHdr() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this.setHdr(numberOfFields,headerTypes,strLengths);
    }
}