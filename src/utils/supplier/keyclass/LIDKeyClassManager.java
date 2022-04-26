package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.LID;
import global.QID;

public class LIDKeyClassManager extends IDKeyClassManager<LID> {

    private int pageNoMax;
    private int slotCntMax;

    private LIDKeyClassManager(int pageNoMax, int slotCntMax) {
        super(pageNoMax, slotCntMax);
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(LID obj) throws KeyTooLongException {
        return super.getKeyClass(obj);
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static LIDKeyClassManager supplier;
    public static LIDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (LIDKeyClassManager.class){
                if(supplier == null){
                    supplier = new LIDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
