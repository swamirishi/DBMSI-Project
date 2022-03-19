package quadrupleheap;

import heap.InvalidTupleSizeException;
import heap.interfaces.DPageInfo;

import java.io.IOException;

public class QuadrapleDataPageInfo extends DPageInfo<Quadruple> {
    public QuadrapleDataPageInfo() {
    }
    
    public QuadrapleDataPageInfo(byte[] array) {
        super(array);
    }
    
    public QuadrapleDataPageInfo(Quadruple _atuple) throws InvalidTupleSizeException, IOException {
        super(_atuple);
    }
    
    @Override
    public Quadruple getTuple(byte[] data, int offset, int size) {
        return new Quadruple(data,offset,size);
    }
}
