package iterator;

//import heap.BasicPatternClass;
import heap.Tuple;

public class BPTripleJoin{
    public BPTripleJoin(int memoryAmount, int numLeftNodes, BPIterator leftItr,
                        int bpJoinNodePosition, int JoinOnSubjectOrObject, String
                                rightSubjectFilter, String rightPredicateFilter, String
                                rightObjectFilter, double rightConfidenceFilter, int[] leftOutNodePositions,
                        int outputRightSubject, int outputRightObject) throws Exception {
//        super();
        Tuple leftRow = leftItr.get_next();
        while (leftRow != null) {
            leftRow = leftItr.get_next();
        }
    }
}
