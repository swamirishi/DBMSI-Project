
package iterator.interfaces;

import heap.Tuple;
import iterator.pnode;

/**
 * An element in the binary tree.
 * including pointers to the children, the parent in addition to the item.
 */
public abstract class pnodeSplayNodeI<T extends Tuple>
{
  /** a reference to the element in the node */
  public pnodeI<T> item;

  /** the left child pointer */
  public pnodeSplayNodeI<T> lt;

  /** the right child pointer */
  public pnodeSplayNodeI<T> rt;

  /** the parent pointer */
  public pnodeSplayNodeI<T> par;
  
  /**
   * class constructor, sets all pointers to <code>null</code>.
   * @param h the element in this node
   */
  public pnodeSplayNodeI(pnodeI<T> h)
  {
    item = h;
    lt = null;
    rt = null;
    par = null;
  }

  /**
   * class constructor, sets all pointers.
   * @param h the element in this node
   * @param l left child pointer
   * @param r right child pointer
   */
  public pnodeSplayNodeI(pnodeI<T> h, pnodeSplayNodeI<T> l, pnodeSplayNodeI<T> r)
  {
    item = h;
    lt = l;
    rt = r;
    par = null;
  }

  /** a static dummy node for use in some methods */
  
}

