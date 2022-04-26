package utils.supplier.iterator.pnodesplaynode;

import basicpatternheap.BasicPattern;
import heap.Tuple;
import iterator.bp.BPpnodeSplayNode;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.pnodeSplayNode;

public class BPIDPNodeSplayNodeSupplier implements PNodeSplayNodeSupplier<BasicPattern> {
    
    
    private static BPIDPNodeSplayNodeSupplier supplier;
    public static BPIDPNodeSplayNodeSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDPNodeSplayNodeSupplier.class){
                if(supplier == null){
                    supplier = new BPIDPNodeSplayNodeSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeSplayNodeI<BasicPattern> getPNodeSplayNode(pnodeI<BasicPattern> h) {
        return new BPpnodeSplayNode(h);
    }
}
