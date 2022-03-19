package utils.supplier.leafdata;

import btree.LeafData;
import btree.quadraple.QIDLeafData;
import global.QID;
import global.RID;

public class QIDLeafDataSupplier implements LeafDataSupplier<QID>{
    
    @Override
    public QIDLeafData getLeafData(QID id) {
        return new QIDLeafData(id);
    }
    
    private QIDLeafDataSupplier() {
    }
    
    private static QIDLeafDataSupplier supplier;
    public static QIDLeafDataSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDLeafDataSupplier.class){
                if(supplier == null){
                    supplier = new QIDLeafDataSupplier();
                }
            }
        }
        return supplier;
    }
}
