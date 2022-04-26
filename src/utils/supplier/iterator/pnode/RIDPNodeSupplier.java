package utils.supplier.iterator.pnode;

import heap.Tuple;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.pnode;
import iterator.pnodeSplayNode;
import utils.supplier.iterator.pnodesplaynode.PNodeSplayNodeSupplier;

public class RIDPNodeSupplier implements PNodeSupplier<Tuple> {
    
    
    private static RIDPNodeSupplier supplier;
    public static RIDPNodeSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDPNodeSupplier.class){
                if(supplier == null){
                    supplier = new RIDPNodeSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeI<Tuple> getPNode() {
        return new pnode();
    }
}
