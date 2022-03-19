package utils.supplier.tuple;

import heap.Tuple;
import utils.supplier.scan.RIDScanSupplier;

public class RIDTupleSupplier implements TupleSupplier<Tuple> {
    @Override
    public Tuple getTuple() {
        return new Tuple();
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
