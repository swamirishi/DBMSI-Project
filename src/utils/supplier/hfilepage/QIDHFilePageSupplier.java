package utils.supplier.hfilepage;

import global.QID;
import global.RID;
import heap.HFPage;
import heap.Tuple;
import heap.interfaces.HFilePage;
import quadrupleheap.Quadruple;
import quadrupleheap.THFPage;

public class QIDHFilePageSupplier implements HFilePageSupplier<QID, Quadruple> {
    @Override
    public THFPage getHFilePage() {
        return new THFPage();
    }
    
    @Override
    public THFPage getHFilePage(HFilePage<QID, Quadruple> heapFilePage) {
        return new THFPage(heapFilePage);
    }
    
    private QIDHFilePageSupplier() {
    }
    
    private static QIDHFilePageSupplier supplier;
    public static QIDHFilePageSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDHFilePageSupplier.class){
                if(supplier == null){
                    supplier = new QIDHFilePageSupplier();
                }
            }
        }
        return supplier;
    }
}
