package utils.supplier.keydataentry;

import btree.KeyClass;
import btree.bp.BPIDKeyDataEntry;
import btree.interfaces.DataClass;
import btree.label.LIDKeyDataEntry;
import global.BPID;
import global.LID;
import global.PageId;

public class BPIDKeyDataEntrySupplier implements KeyDataEntrySupplier<BPID>{
    private BPIDKeyDataEntrySupplier() {
    }
    
    @Override
    public BPIDKeyDataEntry getKeyDataEntry(KeyClass key, DataClass data) {
        return new BPIDKeyDataEntry(key,data);
    }
    
    @Override
    public BPIDKeyDataEntry getKeyDataEntry(KeyClass key, PageId pageNo) {
        return new BPIDKeyDataEntry(key,pageNo);
    }
    
    @Override
    public BPIDKeyDataEntry getKeyDataEntry(KeyClass key, BPID id) {
        return new BPIDKeyDataEntry(key,id);
    }
    
    
    private static BPIDKeyDataEntrySupplier supplier;
    public static BPIDKeyDataEntrySupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDKeyDataEntrySupplier.class){
                if(supplier == null){
                    supplier = new BPIDKeyDataEntrySupplier();
                }
            }
        }
        return supplier;
    }
    
}
