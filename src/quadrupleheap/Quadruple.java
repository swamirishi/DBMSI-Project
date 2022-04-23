package quadrupleheap;

import basicpatternheap.BasicPattern;
import global.*;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

import java.io.IOException;
import java.util.stream.IntStream;

public class Quadruple extends BasicPattern {

    public static final short numberOfFields = 8;
    private static final AttrType intType = new AttrType(AttrType.attrInteger);
    private static final AttrType floType = new AttrType(AttrType.attrReal);
    public static final AttrType[] headerTypes = new AttrType[]{intType,floType,intType,intType,intType,intType,intType,intType};
    public static final short[] strSizes = new short[]{0,0,0,0,0,0,0,0};
    public static final int SUBJECT_NODE_INDEX = 1;
    public static final int OBJECT_NODE_INDEX = 2;
    public static final int PREDICTE_NODE_INDEX = 3;


    public Quadruple(EID subject, PID predicate, EID object, float value) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this();
        this.setSubject(subject);
        this.setPredicate(predicate);
        this.setObject(object);
        this.setValue(value);

    }
    public Quadruple() {
        super(max_size);
        try {
            this.setNumberOfNodes(numberOfFields);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FieldNumberOutOfBoundException e) {
            e.printStackTrace();
        } catch (InvalidTupleSizeException e) {
            e.printStackTrace();
        } catch (InvalidTypeException e) {
            e.printStackTrace();
        }
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

    public Quadruple(Quadruple fromQuadruple){
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
        return super.getNode(SUBJECT_NODE_INDEX).returnEID();
    }

    public PID getPredicate(){
        return super.getNode(PREDICTE_NODE_INDEX).returnPID();
    }

    public EID getObject(){
        return super.getNode(OBJECT_NODE_INDEX).returnEID();
    }
    
    public float getValue(){
        return super.getValue();
    }

    public void setSubject(EID subject) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        super.setNode(SUBJECT_NODE_INDEX,subject.returnNid());
    }

    public void setPredicate(PID predicate) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        super.setNode(PREDICTE_NODE_INDEX,predicate.returnNid());
    }

    public void setObject(EID object) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        super.setNode(OBJECT_NODE_INDEX, object.returnNid());
    }

    public void setValue(float value){
        super.setValue(value);
    }

    @Deprecated
    public byte[] returnQuadrupleByteArray() {
        return super.returnTupleByteArray();
    }
    public void quadrupleSet(byte [] record, int offset, int length) throws InvalidTupleSizeException, FieldNumberOutOfBoundException, IOException {
        this.tupleSet(record,offset,length);
    }
    
    public void setHdr() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this.setHdr(numberOfFields,headerTypes,strSizes);
    }

    public String toString(){
        //make sure it variables are not null
        return this.getSubject().toString() + " " + this.getPredicate().toString() + " " + this.getObject().toString() + " " + this.getValue();
    }
}