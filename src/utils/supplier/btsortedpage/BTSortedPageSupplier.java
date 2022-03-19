package utils.supplier.btsortedpage;

import btree.BTSortedPage;
import btree.ConstructPageException;
import btree.interfaces.BTSortedPageI;
import diskmgr.Page;
import global.ID;
import global.PageId;
import heap.Tuple;

public interface BTSortedPageSupplier<I extends ID, T extends Tuple> {
    public BTSortedPageI<I,T> getBTSortedPage(PageId pageno, int keyType) throws ConstructPageException;
    public BTSortedPageI<I,T> getBTSortedPage(Page page, int keyType);
    
}
