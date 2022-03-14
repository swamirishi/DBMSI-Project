package quadrupleheap;

import global.*;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Quadruple extends Tuple {
    private EID subject;
    private PID predicate;
    private EID object;
    private double value;
    
    public Quadruple() {
        super();
        super.setFldCnt((short) 4);
    }

    public Quadruple(byte [] aQuadruple, int offset, int length) {
        super(aQuadruple,offset,length);
        super.setFldCnt((short) 4);
    }

    public int writeAttributeArrayToByteArray
            (byte[] attrArray, int srcPos, byte[] data, int dstOffset, int length, short[] fldOffset, int fldIndex){
        System.arraycopy(attrArray, srcPos, data, dstOffset, length);
        fldOffset[fldIndex] = (short) dstOffset;
        return dstOffset + length;
    }

    public Quadruple(EID subject, PID predicate, EID object, double value) throws IOException {
        super.setFldCnt((short) 4);
        byte[] subjectArray = Convert.convertToBytes(subject.returnLid());
        byte[] predicateArray = Convert.convertToBytes(predicate.returnLid());
        byte[] objectArray = Convert.convertToBytes(object.returnLid());
        OutputStream out = new ByteArrayOutputStream();
        DataOutputStream outstr = new DataOutputStream (out);
        outstr.writeDouble(value);
        byte[] doubleArray = ((ByteArrayOutputStream) out).toByteArray();
        int size = subjectArray.length + predicateArray.length + objectArray.length + doubleArray.length;
        super.setData(new byte[size]);
        int quadruple_offset  =0;
        super.setFldOffset(new short[super.getFldCnt() + 1]);
        
        quadruple_offset +=
                writeAttributeArrayToByteArray(
                        subjectArray, 0, super.getData(), quadruple_offset, subjectArray.length, super.getFldOffset(), 0);
        quadruple_offset +=
                writeAttributeArrayToByteArray(
                        predicateArray, 0, super.getData(), quadruple_offset, predicateArray.length, super.getFldOffset(), 1);
        quadruple_offset +=
                writeAttributeArrayToByteArray(
                        objectArray, 0, super.getData(), quadruple_offset, objectArray.length,  super.getFldOffset(), 2);
        quadruple_offset +=
                writeAttributeArrayToByteArray(
                        doubleArray, 0, super.getData(), quadruple_offset, doubleArray.length,  super.getFldOffset(), 3);
        super.setTuple_offset(quadruple_offset);
        super.getFldOffset()[super.getFldOffset().length-1] = (short) quadruple_offset;
        super.setTuple_length(super.getLength());
        
    }

    public Quadruple(Quadruple fromQuadruple) {
        super(fromQuadruple);
        super.setFldCnt((short) 4);
    }

    public byte [] getQuadrupleByteArray() {
        return super.getTupleByteArray();
    }

    public int getLength() {
        return super.getLength();
    }

    public Quadruple(int size) {
        super(size);
        super.setFldCnt((short) 4);
    }

    public void quadrupleCopy(Quadruple fromQuadruple) {
        super.tupleCopy(fromQuadruple);
    }

    public void quadrupleSet(byte [] record, int offset, int length) {
        super.tupleSet(record, offset, length);
    }

    public LID getLIDFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException {
        LID val;
        if ( (fldNo > 0) && (fldNo <= super.noOfFlds())) {
            val = Convert.getLIDValue(super.getFldOffset()[fldNo -1], super.getData(), super.getFldOffset()[fldNo] - super.getFldOffset()[fldNo -1]);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public Quadruple setLIDFld(int fldNo, LID val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= super.noOfFlds())) {
            Convert.setLIDValue (val, super.getFldOffset()[fldNo -1], super.getData());
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public double getDoubleFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        double val;
        if ( (fldNo > 0) && (fldNo <= super.noOfFlds())) {
            val = Convert.getDoubleValue(super.getFldOffset()[fldNo -1], super.getData());
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    /**
     * This is the overloaded method of getDoubleFld(int fldNo) and this method will most likely be used the most
     * as we know for a fact that only the 4th field can be a double.
     * @return
     * @throws IOException
     * @throws FieldNumberOutOfBoundException
     */
    public double getDoubleFld()
            throws IOException, FieldNumberOutOfBoundException {
        return this.getDoubleFld(4);
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

    public double getValue() {
        return value;
    }

    public void setSubject(EID subject) {
        this.subject = subject;
    }

    public void setPredicate(PID predicate) {
        this.predicate = predicate;
    }

    public void setObject(EID object) {
        this.object = object;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    public Quadruple setIntFld(int fldNo, int val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple) super.setIntFld(fldNo, val);
    }

    public Quadruple setFloFld(int fldNo, float val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple) super.setFloFld(fldNo,val);
    }

    public Quadruple setStrFld(int fldNo, String val)
            throws IOException, FieldNumberOutOfBoundException {
        return (Quadruple) super.setStrFld(fldNo, val);
    }

    public byte [] returnQuadrupleByteArray() {
        return super.returnTupleByteArray();
    }

    
}
