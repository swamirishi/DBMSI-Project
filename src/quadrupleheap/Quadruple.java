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

public class Quadruple implements GlobalConst {
    /**
        Quadruple class needs to store 4 attributes. EID is represented by 2 integers pid and slotNo.
        pid is stored first and then slotNo. Field count is 1 indexed.
        Quadruple : [Subject, Predicate, Object, Value]
     */
    private EID subject;
    private PID predicate;
    private EID object;
    private double value;
    public static final int max_size = MINIBASE_PAGESIZE;
    public byte [] data;
    private int quadruple_offset;
    private int quadruple_length;
    private static short fldCnt = 7;
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
        try {
            setAttributes();
        } catch (FieldNumberOutOfBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int writeAttributeArrayToByteArray
            (byte[] attrArray, int srcPos, byte[] data, int dstOffset, int length, short[] fldOffset, int fldIndex){
        System.arraycopy(attrArray, srcPos, data, dstOffset, length);
        fldOffset[fldIndex] = (short) dstOffset;
        return dstOffset + length;
    }

    public void setAttributes() throws FieldNumberOutOfBoundException, IOException {
        this.fldOffset = new short[]{0, 4, 8, 12, 16, 20, 24, 32};
        this.fldCnt = 7;
        int subjectPid = getIntFld(1);
        int subjectSlotNo = getIntFld(2);
        int predicatePid = getIntFld(3);
        int predicateSlotNo = getIntFld(4);
        int objectPid = getIntFld(5);
        int objectSlotNo = getIntFld(6);
        double value = getDoubleFld(7);
        this.subject = new EID(new PageId(subjectPid), subjectSlotNo);
        this.predicate = new PID(new PageId(predicatePid), predicateSlotNo);
        this.object = new EID((new PageId(objectPid)), objectSlotNo);
        this.value = value;
    }

    public void setByteArray() throws FieldNumberOutOfBoundException, IOException {
        int subjectPid = subject.getPageNo().pid;
        int subjectSlotNo = subject.getSlotNo();
        int predicatePid = predicate.pageNo.pid;
        int predicateSlotNo = predicate.slotNo;
        int objectPid = object.pageNo.pid;
        int objectSlotNo = object.slotNo;
        double value = this.value;
        fldCnt = 7;
        this.fldOffset = new short[]{0, 4, 8, 12, 16, 20, 24, 32};
        this.quadruple_length = 32;
        this.quadruple_offset = 0;
        data = new byte[this.quadruple_length];
        setIntFld(1, subjectPid);
        setIntFld(2, subjectSlotNo);
        setIntFld(3, predicatePid);
        setIntFld(4, predicateSlotNo);
        setIntFld(5, objectPid);
        setIntFld(6, objectSlotNo);
        setDoubleFld(7, value);
    }

    public Quadruple(EID subject, PID predicate, EID object, double value) throws IOException, FieldNumberOutOfBoundException {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
        this.value = value;
        setByteArray();

    }

    public Quadruple(Quadruple fromQuadruple) {
        data = fromQuadruple.getQuadrupleByteArray();
        quadruple_length = fromQuadruple.getLength();
        quadruple_offset = 0;
        fldOffset = fromQuadruple.copyFldOffset();
    }

    public byte [] getQuadrupleByteArray() {
        byte [] quadruple_copy = new byte [quadruple_length];
        System.arraycopy(data, 0, quadruple_copy, 0, quadruple_length);
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
        System.arraycopy(temparray, 0, data, 0, quadruple_length);
    }

    public LID getGenericObjectFromByteArray(int pidFld, int slotNoFld) throws FieldNumberOutOfBoundException, IOException {
        LID result;
        int genericObjectPid = getIntFld(pidFld);
        int genericObjectSlotNo = getIntFld(slotNoFld);
        return new LID(new PageId(genericObjectPid), genericObjectSlotNo);
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


    public int getIntFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        int val;
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            val = Convert.getIntValue(fldOffset[fldNo -1], data);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public float getFloFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        float val;
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            val = Convert.getFloValue(fldOffset[fldNo -1], data);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public String getStrFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        String val;
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            val = Convert.getStrValue(fldOffset[fldNo -1], data,
                    fldOffset[fldNo] - fldOffset[fldNo -1]); //strlen+2
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public char getCharFld(int fldNo)
            throws IOException, FieldNumberOutOfBoundException {
        char val;
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            val = Convert.getCharValue(fldOffset[fldNo -1], data);
            return val;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");

    }

    public Quadruple setIntFld(int fldNo, int val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            Convert.setIntValue (val, fldOffset[fldNo -1], data);
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public Quadruple setFloFld(int fldNo, float val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= fldCnt))
        {
            Convert.setFloValue (val, fldOffset[fldNo -1], data);
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");

    }

    public Quadruple setStrFld(int fldNo, String val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            Convert.setStrValue (val, fldOffset[fldNo -1], data);
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }

    public Quadruple setDoubleFld(int fldNo, double val)
            throws IOException, FieldNumberOutOfBoundException {
        if ( (fldNo > 0) && (fldNo <= fldCnt)) {
            Convert.setDoubleValue(val, fldOffset[fldNo -1], data);
            return this;
        }
        else
            throw new FieldNumberOutOfBoundException (null, "TUPLE:TUPLE_FLDNO_OUT_OF_BOUND");
    }


    /**
     * setHdr will set the header of this quadruple.
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
        Convert.setShortValue(numFlds, quadruple_offset, data);
        fldOffset = new short[numFlds+1];
        int pos = quadruple_offset+2;  // start position for fldOffset[]

        //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
        //another 1 for fldCnt
        fldOffset[0] = (short) ((numFlds +2) * 2 + quadruple_offset);

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

        quadruple_length = fldOffset[numFlds] - quadruple_offset;

        if(quadruple_length > max_size)
            throw new InvalidTupleSizeException (null, "TUPLE: TUPLE_TOOBIG_ERROR");
    }

    public byte [] returnQuadrupleByteArray()
    {
        return data;
    }

    public int getOffset() {
        return quadruple_offset;
    }
}
