package iterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.AttrOperator;
import global.AttrType;
import heap.*;
import iterator.bp.BPFileScan;
import iterator.bp.BPIterator;
import iterator.bp.BPNestedLoopJoin;
import iterator.interfaces.IteratorI;
import quadrupleheap.Quadruple;

import java.io.IOException;

public class BPTripleJoinDriver {
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
    private AttrType in1[], in2[];

    public BPTripleJoinDriver(int memoryAmount, int numLeftNodes, BPIterator leftItr,
                              int bpJoinNodePosition, int joinOnSubjectOrObject, String
                                      rightSubjectFilter, String rightPredicateFilter, String
                                      rightObjectFilter, double rightConfidenceFilter, int[] leftOutNodePositions,
                              int outputRightSubject, int outputRightObject) {
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
    }

    public void executeFirstLevelJoin() throws HFDiskMgrException, HFException, HFBufMgrException, IOException, InvalidTupleSizeException, NestedLoopException, InvalidRelation, FileScanException, TupleUtilsException {
        CondExpr[] rightFilter = getRightFilterForFirstLevelJoin(rightSubjectFilter, rightPredicateFilter, rightObjectFilter, rightConfidenceFilter);
        CondExpr[] outputFilter = getOutputFilterForFirstLevelJoin(bpJoinNodePosition, joinOnSubjectOrObject);
        FldSpec[] projectionList = getProjectionListForFirstLevelJoin(leftOutNodePositions, outputRightSubject, outputRightObject);
        AttrType[] basicPatternAttrTypes = BasicPattern.headerTypes;
        int basicPatternAttrTypesLen = BasicPattern.numberOfFields;

        FldSpec[] basicPatternProjectionList = BasicPattern.getProjectListForAllColumns();

        IteratorI<BasicPattern> bpFileScan = new BPFileScan("basicPatternHeapFile",
                                                            BasicPattern.headerTypes, BasicPattern.strSizes, (short) 0, basicPatternProjectionList.length,
                                                            basicPatternProjectionList, null);

        IteratorI<BasicPattern> bpNestedLoopJoin = new BPNestedLoopJoin(basicPatternAttrTypes, basicPatternAttrTypesLen,
                BasicPattern.strSizes, Quadruple.headerTypes, Quadruple.numberOfFields, Quadruple.strSizes, memoryAmount,
                bpFileScan, RDFDB.quadrupleHeapFileName, outputFilter, rightFilter, projectionList, projectionList.length);
        iterator.bp.BPIterator bpIterator = (iterator.bp.BPIterator) bpFileScan;
    }

    public void executeSecondLevelJoin() {
        CondExpr[] rightFilter = getRightFilterForTopLevelJoin(rightSubjectFilter, rightObjectFilter, 5);
        CondExpr[] outputFilter = getOutputFilterForFirstLevelJoin(bpJoinNodePosition, joinOnSubjectOrObject);

    }

    private FldSpec[] getProjectionListForFirstLevelJoin(int[] leftOutNodePositions, int outputRightSubject, int outputRightObject) {
        int projectionListLen = leftOutNodePositions.length*2;
        if (outputRightSubject == 1) {
            projectionListLen += 1;
        }
        if (outputRightObject == 1) {
            projectionListLen += 1;
        }
        FldSpec[] projectionList = new FldSpec[projectionListLen];
        in1 = new AttrType[projectionListLen];
        in2 = new AttrType[projectionListLen];
        int i;
        for (i = 0; i < leftOutNodePositions.length; i++) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(leftOutNodePositions[i]);
            projectionList[2*i] = new FldSpec(new RelSpec(RelSpec.outer), pageNumAndSlot[0]);
            projectionList[2*i+1] = new FldSpec(new RelSpec(RelSpec.outer), pageNumAndSlot[1]);
            in1[2*i] = new AttrType(AttrType.attrInteger);
            in1[2*i+1] = new AttrType(AttrType.attrInteger);
            in2[2*i] = new AttrType(AttrType.attrInteger);
            in2[2*i+1] = new AttrType(AttrType.attrInteger);
        }
        if (outputRightSubject == 1) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(Quadruple.SUBJECT_NODE_INDEX);
            projectionList[2*i] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[0]);
            projectionList[2*i+1] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[1]);            in1[i] = new AttrType(AttrType.attrInteger);
            in1[2*i] = new AttrType(AttrType.attrInteger);
            in1[2*i+1] = new AttrType(AttrType.attrInteger);
            in2[2*i] = new AttrType(AttrType.attrInteger);
            in2[2*i+1] = new AttrType(AttrType.attrInteger);
            i++;
        }
        if (outputRightObject == 1) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(Quadruple.OBJECT_NODE_INDEX);
            projectionList[2*i] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[0]);
            projectionList[2*i+1] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[1]);            in1[i] = new AttrType(AttrType.attrInteger);
            in1[2*i] = new AttrType(AttrType.attrInteger);
            in1[2*i+1] = new AttrType(AttrType.attrInteger);
            in2[2*i] = new AttrType(AttrType.attrInteger);
            in2[2*i+1] = new AttrType(AttrType.attrInteger);
            i++;
        }
        return projectionList;
    }

    private CondExpr[] getOutputFilterForFirstLevelJoin(int bpJoinNodePosition, int joinOnSubjectOrObject) {
        int rightOffset = joinOnSubjectOrObject == 0 ? 1 : 2;
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

    private CondExpr[] getRightFilterForFirstLevelJoin(String rightSubjectFilter, String rightPredicateFilter, String rightObjectFilter, double rightConfidenceFilter) {
        CondExpr[] expr = new CondExpr[5];
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].next = null;
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 3);
        expr[0].operand2.string = rightSubjectFilter;

        expr[1].op = new AttrOperator(AttrOperator.aopEQ);
        expr[1].next = null;
        expr[1].type1 = new AttrType(AttrType.attrSymbol);
        expr[1].type2 = new AttrType(AttrType.attrString);
        expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 4);
        expr[1].operand2.string = rightPredicateFilter;

        expr[2].op = new AttrOperator(AttrOperator.aopEQ);
        expr[2].next = null;
        expr[2].type1 = new AttrType(AttrType.attrSymbol);
        expr[2].type2 = new AttrType(AttrType.attrString);
        expr[2].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 5);
        expr[2].operand2.string = rightObjectFilter;

        expr[3].op = new AttrOperator(AttrOperator.aopEQ);
        expr[3].next = null;
        expr[3].type1 = new AttrType(AttrType.attrSymbol);
        expr[3].type2 = new AttrType(AttrType.attrReal);
        expr[3].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), 2);
        expr[3].operand2.real = (float) rightConfidenceFilter;

        expr[4] = null;

        return expr;
    }

    private CondExpr[] getRightFilterForTopLevelJoin(String rightSubjectFilter, String rightObjectFilter, int firstLevelNumNodes) {
        CondExpr[] expr = new CondExpr[3];
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].next = null;
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), firstLevelNumNodes + 1);
        expr[0].operand2.string = rightSubjectFilter;

        expr[1].op = new AttrOperator(AttrOperator.aopEQ);
        expr[1].next = null;
        expr[1].type1 = new AttrType(AttrType.attrSymbol);
        expr[1].type2 = new AttrType(AttrType.attrString);
        expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), firstLevelNumNodes + 2);
        expr[1].operand2.string = rightObjectFilter;

        expr[2] = null;

        return expr;
    }

}
