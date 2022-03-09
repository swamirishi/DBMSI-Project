package labelheap;

import global.*;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import quadrupleheap.Quadruple;

import java.io.*;

import static global.GlobalConst.MINIBASE_PAGESIZE;

/*
    The design of the label class could be more simple i.e we could do away with attributes like fldOffset, fldCnt,
    label_offset, etc. but the reason why that is not done is because we need a way to create a Label object of size 12
    as that is the size DataPageInfo class expects. We would want LHFPage to be able to store DataPageInfo objects and
    Label objects
 */

public class Label {
    private String label;
    public static final int max_size = MINIBASE_PAGESIZE;
    private byte [] data;
    private int label_offset = 0;
    private int label_length;
    private short fldCnt = 1;
    private short [] fldOffset;


    public Label() {
        data = new byte[max_size];
        label_offset = 0;
        label_length = max_size;
    }

    public Label(byte [] aQuadruple, int offset, int length) {
        data = aQuadruple;
        label_offset = offset;
        label_length = length;
    }

    public Label(String label) throws IOException {
        setLabel(label);
    }

    public void setLabel(String label) throws IOException {
        this.label = label;
        this.data = Convert.convertToBytes(label);
        this.label_offset = 0;
        this.fldOffset = new short[1];
        this.fldOffset[0] = (short) label_offset;
        this.label_length = data.length;
    }

    public void printLabel(){
        System.out.println("### Printing label ###");
        System.out.println(this.label);
        System.out.println("### END : Printing label ###");
    }

    public String getLabel() {
        return label;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return label_length;
    }

    public byte[] returnLabelByteArray() {
        return data;
    }

    public int getOffset() {
        return label_offset;
    }

    public byte [] getLabelByteArray() {
        byte [] label_copy = new byte [label_length];
        System.arraycopy(data, 0, label_copy, 0, label_length);
        return label_copy;
    }

    public void labelCopy(Label newLabel) {
        byte [] temparray = newLabel.getLabelByteArray();
        System.arraycopy(temparray, 0, data, label_offset, label_length);
    }

    /**
     * setHdr will set the header of this label.
     *
     * @param	numFlds	  number of fields
     * @param	types[]	  contains the types that will be in this tuple
     * @param	strSizes[]      contains the sizes of the string
     *
     * @exception IOException I/O errors
     * @exception InvalidTypeException Invalid tupe type
     * @exception InvalidTupleSizeException Tuple size too big
     *
     */

    public void setHdr (short numFlds,  AttrType types[], short strSizes[])
            throws IOException, InvalidTypeException, InvalidTupleSizeException
    {
        if((numFlds +2)*2 > max_size)
            throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");

        fldCnt = numFlds;
        Convert.setShortValue(numFlds, label_offset, data);
        fldOffset = new short[numFlds+1];
        int pos = label_offset+2;  // start position for fldOffset[]

        //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
        //another 1 for fldCnt
        fldOffset[0] = (short) ((numFlds +2) * 2 + label_offset);

        Convert.setShortValue(fldOffset[0], pos, data);
        pos +=2;
        short strCount =0;
        short incr;
        int i;

        for (i=1; i<numFlds; i++)
        {
            switch(types[i-1].attrType) {

                case AttrType.attrInteger:
                    incr = 4;
                    break;

                case AttrType.attrReal:
                    incr =4;
                    break;

                case AttrType.attrString:
                    incr = (short) (strSizes[strCount] +2);  //strlen in bytes = strlen +2
                    strCount++;
                    break;

                default:
                    throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
            }
            fldOffset[i]  = (short) (fldOffset[i-1] + incr);
            Convert.setShortValue(fldOffset[i], pos, data);
            pos +=2;

        }
        switch(types[numFlds -1].attrType) {

            case AttrType.attrInteger:
                incr = 4;
                break;

            case AttrType.attrReal:
                incr =4;
                break;

            case AttrType.attrString:
                incr =(short) ( strSizes[strCount] +2);  //strlen in bytes = strlen +2
                break;

            default:
                throw new InvalidTypeException (null, "TUPLE: TUPLE_TYPE_ERROR");
        }

        fldOffset[numFlds] = (short) (fldOffset[i-1] + incr);
        Convert.setShortValue(fldOffset[numFlds], pos, data);

        label_length = fldOffset[numFlds] - label_offset;

        if(label_length > max_size)
            throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
    }
}
