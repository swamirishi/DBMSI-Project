package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.ID;

public class IDKeyClassManager implements KeyClassManager<ID> {
    
    private int pageNoMax;
    private int slotCntMax;
    
    private IDKeyClassManager(int pageNoMax, int slotCntMax) {
        this.pageNoMax = pageNoMax;
        this.slotCntMax = slotCntMax;
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(ID obj) throws KeyTooLongException {
        if(obj.getPageNo().pid>pageNoMax || obj.getSlotNo()>slotCntMax){
            throw new KeyTooLongException("Max Page Value Allowed: "+pageNoMax+
                                          " Slot Value Allowed: :"+slotCntMax+" Given: PagwNo:"+obj.getPageNo().pid+" SlotNo:"
                                          +obj.getSlotNo());
        }
        return new IntegerKey(obj.getPageNo().pid*this.slotCntMax+ obj.getSlotNo());
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static IDKeyClassManager supplier;
    public static IDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (IDKeyClassManager.class){
                if(supplier == null){
                    supplier = new IDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
