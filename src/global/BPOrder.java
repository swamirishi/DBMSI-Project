package global;

public class BPOrder extends TupleOrder{
    /**
     * TupleOrder Constructor
     * <br>
     * A tuple ordering can be defined as
     * <ul>
     * <li>   TupleOrder tupleOrder = new TupleOrder(TupleOrder.Random);
     * </ul>
     * and subsequently used as
     * <ul>
     * <li>   if (tupleOrder.tupleOrder == TupleOrder.Random) ....
     * </ul>
     *
     * @param _tupleOrder The possible ordering of the tuples
     */
    public BPOrder(int _tupleOrder) {
        super(_tupleOrder);
    }
}
