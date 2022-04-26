package utils.supplier.keyclass;

import btree.KeyClass;
import btree.KeyNotMatchException;
import btree.KeyTooLongException;

public interface KeyClassManager<O> {
    public KeyClass getKeyClass(O obj) throws KeyTooLongException, KeyNotMatchException;
}
