package basicpatternheap;

import global.*;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicPattern extends Tuple {
    public static final short numberOfNodes = 50;
    public static final short numberOfFields =2*numberOfNodes+2;
    private static final AttrType intType = new AttrType(AttrType.attrInteger);
    private static final AttrType floType = new AttrType(AttrType.attrReal);
    public static final AttrType[] headerTypes = IntStream.range(0, numberOfFields).mapToObj(i->i==2?floType:intType).collect(
            Collectors.toList()).toArray(new AttrType[numberOfFields]);
    public static final short[] strSizes = new short[numberOfFields];

    private static final int NUMBER_OF_NODES_FLD = 1;
    private static final int VALUE_FLD = 2;
    private boolean hdrSet = false;
    
    public BasicPattern() {
        super(max_size);
        this.clear();
    }

    public BasicPattern(byte [] aBasicPattern, int offset, int length) {
        super(aBasicPattern,offset,length);
    }

//    public Quadruple(EID subject, PID predicate, EID object, float value) throws IOException, FieldNumberOutOfBoundException, InvalidTupleSizeException, InvalidTypeException {
//        this.setSubject(subject);
//        this.setPredicate(predicate);
//        this.setObject(object);
//        this.setValue(value);
//        this.quadrupleCopy(this);
//    }

    public BasicPattern(BasicPattern fromBasicPattern) throws InvalidTupleSizeException {
        super(fromBasicPattern);
    }

    public byte[] getBasicPatternByteArray() {
        return super.getTupleByteArray();
    }

    //TODO: Check this
    public BasicPattern(int size) {
        super(size);

        //not handled setAttributes here
    }

    public void basicPatternCopy(BasicPattern fromBasicPattern) throws IOException, FieldNumberOutOfBoundException {
        super.tupleCopy(fromBasicPattern);
    }
    public int getNumberOfNodes(){
        try {
            setHdrIfNotSet();
            return super.getIntFld(NUMBER_OF_NODES_FLD);
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
    private int getOffset(int nodeIndex){
        return 2*nodeIndex+1;
    }

    //TODO in getNode on nodeIndex=-1 I want to return float
    public NID getNode(int nodeIndex){
        try {
            setHdrIfNotSet();
            if(nodeIndex>=this.getNumberOfNodes()){
                throw new RuntimeException("Node Idx ="+nodeIndex+">="+this.getNumberOfNodes());
            }
            int offset = getOffset(nodeIndex);
            return new NID(new PageId(super.getIntFld(offset)),super.getIntFld(offset+1));
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
    private void setNumberOfNodes(int numberOfNodes) throws IOException, FieldNumberOutOfBoundException {
        super.setIntFld(NUMBER_OF_NODES_FLD, numberOfNodes);
    }
    public void addNode(NID node){
        int numberOfNodes = this.getNumberOfNodes()+1;
        if(numberOfNodes>BasicPattern.numberOfNodes){
            throw new RuntimeException("Cannot add more than "+BasicPattern.numberOfNodes+" nodes");
        }
        try {
            this.setNumberOfNodes(numberOfNodes);
            this.setNode(numberOfNodes,node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FieldNumberOutOfBoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setNode(int nodeIndex, NID node){
        int offset = getOffset(nodeIndex);
        try {
            super.setIntFld(offset,node.getPageNo().pid);
            super.setIntFld(offset+1,node.getSlotNo());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FieldNumberOutOfBoundException e) {
            throw new RuntimeException(e);
        }
    }

    

    @Deprecated
    public byte[] returnBasicPatternByteArray() {
        return super.returnTupleByteArray();
    }
    public void basicPatternSet(byte [] record, int offset, int length) throws InvalidTupleSizeException, FieldNumberOutOfBoundException, IOException {
        this.tupleSet(record,offset,length);
    }
    public void setHdr(short numFlds,  AttrType types[], short strSizes[]) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        hdrSet = true;
        super.setHdr(numFlds,types,strSizes);
    }
    public void setHdr() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this.setHdr(numberOfFields,headerTypes,strSizes);
    }
    
    public void clear(){
        try {
            this.setNumberOfNodes(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FieldNumberOutOfBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        //make sure it variables are not null
        List<NID> ids = new ArrayList<>();
        for(int i=0;i<this.getNumberOfNodes();i++){
            ids.add(this.getNode(i));
        }
        return ids.toString();
    }
}