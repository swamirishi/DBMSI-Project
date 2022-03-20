package quadrupleheap;

import global.*;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

import java.io.IOException;
import java.util.stream.IntStream;

public class Quadruple extends Tuple {
    
    private static final short numberOfFields = 7;
    private static final AttrType intType = new AttrType(AttrType.attrInteger);
    private static final AttrType floType = new AttrType(AttrType.attrReal);
    public static final AttrType[] headerTypes = new AttrType[]{intType,intType,intType,intType,intType,intType,floType};
    public static final short[] strSizes = new short[]{0,0,0,0,0,0,0};
    private static int SUBJECT_PG_NO_FLD = 1;
    private static int SUBJECT_SLOT_FLD = 2;
    private static int OBJECT_PG_NO_FLD = 3;
    private static int OBJECT_SLOT_FLD = 4;
    private static int PREDICATE_PG_NO_FLD = 5;
    private static int PREDICATE_SLOT_FLD = 6;
    private static int VALUE_FLD = 7;
    private boolean hdrSet = false;
    
    
    
    public Quadruple(EID subject, PID predicate, EID object, float value) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this();
        this.setSubject(subject);
        this.setPredicate(predicate);
        this.setObject(object);
        this.setValue(value);

    }
    public Quadruple() {
        super(max_size);
    }

    public Quadruple(byte [] aQuadruple, int offset, int length) {
        super(aQuadruple,offset,length);
    }

//    public Quadruple(EID subject, PID predicate, EID object, float value) throws IOException, FieldNumberOutOfBoundException, InvalidTupleSizeException, InvalidTypeException {
//        this.setSubject(subject);
//        this.setPredicate(predicate);
//        this.setObject(object);
//        this.setValue(value);
//        this.quadrupleCopy(this);
//    }

    public Quadruple(Quadruple fromQuadruple) throws InvalidTupleSizeException {
        super(fromQuadruple);
    }

    public byte[] getQuadrupleByteArray() {
        return super.getTupleByteArray();
    }
    
    //TODO: Check this
    public Quadruple(int size) {
        super(size);

        //not handled setAttributes here
    }
    
    public void quadrupleCopy(Quadruple fromQuadruple) throws IOException, FieldNumberOutOfBoundException {
        super.tupleCopy(fromQuadruple);
    }

    public EID getSubject(){
        try {
            setHdrIfNotSet();
            return new EID(new PageId(super.getIntFld(SUBJECT_PG_NO_FLD)),super.getIntFld(SUBJECT_SLOT_FLD));
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

    public PID getPredicate(){
        try {
            setHdrIfNotSet();
            return new PID(new PageId(super.getIntFld(PREDICATE_PG_NO_FLD)),super.getIntFld(PREDICATE_SLOT_FLD));
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

    public EID getObject(){
        try {
            setHdrIfNotSet();
            return new EID(new PageId(super.getIntFld(OBJECT_PG_NO_FLD)),super.getIntFld(OBJECT_SLOT_FLD));
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
    public void setHdrIfNotSet() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        if(!hdrSet){
            this.setHdr();
        }
    }
    public float getValue(){
        try {
            setHdrIfNotSet();
            return super.getFloFld(VALUE_FLD);
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

    public void setSubject(EID subject){
        try {
            setHdrIfNotSet();
            super.setIntFld(SUBJECT_PG_NO_FLD,subject.getPageNo().pid);
            super.setIntFld(SUBJECT_SLOT_FLD,subject.getSlotNo());
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

    public void setPredicate(PID predicate){
        try {
            setHdrIfNotSet();
            super.setIntFld(PREDICATE_PG_NO_FLD,predicate.getPageNo().pid);
            super.setIntFld(PREDICATE_SLOT_FLD,predicate.getSlotNo());
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

    public void setObject(EID object){
        try {
            setHdrIfNotSet();
            super.setIntFld(OBJECT_PG_NO_FLD,object.getPageNo().pid);
            super.setIntFld(OBJECT_SLOT_FLD,object.getSlotNo());
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

    public void setValue(float value){
        try {
            setHdrIfNotSet();
            super.setFloFld(VALUE_FLD,value);
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

    public byte[] returnQuadrupleByteArray() {
        return super.returnTupleByteArray();
    }
    public void quadrupleSet(byte [] record, int offset, int length) throws InvalidTupleSizeException, FieldNumberOutOfBoundException, IOException {
        this.tupleSet(record,offset,length);
    }
    public void setHdr(short numFlds,  AttrType types[], short strSizes[]) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        hdrSet = true;
        super.setHdr(numFlds,types,strSizes);
    }
    public void setHdr() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this.setHdr(numberOfFields,headerTypes,strSizes);
    }

    public String toString(){
        //make sure it variables are not null
        return this.getSubject().toString() + " " + this.getPredicate().toString() + " " + this.getObject().toString() + " " + this.getValue();
    }
}