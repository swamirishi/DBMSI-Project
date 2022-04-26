package utils.supplier.dpageinfo;

import heap.InvalidTupleSizeException;
import quadrupleheap.QuadrapleDataPageInfo;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class QIDDPageInfoSupplier implements DPageInfoSupplier<Quadruple>{
    @Override
    public QuadrapleDataPageInfo getDataPageInfo(Quadruple tuple) throws InvalidTupleSizeException, IOException {
        return new QuadrapleDataPageInfo(tuple);
    }
    
    public QuadrapleDataPageInfo getDataPageInfo() {
        return new QuadrapleDataPageInfo();
    }
    
    private QIDDPageInfoSupplier() {
    }
    
    private static QIDDPageInfoSupplier supplier;
    public static QIDDPageInfoSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDDPageInfoSupplier.class){
                if(supplier == null){
                    supplier = new QIDDPageInfoSupplier();
                }
            }
        }
        return supplier;
    }
}
