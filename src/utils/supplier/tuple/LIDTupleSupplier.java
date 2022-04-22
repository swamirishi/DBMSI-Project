package utils.supplier.tuple;

import heap.InvalidTupleSizeException;
import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDTupleSupplier implements TupleSupplier<Label> {
    @Override
    public Label getTuple() {
        return new Label();
    }
    
    @Override
    public Label getTuple(byte[] atuple, int offset, int length) {
        return new Label(atuple,offset,length);
    }
    
    @Override
    public Label getTuple(int size) {
        return new Label(size);
    }
    
    @Override
    public Label getTuple(Label tuple){
        return new Label(tuple);
    }
    
    private LIDTupleSupplier() {
    }
    
    private static LIDTupleSupplier supplier;
    public static LIDTupleSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDTupleSupplier.class){
                if(supplier == null){
                    supplier = new LIDTupleSupplier();
                }
            }
        }
        return supplier;
    }
}
