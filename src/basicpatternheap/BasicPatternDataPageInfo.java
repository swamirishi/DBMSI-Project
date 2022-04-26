package basicpatternheap;

import heap.InvalidTupleSizeException;
import heap.interfaces.DPageInfo;

import java.io.IOException;

public class BasicPatternDataPageInfo extends DPageInfo<BasicPattern> {
    public BasicPatternDataPageInfo() {
    }
    
    public BasicPatternDataPageInfo(byte[] array) {
        super(array);
    }
    
    public BasicPatternDataPageInfo(BasicPattern _atuple) throws InvalidTupleSizeException, IOException {
        super(_atuple);
    }
    
    @Override
    public BasicPattern getTuple(byte[] data, int offset, int size) {
        return new BasicPattern(data,offset,size);
    }
}
