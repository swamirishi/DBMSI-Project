package utils.supplier.iterator.pnode;

import basicpatternheap.BasicPattern;
import heap.Tuple;
import iterator.bp.BPpnode;
import iterator.interfaces.pnodeI;
import iterator.pnode;

public class BPIDPNodeSupplier implements PNodeSupplier<BasicPattern> {
    
    
    private static BPIDPNodeSupplier supplier;
    public static BPIDPNodeSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDPNodeSupplier.class){
                if(supplier == null){
                    supplier = new BPIDPNodeSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeI<BasicPattern> getPNode() {
        return new BPpnode();
    }
}
