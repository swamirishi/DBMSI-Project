package iterator;

//import heap.BasicPatternClass;

import bufmgr.PageNotReadException;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import heap.interfaces.HFile;
import heap.interfaces.ScanI;
import index.IndexException;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import quadrupleheap.TScan;

import java.io.IOException;

public class BPTripleJoin extends NestedLoopsJoins {
    int memoryAmount;
    int numLeftNodes;
    BPIterator leftItr;
    int bpJoinNodePosition;
    int joinOnSubjectOrObject;
    String rightSubjectFilter;
    String rightPredicateFilter;
    String rightObjectFilter;
    double rightConfidenceFilter;
    int[] leftOutNodePositions;
    int outputRightSubject;
    int outputRightObject;
    HFile rightTable;
    CondExpr[] rightFilter;
    CondExpr[] outputFilter;
    private FldSpec[] projectionList;
    private AttrType in1[], in2[];
    boolean isOuterJoin;

    public void setRightTable(HFile heapFile) {
        this.rightTable = heapFile;
    }

    public BPTripleJoin(int memoryAmount, int numLeftNodes, BPIterator leftItr,
                        int bpJoinNodePosition, int joinOnSubjectOrObject, String
                                rightSubjectFilter, String rightPredicateFilter, String
                                rightObjectFilter, double rightConfidenceFilter, int[] leftOutNodePositions,
                        int outputRightSubject, int outputRightObject) throws Exception {
        super(null, 0, null, null, 0, null, 0, null, null, null, null, null, 0);
        this.memoryAmount = memoryAmount;
        this.numLeftNodes = numLeftNodes;
        this.leftItr = leftItr;
        this.bpJoinNodePosition = bpJoinNodePosition;
        this.joinOnSubjectOrObject = joinOnSubjectOrObject;
        this.rightSubjectFilter = rightSubjectFilter;
        this.rightPredicateFilter = rightPredicateFilter;
        this.rightObjectFilter = rightObjectFilter;
        this.rightConfidenceFilter = rightConfidenceFilter;
        this.leftOutNodePositions = leftOutNodePositions;
        this.outputRightSubject = outputRightSubject;
        this.outputRightObject = outputRightObject;
        this.rightFilter = getRightFilter(rightSubjectFilter, rightPredicateFilter, rightObjectFilter, rightConfidenceFilter);
        this.outputFilter = getOutputFilter(joinOnSubjectOrObject);
        this.projectionList = getProjectionList(leftOutNodePositions, outputRightSubject, outputRightObject);
    }

    private FldSpec[] getProjectionList(int[] leftOutNodePositions, int outputRightSubject, int outputRightObject) {
        int projectionListLen = leftOutNodePositions.length;
        if (outputRightSubject == 1) {
            projectionListLen += 1;
        }
        if (outputRightObject == 1) {
            projectionListLen += 1;
        }
        FldSpec[] projectionList = new FldSpec[projectionListLen];
        in1 = new AttrType[projectionListLen];
        in2 = new AttrType[projectionListLen];
        int i = 0;
        for (i = 0; i < leftOutNodePositions.length; i++) {
            projectionList[i] = new FldSpec(new RelSpec(RelSpec.outer), leftOutNodePositions[i]);
            in1[i] = new AttrType(AttrType.attrInteger);
            in2[i] = new AttrType(AttrType.attrInteger);
        }
        if (outputRightSubject == 1) {
            projectionList[i] = new FldSpec(new RelSpec(RelSpec.innerRel), 1);
            in1[i] = new AttrType(AttrType.attrInteger);
            in2[i] = new AttrType(AttrType.attrInteger);
            i++;
        }
        if (outputRightObject == 1) {
            projectionList[i] = new FldSpec(new RelSpec(RelSpec.innerRel), 3);
            in1[i] = new AttrType(AttrType.attrInteger);
            in2[i] = new AttrType(AttrType.attrInteger);
        }
        return projectionList;
    }

    private CondExpr[] getOutputFilter(int joinOnSubjectOrObject) {
        int rightOffset = joinOnSubjectOrObject == 0 ? 1 : 3;
        CondExpr[] expr = new CondExpr[2];
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].next = null;
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrSymbol);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), bpJoinNodePosition);
        expr[0].operand2.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), rightOffset);

        expr[1] = null;
        return expr;
    }

    private CondExpr[] getRightFilter(String rightSubjectFilter, String rightPredicateFilter, String rightObjectFilter, double rightConfidenceFilter) {
        int offset = 0;
        if (isOuterJoin) {
            offset = leftOutNodePositions.length;
        }
        CondExpr[] expr = new CondExpr[5];
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].next = null;
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), offset + 1);
        expr[0].operand2.string = rightSubjectFilter;

        expr[1].op = new AttrOperator(AttrOperator.aopEQ);
        expr[1].next = null;
        expr[1].type1 = new AttrType(AttrType.attrSymbol);
        expr[1].type2 = new AttrType(AttrType.attrString);
        expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), offset + 2);
        expr[1].operand2.string = rightPredicateFilter;

        expr[2].op = new AttrOperator(AttrOperator.aopEQ);
        expr[2].next = null;
        expr[2].type1 = new AttrType(AttrType.attrSymbol);
        expr[2].type2 = new AttrType(AttrType.attrString);
        expr[2].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), offset + 3);
        expr[2].operand2.string = rightObjectFilter;

        expr[3].op = new AttrOperator(AttrOperator.aopEQ);
        expr[3].next = null;
        expr[3].type1 = new AttrType(AttrType.attrSymbol);
        expr[3].type2 = new AttrType(AttrType.attrReal);
        expr[3].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), offset + 4);
        expr[3].operand2.real = (float) rightConfidenceFilter;

        expr[4] = null;

        return expr;
    }

    @Override
    public Tuple get_next() throws Exception {
        Tuple outputTuple = new Tuple();
        Tuple leftRow = leftItr.get_next();
        while (leftRow != null) {
            ScanI scan = rightTable.openScan();
            RID qid = new RID();
            Tuple rightRow = scan.getNext(qid);
//            if (BasicPatternPredEval.Eval(rightFilter, rightRow, null, null, null)) {
//                if (BasicPatternPredEval.Eval(outputFilter, leftRow, rightRow, null, null)) {
//                    // Apply a projection on the outer and inner tuples.
//                    BasicPatternProjection.Join(leftRow, in1, rightRow, in2,
//                            outputTuple, projectionList, projectionList.length);
//                    return outputTuple;
//                }
//            }
            leftRow = leftItr.get_next();
        }
        return null;
    }
}
