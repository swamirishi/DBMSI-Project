package utils.supplier.iterator.pnodesplaypq;

import basicpatternheap.BasicPattern;
import global.AttrType;
import global.TupleOrder;
import iterator.bp.BPpnodeSplayPQ;
import iterator.interfaces.pnodeSplayPQI;

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
    
    
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order) {
        return new BPpnodeSplayPQ(fldNo, fldType, order,true);
    }
    
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ() {
        return new BPpnodeSplayPQ(true);
    }
    
    @Override
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ(int fldNo, AttrType fldType, TupleOrder order,boolean referenceBased) {
        return new BPpnodeSplayPQ(fldNo, fldType, order,referenceBased);
    }
    
    @Override
    public pnodeSplayPQI<BasicPattern> getPnodeSplayPQ(boolean referenceBased) {
        return new BPpnodeSplayPQ(referenceBased);
    }
}
