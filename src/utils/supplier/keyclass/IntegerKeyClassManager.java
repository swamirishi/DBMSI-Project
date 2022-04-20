package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyClass;
import btree.KeyTooLongException;

public class IntegerKeyClassManager implements KeyClassManager<Integer>{
    @Override
    public KeyClass getKeyClass(Integer obj) throws KeyTooLongException {
        return new IntegerKey(obj);
    }
    
    private static IntegerKeyClassManager supplier;
    public static IntegerKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (IntegerKeyClassManager.class){
                if(supplier == null){
                    supplier = new IntegerKeyClassManager();
                }
            }
        }
        return supplier;
    }
}
