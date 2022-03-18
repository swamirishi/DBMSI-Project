package btree.interfaces;

import global.ID;
import global.PageId;

/**  IndexData: It extends the DataClass.
 *   It defines the data "rid" for leaf node in B++ tree.
 */
public abstract class ILeafData<I extends ID> extends DataClass {
  private I myRid;
  protected abstract I getID(PageId pageNo, int slotNo);
  public String toString() {
     String s;
     s="[ "+ (new Integer(myRid.getPageNo().pid)).toString() +" "
              + (new Integer(myRid.getSlotNo())).toString() + " ]";
     return s;
  }

  /** Class constructor
   *  @param    rid  the data rid
   */
  public ILeafData(I rid) { myRid= getID(rid.getPageNo(), rid.getSlotNo());};

  /** get a copy of the rid
  *  @return the reference of the copy 
  */
  public I getData() {return getID(myRid.getPageNo(), myRid.getSlotNo());};

  /** set the rid
   */ 
  public void setData(I rid) { myRid= getID(rid.getPageNo(), rid.getSlotNo());};
  
}   
