package utils.supplier.tuple;

import heap.InvalidTupleSizeException;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDTupleSupplier implements TupleSupplier<Quadruple> {
    @Override
    public Quadruple getTuple() {
        return new Quadruple();
    }
    
    @Override
    public Quadruple getTuple(byte[] atuple, int offset, int length) {
        return new Quadruple(atuple,offset,length);
    }
    
    @Override
    public Quadruple getTuple(int size) {
        return new Quadruple(size);
    }
    
    @Override
    public Quadruple getTuple(Quadruple tuple){
        return new Quadruple(tuple);
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
