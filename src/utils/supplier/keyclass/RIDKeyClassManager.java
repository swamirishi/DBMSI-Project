package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.ID;
import global.RID;

public class RIDKeyClassManager extends IDKeyClassManager<RID> {

    private int pageNoMax;
    private int slotCntMax;

    private RIDKeyClassManager(int pageNoMax, int slotCntMax) {
        super(pageNoMax, slotCntMax);
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(RID obj) throws KeyTooLongException {
        return super.getKeyClass(obj);
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static RIDKeyClassManager supplier;
    public static RIDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (RIDKeyClassManager.class){
                if(supplier == null){
                    supplier = new RIDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
