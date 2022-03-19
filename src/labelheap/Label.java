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
        setAttributes();
    }
    private void setAttributes(){
        if(super.getLength()>=2){
            try {
                short length = Convert.getShortValue(this.getOffset(),this.getData());
                if(length>0 && super.getLength()>=2+length)
                    this.label = Convert.getStrValue(this.getOffset()+2,this.getData(),length);
                else if(length==0){
                    this.label = "";
                }else{
                    this.label = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Label(int size) {
        this(new byte[size],0,size);
        
        //not handled setAttributes here
    }
    public Label(String label) throws IOException, FieldNumberOutOfBoundException {
        this.label = label;
    }
    
    public Label(Label label) throws InvalidTupleSizeException {
        this(label.getTupleByteArray(),0,label.getLength());
    }
    
    public void setLabel(String label) throws FieldNumberOutOfBoundException, IOException {
        this.label = label;
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
    
    public byte[] returnLabelByteArray() throws IOException {
        return super.returnTupleByteArray();
    }
    
    public byte [] getLabelByteArray() throws IOException {
        if(this.label==null){
            byte[] data = new byte[2];
            Convert.setShortValue((short)-1,0,data);
            return data;
        }
        
        
        byte[] labelValue = Convert.getStrValueToBytes(label);
        byte[] data = new byte[labelValue.length+2];
        int length = labelValue.length;
        System.arraycopy (labelValue, 0, data, 2, length);
        Convert.setShortValue((short)length,0,data);
        return data;
    }
    
    public void labelCopy(Label newLabel) {
        super.tupleCopy(newLabel);
        this.setAttributes();
    }
    
    public void tupleSet(byte[] record,int offset, int length) throws InvalidTupleSizeException {
        super.tupleSet(record,offset,length);
        this.setAttributes();
    }
    public void labelSet(byte [] record, int offset, int length) throws InvalidTupleSizeException {
        this.tupleSet(record,offset,length);
    }
}