package utils.supplier.iterator.pnodesplaypq;

import global.AttrType;
import global.TupleOrder;
import heap.Tuple;
import iterator.interfaces.pnodeSplayPQI;
import iterator.pnodeSplayPQ;

public class RIDPNodeSPlayPQSupplier implements pnodeSplayPQSupplier<Tuple> {
    
    
    private static RIDPNodeSPlayPQSupplier supplier;
    public static RIDPNodeSPlayPQSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDPNodeSPlayPQSupplier.class){
                if(supplier == null){
                    supplier = new RIDPNodeSPlayPQSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public pnodeSplayPQI<Tuple> getPnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order) {
        return new pnodeSplayPQ(fldNo,fldType,order);
    }
    
    @Override
    public pnodeSplayPQI<Tuple> getPnodeSplayPQ() {
        return new pnodeSplayPQ();
    }
}
