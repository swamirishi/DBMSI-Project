package utils.supplier.keydataentry;

import btree.KeyClass;
import btree.interfaces.DataClass;
import btree.label.LIDKeyDataEntry;
import btree.quadraple.QIDKeyDataEntry;
import global.LID;
import global.PageId;
import global.QID;

public class LIDKeyDataEntrySupplier implements KeyDataEntrySupplier<LID>{
    private LIDKeyDataEntrySupplier() {
    }
    
    @Override
    public LIDKeyDataEntry getKeyDataEntry(KeyClass key, DataClass data) {
        return new LIDKeyDataEntry(key,data);
    }
    
    @Override
    public LIDKeyDataEntry getKeyDataEntry(KeyClass key, PageId pageNo) {
        return new LIDKeyDataEntry(key,pageNo);
    }
    
    @Override
    public LIDKeyDataEntry getKeyDataEntry(KeyClass key, LID id) {
        return new LIDKeyDataEntry(key,id);
    }
    
    
    private static LIDKeyDataEntrySupplier supplier;
    public static LIDKeyDataEntrySupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDKeyDataEntrySupplier.class){
                if(supplier == null){
                    supplier = new LIDKeyDataEntrySupplier();
                }
            }
        }
        return supplier;
    }
    
}
