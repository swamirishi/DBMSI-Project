package utils.supplier.iterator.pnodesplaynode;

import global.RID;
import heap.Tuple;
import iterator.IoBuf;
import iterator.interfaces.IoBufI;
import iterator.interfaces.pnodeI;
import iterator.interfaces.pnodeSplayNodeI;
import iterator.pnodeSplayNode;
import utils.supplier.iterator.iobuf.IoBufSupplier;

public class RIDPNodeSplayNodeSupplier implements PNodeSplayNodeSupplier<Tuple> {
    
    
    private static RIDPNodeSplayNodeSupplier supplier;
    public static RIDPNodeSplayNodeSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDPNodeSplayNodeSupplier.class){
                if(supplier == null){
                    supplier = new RIDPNodeSplayNodeSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeSplayNodeI<Tuple> getPNodeSplayNode(pnodeI<Tuple> h) {
        return new pnodeSplayNode(h);
    }
}
