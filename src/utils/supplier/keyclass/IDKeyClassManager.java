package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.ID;


public class IDKeyClassManager<I extends ID> implements KeyClassManager<I> {
    
    private int pageNoMax;
    private int slotCntMax;
    
    public IDKeyClassManager(int pageNoMax, int slotCntMax) {
        this.pageNoMax = pageNoMax;
        this.slotCntMax = slotCntMax;
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(I obj) throws KeyTooLongException {
        if(obj.getPageNo().pid>pageNoMax || obj.getSlotNo()>slotCntMax){
            throw new KeyTooLongException("Max Page Value Allowed: "+pageNoMax+
                                          " Slot Value Allowed: :"+slotCntMax+" Given: PagwNo:"+obj.getPageNo().pid+" SlotNo:"
                                          +obj.getSlotNo());
        }
        return new IntegerKey(obj.getPageNo().pid*this.slotCntMax+ obj.getSlotNo());
    }
    
    
}
