package iterator.interfaces;

import global.AttrType;
import global.TupleOrder;
import heap.Tuple;
import iterator.TupleUtilsException;
import iterator.UnknowAttrType;
import utils.supplier.iterator.pnodesplaynode.PNodeSplayNodeSupplier;

import java.io.IOException;

/**
 * Implements a sorted binary tree (extends class pnodePQ).
 * Implements the <code>enq</code> and the <code>deq</code> functions.
 */
public abstract class pnodeSplayPQI<T extends Tuple> extends pnodePQI<T> {
    protected abstract PNodeSplayNodeSupplier<T> getPNodeSplayNodeSupplier();
    
    protected abstract pnodeSplayNodeI<T> getDummyPNodeSPlayNode();
    
    /**
     * the root of the tree
     */
    protected pnodeSplayNodeI<T> root;
  /*
  pnodeSplayNode*   leftmost();
  pnodeSplayNode*   rightmost();
  pnodeSplayNode*   pred(pnodeSplayNode* t);
  pnodeSplayNode*   succ(pnodeSplayNode* t);
  void            _kill(pnodeSplayNode* t);
  pnodeSplayNode*   _copy(pnodeSplayNode* t);
  */
    
    /**
     * class constructor, sets default values.
     */
    public pnodeSplayPQI() {
        root = null;
        count = 0;
        fld_no = 0;
        fld_type = new AttrType(AttrType.attrInteger);
        sort_order = new TupleOrder(TupleOrder.Ascending);
    }
    
    /**
     * class constructor.
     *
     * @param fldNo   the field number for sorting
     * @param fldType the type of the field for sorting
     * @param order   the order of sorting (Ascending or Descending)
     */
    public pnodeSplayPQI(int fldNo, AttrType fldType, TupleOrder order) {
        root = null;
        count = 0;
        fld_no = fldNo;
        fld_type = fldType;
        sort_order = order;
    }
    
    /**
     * Inserts an element into the binary tree.
     *
     * @param item the element to be inserted
     * @throws IOException         from lower layers
     * @throws UnknowAttrType      <code>attrSymbol</code> or
     *                             <code>attrNull</code> encountered
     * @throws TupleUtilsException error in tuple compare routines
     */
    public void enq(pnodeI<T> item) throws IOException, UnknowAttrType, TupleUtilsException {
        count++;
        pnodeSplayNodeI<T> newnode = getPNodeSplayNodeSupplier().getPNodeSplayNode(item);
        pnodeSplayNodeI<T> t = root;
        
        if (t == null) {
            root = newnode;
            return;
        }
        
        int comp = pnodeCMP(item, t.item);
        
        pnodeSplayNodeI<T> l = getDummyPNodeSPlayNode();
        pnodeSplayNodeI<T> r = getDummyPNodeSPlayNode();
        
        boolean done = false;
        
        while (!done) {
            if ((sort_order.tupleOrder == TupleOrder.Ascending && comp >= 0) || (
                    sort_order.tupleOrder == TupleOrder.Descending && comp <= 0)) {
                pnodeSplayNodeI<T> tr = t.rt;
	            if (tr == null) {
		            tr = newnode;
		            comp = 0;
		            done = true;
	            } else {
		            comp = pnodeCMP(item, tr.item);
	            }
                
                if ((sort_order.tupleOrder == TupleOrder.Ascending && comp <= 0) || (
                        sort_order.tupleOrder == TupleOrder.Descending && comp >= 0)) {
                    l.rt = t;
                    t.par = l;
                    l = t;
                    t = tr;
                } else {
                    pnodeSplayNodeI<T> trr = tr.rt;
	                if (trr == null) {
		                trr = newnode;
		                comp = 0;
		                done = true;
	                } else {
		                comp = pnodeCMP(item, trr.item);
	                }
	
	                if ((t.rt = tr.lt) != null) {
		                t.rt.par = t;
	                }
                    tr.lt = t;
                    t.par = tr;
                    l.rt = tr;
                    tr.par = l;
                    l = tr;
                    t = trr;
                }
            } // end of if(comp >= 0)
            else {
                pnodeSplayNodeI<T> tl = t.lt;
	            if (tl == null) {
		            tl = newnode;
		            comp = 0;
		            done = true;
	            } else {
		            comp = pnodeCMP(item, tl.item);
	            }
                
                if ((sort_order.tupleOrder == TupleOrder.Ascending && comp >= 0) || (
                        sort_order.tupleOrder == TupleOrder.Descending && comp <= 0)) {
                    r.lt = t;
                    t.par = r;
                    r = t;
                    t = tl;
                } else {
                    pnodeSplayNodeI<T> tll = tl.lt;
	                if (tll == null) {
		                tll = newnode;
		                comp = 0;
		                done = true;
	                } else {
		                comp = pnodeCMP(item, tll.item);
	                }
	
	                if ((t.lt = tl.rt) != null) {
		                t.lt.par = t;
	                }
                    tl.rt = t;
                    t.par = tl;
                    r.lt = tl;
                    tl.par = r;
                    r = tl;
                    t = tll;
                }
            } // end of else
        } // end of while(!done)
	
	    if ((r.lt = t.rt) != null) {
		    r.lt.par = r;
	    }
	    if ((l.rt = t.lt) != null) {
		    l.rt.par = l;
	    }
	    if ((t.lt = getDummyPNodeSPlayNode().rt) != null) {
		    t.lt.par = t;
	    }
	    if ((t.rt = getDummyPNodeSPlayNode().lt) != null) {
		    t.rt.par = t;
	    }
        t.par = null;
        root = t;
        
        return;
    }
    
    /**
     * Removes the minimum (Ascending) or maximum (Descending) element.
     *
     * @return the element removed
     */
    public pnodeI<T> deq() {
	    if (root == null) {
		    return null;
	    }
        
        count--;
        pnodeSplayNodeI<T> t = root;
        pnodeSplayNodeI<T> l = root.lt;
        if (l == null) {
	        if ((root = t.rt) != null) {
		        root.par = null;
	        }
            return t.item;
        } else {
            while (true) {
                pnodeSplayNodeI<T> ll = l.lt;
                if (ll == null) {
	                if ((t.lt = l.rt) != null) {
		                t.lt.par = t;
	                }
                    return l.item;
                } else {
                    pnodeSplayNodeI<T> lll = ll.lt;
                    if (lll == null) {
	                    if ((l.lt = ll.rt) != null) {
		                    l.lt.par = l;
	                    }
                        return ll.item;
                    } else {
                        t.lt = ll;
                        ll.par = t;
	                    if ((l.lt = ll.rt) != null) {
		                    l.lt.par = l;
	                    }
                        ll.rt = l;
                        l.par = ll;
                        t = ll;
                        l = lll;
                    }
                }
            } // end of while(true)
        }
    }
  
  /*  
                  pnodeSplayPQ(pnodeSplayPQ& a);
  virtual       ~pnodeSplayPQ();

  Pix           enq(pnode  item);
  pnode           deq(); 

  pnode&          front();
  void          del_front();

  int           contains(pnode  item);

  void          clear(); 

  Pix           first(); 
  Pix           last(); 
  void          next(Pix& i);
  void          prev(Pix& i);
  pnode&          operator () (Pix i);
  void          del(Pix i);
  Pix           seek(pnode  item);

  int           OK();                    // rep invariant
  */
}
