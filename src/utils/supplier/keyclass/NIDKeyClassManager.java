package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.LID;
import global.NID;

public class NIDKeyClassManager extends IDKeyClassManager<NID> {

    private int pageNoMax;
    private int slotCntMax;

    private NIDKeyClassManager(int pageNoMax, int slotCntMax) {
        super(pageNoMax, slotCntMax);
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(NID obj) throws KeyTooLongException {
        return super.getKeyClass(obj);
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static NIDKeyClassManager supplier;
    public static NIDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (NIDKeyClassManager.class){
                if(supplier == null){
                    supplier = new NIDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
