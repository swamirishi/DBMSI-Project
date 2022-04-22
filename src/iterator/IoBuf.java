
package iterator;
import heap.*;
import global.*;
import iterator.interfaces.IoBufI;
import utils.supplier.iterator.spoofIBuf.RIDSpoofIBufSupplier;
import utils.supplier.iterator.spoofIBuf.SpoofIBufSupplier;

public class IoBuf extends IoBufI<RID,Tuple> {
    
    @Override
    protected SpoofIBufSupplier<RID, Tuple> getSpoofIBufSupplier() {
        return RIDSpoofIBufSupplier.getSupplier();
    }
}








