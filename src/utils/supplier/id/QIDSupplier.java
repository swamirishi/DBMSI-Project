package utils.supplier.id;

import global.PageId;
import global.QID;

public class QIDSupplier implements IDSupplier<QID>{
    @Override
    public QID getID() {
        return new QID();
    }
    
    @Override
    public QID getID(PageId pageno, int slotno) {
        return new QID(pageno,slotno);
    }
    
    private QIDSupplier() {
    }
    
    private static QIDSupplier supplier;
    public static QIDSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDSupplier.class){
                if(supplier == null){
                    supplier = new QIDSupplier();
                }
            }
        }
        return supplier;
    }
}
