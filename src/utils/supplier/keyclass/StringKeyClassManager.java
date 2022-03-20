package utils.supplier.keyclass;

import btree.IntegerKey;
import btree.KeyClass;
import btree.KeyTooLongException;
import btree.StringKey;

public class StringKeyClassManager implements KeyClassManager<String>{
    @Override
    public KeyClass getKeyClass(String obj) throws KeyTooLongException {
        return new StringKey(obj);
    }
    
    private static StringKeyClassManager supplier;
    public static StringKeyClassManager getSupplier(){
        if(supplier == null){
            synchronized (StringKeyClassManager.class){
                if(supplier == null){
                    supplier = new StringKeyClassManager();
                }
            }
        }
        return supplier;
    }
}
