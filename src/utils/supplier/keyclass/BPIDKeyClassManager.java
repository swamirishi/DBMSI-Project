package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.BPID;
import global.LID;

public class BPIDKeyClassManager extends IDKeyClassManager<BPID> {

    private int pageNoMax;
    private int slotCntMax;

    private BPIDKeyClassManager(int pageNoMax, int slotCntMax) {
        super(pageNoMax, slotCntMax);
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(BPID obj) throws KeyTooLongException {
        return super.getKeyClass(obj);
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static BPIDKeyClassManager supplier;
    public static BPIDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (BPIDKeyClassManager.class){
                if(supplier == null){
                    supplier = new BPIDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
