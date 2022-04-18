package utils.supplier.dpageinfo;

import basicpatternheap.BasicPattern;
import basicpatternheap.BasicPatternDataPageInfo;
import heap.InvalidTupleSizeException;

import java.io.IOException;

public class BPIDDPageInfoSupplier implements DPageInfoSupplier<BasicPattern>{
    @Override
    public BasicPatternDataPageInfo getDataPageInfo(BasicPattern tuple) throws InvalidTupleSizeException, IOException {
        return new BasicPatternDataPageInfo(tuple);
    }

    public BasicPatternDataPageInfo getDataPageInfo() {
        return new BasicPatternDataPageInfo();
    }

    private BPIDDPageInfoSupplier() {
    }

    private static BPIDDPageInfoSupplier supplier;
    public static BPIDDPageInfoSupplier getSupplier(){
        if(supplier == null){
            synchronized (BPIDDPageInfoSupplier.class){
                if(supplier == null){
                    supplier = new BPIDDPageInfoSupplier();
                }
            }
        }
        return supplier;
    }
}
