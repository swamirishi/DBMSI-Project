package utils.supplier.iterator.pnodesplaypq;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.TupleOrder;
import heap.Tuple;
import iterator.bp.BPpnodeSplayPQ;
import iterator.interfaces.pnodeSplayPQI;
import iterator.pnodeSplayPQ;

public class BPIDPNodeSPlayPQSupplier implements pnodeSplayPQSupplier<BasicPattern> {
    
    
    private static BPIDPNodeSPlayPQSupplier supplier;
    public static BPIDPNodeSPlayPQSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDPNodeSPlayPQSupplier.class){
                if(supplier == null){
                    supplier = new BPIDPNodeSPlayPQSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order) {
        return new BPpnodeSplayPQ(fldNo, fldType, order);
    }
    
    @Override
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ() {
        return new BPpnodeSplayPQ();
    }
}
