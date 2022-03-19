package utils.supplier.btheaderpage;

import btree.ConstructPageException;
import btree.interfaces.BTreeHeaderPageI;
import diskmgr.Page;
import global.ID;
import global.PageId;
import heap.Tuple;

public interface BTreeHeaderPageSupplier<I extends ID, T extends Tuple> {
    public BTreeHeaderPageI<I,T> getBTreeHeaderPage(PageId pageId) throws ConstructPageException;
    public BTreeHeaderPageI<I,T> getBTreeHeaderPage(Page page);
    public BTreeHeaderPageI<I,T> getBTreeHeaderPage() throws ConstructPageException;
    
}
