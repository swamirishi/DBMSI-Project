package utils.supplier.dpageinfo;

import heap.DataPageInfo;
import heap.InvalidTupleSizeException;
import heap.Tuple;
import heap.interfaces.DPageInfo;
import utils.supplier.leafdata.RIDLeafDataSupplier;

import java.io.IOException;

public class RIDDPageInfoSupplier implements DPageInfoSupplier<Tuple>{
    @Override
    public DataPageInfo getDataPageInfo(Tuple tuple) throws InvalidTupleSizeException, IOException {
        return new DataPageInfo(tuple);
    }
    
    @Override
    public DataPageInfo getDataPageInfo() {
        return new DataPageInfo();
    }
    
    private RIDDPageInfoSupplier() {
    }
    
    private static RIDDPageInfoSupplier supplier;
    public static RIDDPageInfoSupplier getSupplier(){
        if(supplier == null){
            synchronized (RIDDPageInfoSupplier.class){
                if(supplier == null){
                    supplier = new RIDDPageInfoSupplier();
                }
            }
        }
        return supplier;
    }
}
