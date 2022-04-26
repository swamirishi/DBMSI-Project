package iterator.interfaces;

import global.ID;
import heap.Tuple;

/**
 * A structure describing a tuple.
 * include a run number and the tuple
 */
public class pnodeI<T extends Tuple> {
  /** which run does this tuple belong */
  public int     run_num;

  /** the tuple reference */
  public T   tuple;

  /**
   * class constructor, sets <code>run_num</code> to 0 and <code>tuple</code>
   * to null.
   */
  public pnodeI()
  {
    run_num = 0;  // this may need to be changed
    tuple = null;
  }
  
  /**
   * class constructor, sets <code>run_num</code> and <code>tuple</code>.
   * @param runNum the run number
   * @param t      the tuple
   */
  public pnodeI(int runNum, T t)
  {
    run_num = runNum;
    tuple = t;
  }
  
}

