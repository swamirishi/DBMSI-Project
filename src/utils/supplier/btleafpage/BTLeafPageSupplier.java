package utils.supplier.btleafpage;

import btree.BTLeafPage;
import btree.ConstructPageException;
import btree.interfaces.BTLeafPageI;
import diskmgr.Page;
import global.ID;
import global.PageId;
import heap.Tuple;

import java.io.IOException;

public interface BTLeafPageSupplier<I extends ID,T extends Tuple> {
    public BTLeafPageI<I,T> getBTLeafPage(Page page, int keyType) throws IOException, ConstructPageException;
    public BTLeafPageI<I,T> getBTLeafPage(PageId pageno, int keyType) throws IOException, ConstructPageException;
    public BTLeafPageI<I,T> getBTLeafPage(int keyType) throws IOException, ConstructPageException;
    
    
}
