package utils.supplier.keydataentry;

import btree.KeyClass;
import btree.KeyDataEntry;
import btree.interfaces.DataClass;
import global.PageId;
import global.RID;

public class RIDKeyDataEntrySupplier implements KeyDataEntrySupplier<RID>{
    private RIDKeyDataEntrySupplier() {
    }
    
    @Override
    public KeyDataEntry getKeyDataEntry(KeyClass key, DataClass data) {
        return new KeyDataEntry(key,data);
    }
    
    @Override
    public KeyDataEntry getKeyDataEntry(KeyClass key, PageId pageNo) {
        return new KeyDataEntry(key,pageNo);
    }
    
    @Override
    public KeyDataEntry getKeyDataEntry(KeyClass key, RID id) {
        return new KeyDataEntry(key,id);
    }
    
    
    private static RIDKeyDataEntrySupplier supplier;
    public static RIDKeyDataEntrySupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDKeyDataEntrySupplier.class){
                if(supplier == null){
                    supplier = new RIDKeyDataEntrySupplier();
                }
            }
        }
        return supplier;
    }
    
}
