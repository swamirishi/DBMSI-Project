package utils.supplier.btindexpage;

import btree.ConstructPageException;
import btree.interfaces.BTIndexPageI;
import diskmgr.Page;
import global.ID;
import global.PageId;
import heap.Tuple;

import java.io.IOException;

public interface BTIndexPageSupplier<I extends ID, T extends Tuple> {
    
    public BTIndexPageI<I,T> getBTIndexPage(PageId pageno, int keyType) throws IOException, ConstructPageException;
    public BTIndexPageI<I,T> getBTIndexPage(Page page, int keyType) throws IOException, ConstructPageException;
    public BTIndexPageI<I,T> getBTIndexPage(int keyType) throws IOException, ConstructPageException;
    
}
