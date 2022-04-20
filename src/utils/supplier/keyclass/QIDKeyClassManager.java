package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyTooLongException;
import global.QID;
import global.RID;

public class QIDKeyClassManager extends IDKeyClassManager<QID> {

    private int pageNoMax;
    private int slotCntMax;

    private QIDKeyClassManager(int pageNoMax, int slotCntMax) {
        super(pageNoMax, slotCntMax);
    }
    
    
    
    @Override
    public IntegerKey getKeyClass(QID obj) throws KeyTooLongException {
        return super.getKeyClass(obj);
    }
    private static final int MAX_PAGE_NO = 80200;
    private static final int MAX_SLOT_NO = 26750;
    private static QIDKeyClassManager supplier;
    public static QIDKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (QIDKeyClassManager.class){
                if(supplier == null){
                    supplier = new QIDKeyClassManager(MAX_PAGE_NO, MAX_SLOT_NO);
                }
            }
        }
        return supplier;
    }
    
    
}
