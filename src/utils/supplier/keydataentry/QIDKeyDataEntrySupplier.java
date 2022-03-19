package utils.supplier.keydataentry;

import btree.KeyClass;
import btree.KeyDataEntry;
import btree.interfaces.DataClass;
import btree.quadraple.QIDKeyDataEntry;
import global.PageId;
import global.QID;

public class QIDKeyDataEntrySupplier implements KeyDataEntrySupplier<QID>{
    private QIDKeyDataEntrySupplier() {
    }
    
    @Override
    public QIDKeyDataEntry getKeyDataEntry(KeyClass key, DataClass data) {
        return new QIDKeyDataEntry(key,data);
    }
    
    @Override
    public QIDKeyDataEntry getKeyDataEntry(KeyClass key, PageId pageNo) {
        return new QIDKeyDataEntry(key,pageNo);
    }
    
    @Override
    public QIDKeyDataEntry getKeyDataEntry(KeyClass key, QID id) {
        return new QIDKeyDataEntry(key,id);
    }
    
    
    private static QIDKeyDataEntrySupplier supplier;
    public static QIDKeyDataEntrySupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDKeyDataEntrySupplier.class){
                if(supplier == null){
                    supplier = new QIDKeyDataEntrySupplier();
                }
            }
        }
        return supplier;
    }
    
}
