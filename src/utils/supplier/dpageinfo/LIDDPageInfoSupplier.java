package utils.supplier.dpageinfo;

import heap.DataPageInfo;
import heap.InvalidTupleSizeException;
import heap.Tuple;
import labelheap.Label;
import labelheap.LabelDataPageInfo;

import java.io.IOException;

public class LIDDPageInfoSupplier implements DPageInfoSupplier<Label>{
    @Override
    public LabelDataPageInfo getDataPageInfo(Label tuple) throws InvalidTupleSizeException, IOException {
        return new LabelDataPageInfo(tuple);
    }
    
    @Override
    public LabelDataPageInfo getDataPageInfo() {
        return new LabelDataPageInfo();
    }
    
    private LIDDPageInfoSupplier() {
    }
    
    private static LIDDPageInfoSupplier supplier;
    public static LIDDPageInfoSupplier getSupplier(){
        if(supplier == null){
            synchronized (LIDDPageInfoSupplier.class){
                if(supplier == null){
                    supplier = new LIDDPageInfoSupplier();
                }
            }
        }
        return supplier;
    }
}
