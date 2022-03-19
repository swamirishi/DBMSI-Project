package labelheap;

import heap.InvalidTupleSizeException;
import heap.interfaces.DPageInfo;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class LabelDataPageInfo extends DPageInfo<Label> {
    public LabelDataPageInfo() {
    }
    
    public LabelDataPageInfo(byte[] array) {
        super(array);
    }
    
    public LabelDataPageInfo(Label _atuple) throws InvalidTupleSizeException, IOException {
        super(_atuple);
    }
    
    @Override
    public Label getTuple(byte[] data, int offset, int size) {
        return new Label(data,offset,size);
    }
}
