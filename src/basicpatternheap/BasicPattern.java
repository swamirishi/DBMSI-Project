package basicpatternheap;

import global.AttrType;
import global.NID;
import global.PageId;
import heap.FieldNumberOutOfBoundException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;
import iterator.FldSpec;
import iterator.RelSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicPattern extends Tuple {
    public static final short numberOfNodes = 10;
    public static final short numberOfFields = 2 * numberOfNodes + 1;
    public static final AttrType intType = new AttrType(AttrType.attrInteger);
    public static final AttrType floType = new AttrType(AttrType.attrReal);
    public static final AttrType[] headerTypes = IntStream.range(0, numberOfFields).mapToObj(i -> i == 0 ? floType : intType).collect(
            Collectors.toList()).toArray(new AttrType[numberOfFields]);
    public static final short[] strSizes = null;
    public static final int VALUE_FLD = 1;
    protected boolean hdrSet = false;

    public BasicPattern() {
        super(max_size);
    }

    public BasicPattern(byte[] aBasicPattern, int offset, int length) {
        super(aBasicPattern, offset, length);
    }

    public BasicPattern(BasicPattern fromBasicPattern){
        super(fromBasicPattern);
        this.hdrSet = fromBasicPattern.hdrSet;
    }

    public byte[] getBasicPatternByteArray() {
        return super.getTupleByteArray();
    }

    public BasicPattern(int size) {
        super(size);
    }
    
    public int getTotalNumberOfNodes(){
        return this.getFldCnt()/2;
    }

    public void basicPatternCopy(BasicPattern fromBasicPattern) throws IOException, FieldNumberOutOfBoundException {
        super.tupleCopy(fromBasicPattern);
    }

    public void setValue(float value) {
        try {
            setHdrIfNotSet();
            super.setFloFld(VALUE_FLD, value);
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

    public float getValue() {
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

    public static int getOffset(int nodeIndex) {
        return 2 * nodeIndex;
    }

    public static int[] getPageNumberAndSlot(int nodeIndex) {
        return new int[]{getOffset(nodeIndex), getOffset(nodeIndex) + 1};
    }

    //TODO in getNode on nodeIndex=-1 I want to return float
    public NID getNode(int nodeIndex) {
        int offset = getOffset(nodeIndex);
        return getNodeWithOffset(offset);
    }
    
    public NID getNodeWithOffset(int offset){
        try {
            setHdrIfNotSet();
            return new NID(new PageId(super.getIntFld(offset)), super.getIntFld(offset + 1));
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
        if (!hdrSet) {
            this.setHdr();
        }
    }

    public void setNode(int nodeIndex, NID node) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        setHdrIfNotSet();
        int offset = getOffset(nodeIndex);
        try {
            super.setIntFld(offset, node.getPageNo().pid);
            super.setIntFld(offset + 1, node.getSlotNo());
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

    public void basicPatternSet(byte[] record, int offset, int length) throws InvalidTupleSizeException, FieldNumberOutOfBoundException, IOException {
        this.tupleSet(record, offset, length);
    }

    public void setHdr(short numFlds, AttrType types[], short strSizes[]) throws InvalidTupleSizeException, IOException, InvalidTypeException {
        hdrSet = true;
        super.setHdr(numFlds, types, strSizes);
    }
    public void setHdr(short numberOfNodes) throws IOException, InvalidTupleSizeException, InvalidTypeException {
        short numberOfFields = (short) (2 * numberOfNodes + 1);
        this.setHdr(numberOfFields,IntStream.range(0, numberOfFields).mapToObj(i -> i == 0 ? floType : intType).collect(
                Collectors.toList()).toArray(new AttrType[numberOfFields]),strSizes);
    }
    public void setHdr() throws InvalidTupleSizeException, IOException, InvalidTypeException {
        this.setHdr(numberOfFields, headerTypes, strSizes);
    }

    public String toString() {
        //make sure it variables are not null
        List<NID> ids = new ArrayList<>();
        for (int i = 1; i <= this.getTotalNumberOfNodes(); i++) {
            ids.add(this.getNode(i));
        }
        return ids.toString() + "\t" + this.getValue();
    }
    private static FldSpec getFldSpec(int idx, RelSpec relSpec){
        return new FldSpec(relSpec, idx);
    }
    public static FldSpec getValueProject(RelSpec relSpec){
        return getFldSpec(1, relSpec);
    }
    public static List<FldSpec> getProjectListForNode(int nodeIdx, RelSpec relSpec){
        List<FldSpec> nodeFields = new ArrayList<>();
        for(int i:getPageNumberAndSlot(nodeIdx)){
            nodeFields.add(getFldSpec(i, relSpec));
        }
        return nodeFields;
    }

    public void printBasicPatternValues(){
        System.out.println("Confidence:" + this.getValue());
    }
}