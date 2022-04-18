package utils.supplier.tuple;

import basicpatternheap.BasicPattern;

public class BPIDTupleSupplier implements TupleSupplier<BasicPattern> {
    @Override
    public BasicPattern getTuple() {
        return new BasicPattern();
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
