package utils.supplier.tuple;

import labelheap.Label;
import quadrupleheap.Quadruple;

public class LIDTupleSupplier implements TupleSupplier<Label> {
    @Override
    public Label getTuple() {
        return new Label();
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
