package quadrupleheap;

import global.*;
import heap.FieldNumberOutOfBoundException;

import java.io.IOException;

public class Quadruple implements GlobalConst {
    private EID subject;
    private PID predicate;
    private EID object;
    private double value;
    public static final int max_size = MINIBASE_PAGESIZE;
    private byte [] data;
    private int quadruple_offset;
    private int quadruple_length;
    private final short fldCnt = 4;
    private short [] fldOffset;

    public Quadruple() {
        data = new byte[max_size];
        quadruple_offset = 0;
        quadruple_length = max_size;
    }

    public Quadruple(byte [] aQuadruple, int offset, int length) {
        data = aQuadruple;
        quadruple_offset = offset;
        quadruple_length = length;
    }

    public Quadruple(Quadruple fromQuadruple) {
        data = fromQuadruple.getQuadrupleByteArray();
        quadruple_length = fromQuadruple.getLength();
        quadruple_offset = 0;
        fldOffset = fromQuadruple.copyFldOffset();
    }

    public byte [] getQuadrupleByteArray() {
        byte [] quadruple_copy = new byte [quadruple_length];
        System.arraycopy(data, quadruple_offset, quadruple_copy, 0, quadruple_length);
        return quadruple_copy;
    }

    public short noOfFlds() {
        return fldCnt;
    }

    public short[] copyFldOffset() {
        short[] newFldOffset = new short[fldCnt + 1];
        for (int i=0; i<=fldCnt; i++) {
            newFldOffset[i] = fldOffset[i];
        }
        return newFldOffset;
    }

    public int getLength() {
        return quadruple_length;
    }

    public Quadruple(int size) {
        data = new byte[size];
        quadruple_offset = 0;
        quadruple_length = size;
    }

    public void quadrupleCopy(Quadruple fromQuadruple) {
        byte [] temparray = fromQuadruple.getQuadrupleByteArray();
        System.arraycopy(temparray, 0, data, quadruple_offset, quadruple_length);
    }

    public void quadrupleInit(byte [] aQuadruple, int offset, int length) {
        data = aQuadruple;
        quadruple_offset = offset;
        quadruple_length = length;
    }

    public void quadrupleSet(byte [] record, int offset, int length) {
        System.arraycopy(record, offset, data, 0, length);
        quadruple_offset = 0;
        quadruple_length = length;
    }

    public LID getObjectFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException, ClassNotFoundException {
        LID val;
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            val = Convert.getLIDValue(fldOffset[fldNo -1], data, fldOffset[fldNo] - fldOffset[fldNo -1]);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public Quadruple setLIDFld(int fldNo, LID val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            Convert.setLIDValue (val, fldOffset[fldNo -1], data);
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public double getDoubleFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        double val;
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            val = Convert.getDoubleValue(fldOffset[fldNo -1], data);
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
        final int fldNo = 4;
        double val;
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            val = Convert.getDoubleValue(fldOffset[fldNo -1], data);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public short size() {
        return ((short) (fldOffset[fldCnt] - quadruple_offset));
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
}
