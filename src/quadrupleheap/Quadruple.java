package quadrupleheap;

import global.*;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

import java.io.IOException;
import java.util.stream.IntStream;

public class Quadruple extends Tuple {
    private EID subject;
    private PID predicate;
    private EID object;
    private float value;
    private static final short numberOfFields = 7;
    private static final AttrType intType = new AttrType(AttrType.attrInteger);
    private static final AttrType floType = new AttrType(AttrType.attrReal);
    private static final AttrType[] headerTypes = new AttrType[]{intType,intType,intType,intType,intType,intType,floType};
    private static final int[] offsets = new int[]{0,4,8,12,16,20,24};

    public Quadruple() {
        this(max_size);
    }

    public Quadruple(byte [] aQuadruple, int offset, int length) {
        super(aQuadruple,offset,length);
        try {
            this.setAttributes();
        } catch (IOException | FieldNumberOutOfBoundException e) {
            e.printStackTrace();
        }
    }
    private void setAttributes() throws IOException, FieldNumberOutOfBoundException {
        if(this.getLength()>=28){
            this.subject = new EID(new PageId(Convert.getIntValue(offsets[0],super.getData())),Convert.getIntValue(offsets[1],super.getData()));
            this.predicate = new PID(new PageId(Convert.getIntValue(offsets[2],super.getData())),Convert.getIntValue(offsets[3],super.getData()));
            this.object = new EID(new PageId(Convert.getIntValue(offsets[4],super.getData())),Convert.getIntValue(offsets[5],super.getData()));
            this.value = Convert.getFloValue(offsets[6],super.getData());
        }
    }

    public Quadruple(EID subject, PID predicate, EID object, float value) throws IOException, FieldNumberOutOfBoundException, InvalidTupleSizeException, InvalidTypeException {
        this();
        this.setSubject(subject);
        this.setPredicate(predicate);
        this.setObject(object);
        this.setValue(value);
    }

    public Quadruple(Quadruple fromQuadruple) throws InvalidTupleSizeException {
        this(fromQuadruple.getQuadrupleByteArray(),fromQuadruple.getLength(),0);
    }

    public byte[] getQuadrupleByteArray() {
        byte[] ret = new byte[28];
        try {
            if(this.subject!=null){
                Convert.setIntValue(this.subject.getPageNo().pid,offsets[0],ret);
                Convert.setIntValue(this.subject.getSlotNo(),offsets[1],ret);
            }
            if(this.predicate!=null){
                Convert.setIntValue(this.predicate.getPageNo().pid,offsets[2],ret);
                Convert.setIntValue(this.predicate.getSlotNo(),offsets[3],ret);
            }
            if(this.object!=null){
                Convert.setIntValue(this.object.getPageNo().pid,offsets[4],ret);
                Convert.setIntValue(this.object.getSlotNo(),offsets[5],ret);
            }
            Convert.setFloValue(this.value,offsets[6],ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public short noOfFlds() {
        return super.noOfFlds();
    }

    public short[] copyFldOffset() {
        return super.copyFldOffset();
    }

    public int getLength() {
        return super.getLength();
    }
    //TODO: Check this
    public Quadruple(int size) {
        this(new byte[size],0,size);

        //not handled setAttributes here
    }
    public void tupleCopy(Quadruple quadruple){
        super.tupleCopy(quadruple);
    }
    public void quadrupleCopy(Quadruple fromQuadruple) throws IOException, FieldNumberOutOfBoundException {
        this.tupleCopy(fromQuadruple);
    }

    public LID getGenericObjectFromByteArray(int pidFld, int slotNoFld) throws FieldNumberOutOfBoundException, IOException {
        LID result;
        int genericObjectPid = getIntFld(pidFld);
        int genericObjectSlotNo = getIntFld(slotNoFld);
        return new LID(new PageId(genericObjectPid), genericObjectSlotNo);
    }

    public short size() {
        return super.size();
    }

    public EID getSubject() {
        return subject;
    }

    public PID getPredicate() {
        return predicate;
    }

    public EID getObject() {
        return object;
    }

    public float getValue() {
        return value;
    }

    public void setSubject(EID subject) throws IOException, FieldNumberOutOfBoundException {
        this.subject = subject;
    }

    public void setPredicate(PID predicate) throws IOException, FieldNumberOutOfBoundException {
        this.predicate = predicate;
    }

    public void setObject(EID object) throws IOException, FieldNumberOutOfBoundException {
        this.object = object;
    }

    public void setValue(float value) throws IOException, FieldNumberOutOfBoundException {
        this.value = value;
    }


    public int getIntFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        return super.getIntFld(fldNo);
    }

    public float getFloFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        return super.getFloFld(fldNo);
    }

    public String getStrFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        return super.getStrFld(fldNo);
    }

    public char getCharFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        return super.getCharFld(fldNo);
    }

    public Quadruple setIntFld(int fldNo, int val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple) super.setIntFld(fldNo,val);
    }

    public Quadruple setFloFld(int fldNo, float val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple)  super.setFloFld(fldNo,val);
    }

    public Quadruple setStrFld(int fldNo, String val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple) super.setStrFld(fldNo,val);
    }

    public byte[] returnQuadrupleByteArray() {
        return super.getTupleByteArray();
    }

    public int getOffset() {
        return super.getOffset();
    }

    public void tupleSet(byte[] record,int offset, int length) throws InvalidTupleSizeException {
        super.tupleSet(record,offset,length);
    }
    public void quadrupleSet(byte [] record, int offset, int length) throws InvalidTupleSizeException {
        this.tupleSet(record,offset,length);
    }
}