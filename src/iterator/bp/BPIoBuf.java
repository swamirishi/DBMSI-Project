
package iterator.bp;

import basicpatternheap.BasicPattern;
import global.BPID;
import iterator.interfaces.IoBufI;
import utils.supplier.iterator.spoofIBuf.BPIDSpoofIBufSupplier;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;

public class BPIoBuf extends IoBufI<BPID, BasicPattern> {
    @Override
    protected SpoofIBufSupplier<BPID, BasicPattern> getSpoofIBufSupplier() {
        return BPIDSpoofIBufSupplier.getSupplier();
    }
}








