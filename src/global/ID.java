package global;

import java.io.IOException;
import java.io.Serializable;

public interface ID<T extends ID> extends Serializable {
    
    public int getSlotNo();
    public PageId getPageNo();
    public void setSlotNo(int slotNo);
    public void setPageNo(PageId pageId);
    public default void copy(T id){
        this.setPageNo(id.getPageNo());
        this.setSlotNo(id.getSlotNo());
    }
    public void writeToByteArray(byte[] array, int offset) throws IOException;
    
}
