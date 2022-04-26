package utils.supplier.tuple;

import heap.InvalidTupleSizeException;
import heap.Tuple;
import utils.supplier.scan.RIDScanSupplier;

public class RIDTupleSupplier implements TupleSupplier<Tuple> {
    @Override
    public Tuple getTuple() {
        return new Tuple();
    }
    
    @Override
    public Tuple getTuple(byte[] atuple, int offset, int length) {
        return new Tuple(atuple,offset,length);
    }
    
    @Override
    public Tuple getTuple(int size) {
        return new Tuple(size);
    }
    
    @Override
    public Tuple getTuple(Tuple tuple){
        return new Tuple(tuple);
    }
    
    private RIDTupleSupplier() {
    }
    
    private static RIDTupleSupplier supplier;
    public static RIDTupleSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDTupleSupplier.class){
                if(supplier == null){
                    supplier = new RIDTupleSupplier();
                }
            }
        }
        return supplier;
    }
}
