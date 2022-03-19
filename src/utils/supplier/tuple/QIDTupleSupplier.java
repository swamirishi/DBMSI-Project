package utils.supplier.tuple;

import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDTupleSupplier implements TupleSupplier<Quadruple> {
    @Override
    public Quadruple getTuple() {
        return new Quadruple();
    }
    
    private QIDTupleSupplier() {
    }
    
    private static QIDTupleSupplier supplier;
    public static QIDTupleSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDTupleSupplier.class){
                if(supplier == null){
                    supplier = new QIDTupleSupplier();
                }
            }
        }
        return supplier;
    }
}
