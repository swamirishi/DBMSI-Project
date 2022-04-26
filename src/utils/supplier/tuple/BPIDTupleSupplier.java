package utils.supplier.tuple;

import basicpatternheap.BasicPattern;
import heap.InvalidTupleSizeException;

public class BPIDTupleSupplier implements TupleSupplier<BasicPattern> {
    @Override
    public BasicPattern getTuple() {
        return new BasicPattern();
    }
    
    @Override
    public BasicPattern getTuple(byte[] atuple, int offset, int length) {
        return new BasicPattern(atuple,offset,length);
    }
    
    @Override
    public BasicPattern getTuple(int size) {
        return new BasicPattern(size);
    }
    
    @Override
    public BasicPattern getTuple(BasicPattern tuple){
        return new BasicPattern(tuple);
    }
    
    private BPIDTupleSupplier() {
    }

    private static BPIDTupleSupplier supplier;
    public static BPIDTupleSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDTupleSupplier.class){
                if(supplier == null){
                    supplier = new BPIDTupleSupplier();
                }
            }
        }
        return supplier;
    }
}
