package utils.supplier.iterator.iobuf;

import basicpatternheap.BasicPattern;
import global.BPID;
import global.RID;
import heap.Tuple;
import iterator.IoBuf;
import iterator.bp.BPIoBuf;
import iterator.interfaces.IoBufI;

public class BPIDIoBufSupplier implements IoBufSupplier<BPID, BasicPattern> {
    
    
    private static BPIDIoBufSupplier supplier;
    public static BPIDIoBufSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDIoBufSupplier.class){
                if(supplier == null){
                    supplier = new BPIDIoBufSupplier();
                }
            }
        }
        return supplier;
    }
    
    @Override
    public IoBufI<BPID, BasicPattern> getIoBuf() {
        return new BPIoBuf();
    }
}
