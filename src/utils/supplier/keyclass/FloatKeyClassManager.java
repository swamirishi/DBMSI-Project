package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyClass;
import btree.KeyTooLongException;

public class FloatKeyClassManager implements KeyClassManager<Float>{
    private int multiplier;
    private FloatKeyClassManager(int multiplier) {
        this.multiplier = multiplier;
    }
    
    @Override
    public KeyClass getKeyClass(Float obj) throws KeyTooLongException {
        return IntegerKeyClassManager.getSupplier().getKeyClass((int)(obj.floatValue()*this.multiplier));
    }
    
    private static FloatKeyClassManager supplier;
    public static FloatKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (FloatKeyClassManager.class){
                if(supplier == null){
                    supplier = new FloatKeyClassManager(1000000);
                }
            }
        }
        return supplier;
    }
    
}
