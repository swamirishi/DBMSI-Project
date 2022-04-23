package iterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.AttrOperator;
import global.AttrType;
import global.LID;
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
    LID rightSubjectFilter;
    LID rightPredicateFilter;
    LID rightObjectFilter;
    double rightConfidenceFilter;
    int[] leftOutNodePositions;
    int outputRightSubject;
    int outputRightObject;
    private AttrType in1[], in2[];

    public BPTripleJoinDriver(int memoryAmount, int numLeftNodes,
                              int bpJoinNodePosition, int joinOnSubjectOrObject, LID
                                      rightSubjectFilter, LID rightPredicateFilter, LID
                                      rightObjectFilter, double rightConfidenceFilter, int[] leftOutNodePositions,
                              int outputRightSubject, int outputRightObject) {
        this.memoryAmount = memoryAmount;
        this.numLeftNodes = numLeftNodes;
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

    public IteratorI<BasicPattern> getJoinIterator(IteratorI<BasicPattern> basicPatternIterator) throws HFDiskMgrException, HFException, HFBufMgrException, IOException, InvalidTupleSizeException, NestedLoopException, InvalidRelation, FileScanException, TupleUtilsException {
        CondExpr[] rightFilter = getRightFilter();
        CondExpr[] outputFilter = getOutputFilter();
        FldSpec[] projectionList = getProjectionList();
        AttrType[] basicPatternAttrTypes = BasicPattern.headerTypes;
        int basicPatternAttrTypesLen = BasicPattern.numberOfFields;

        IteratorI<BasicPattern> bpNestedLoopJoin = new BPNestedLoopJoin(basicPatternAttrTypes, basicPatternAttrTypesLen,
                BasicPattern.strSizes, Quadruple.headerTypes, Quadruple.numberOfFields, Quadruple.strSizes, memoryAmount,
                basicPatternIterator, RDFDB.quadrupleHeapFileName, outputFilter, rightFilter, projectionList, projectionList.length);
        return bpNestedLoopJoin;
    }

    private FldSpec[] getProjectionList() {
        int projectionListLen = leftOutNodePositions.length * 2;
        if (outputRightSubject == 1) {
            projectionListLen += 2;
        }
        if (outputRightObject == 1) {
            projectionListLen += 2;
        }
        FldSpec[] projectionList = new FldSpec[projectionListLen];
        in1 = new AttrType[projectionListLen];
        in2 = new AttrType[projectionListLen];
        int i;
        for (i = 0; i < leftOutNodePositions.length; i++) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(leftOutNodePositions[i]);
            projectionList[2 * i] = new FldSpec(new RelSpec(RelSpec.outer), pageNumAndSlot[0]);
            projectionList[2 * i + 1] = new FldSpec(new RelSpec(RelSpec.outer), pageNumAndSlot[1]);
            in1[2 * i] = new AttrType(AttrType.attrInteger);
            in1[2 * i + 1] = new AttrType(AttrType.attrInteger);
            in2[2 * i] = new AttrType(AttrType.attrInteger);
            in2[2 * i + 1] = new AttrType(AttrType.attrInteger);
        }
        if (outputRightSubject == 1) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(Quadruple.SUBJECT_NODE_INDEX);
            projectionList[2 * i] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[0]);
            projectionList[2 * i + 1] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[1]);
            in1[i] = new AttrType(AttrType.attrInteger);
            in1[2 * i] = new AttrType(AttrType.attrInteger);
            in1[2 * i + 1] = new AttrType(AttrType.attrInteger);
            in2[2 * i] = new AttrType(AttrType.attrInteger);
            in2[2 * i + 1] = new AttrType(AttrType.attrInteger);
            i++;
        }
        if (outputRightObject == 1) {
            int[] pageNumAndSlot = BasicPattern.getPageNumberAndSlot(Quadruple.OBJECT_NODE_INDEX);
            projectionList[2 * i] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[0]);
            projectionList[2 * i + 1] = new FldSpec(new RelSpec(RelSpec.innerRel), pageNumAndSlot[1]);
            in1[i] = new AttrType(AttrType.attrInteger);
            in1[2 * i] = new AttrType(AttrType.attrInteger);
            in1[2 * i + 1] = new AttrType(AttrType.attrInteger);
            in2[2 * i] = new AttrType(AttrType.attrInteger);
            in2[2 * i + 1] = new AttrType(AttrType.attrInteger);
            i++;
        }
        return projectionList;
    }

    private CondExpr[] getOutputFilter() {
        int rightNodeIndex = joinOnSubjectOrObject == 0 ? Quadruple.SUBJECT_NODE_INDEX : Quadruple.OBJECT_NODE_INDEX;
        int[] leftOffsets = BasicPattern.getPageNumberAndSlot(bpJoinNodePosition);
        int[] rightOffsets = Quadruple.getPageNumberAndSlot(rightNodeIndex);

        CondExpr[] expr = new CondExpr[3];
        expr[0] = new CondExpr();
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].next = null;
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrSymbol);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), leftOffsets[0]);
        expr[0].operand2.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), rightOffsets[0]);

        expr[1] = new CondExpr();
        expr[1].op = new AttrOperator(AttrOperator.aopEQ);
        expr[1].next = null;
        expr[1].type1 = new AttrType(AttrType.attrSymbol);
        expr[1].type2 = new AttrType(AttrType.attrSymbol);
        expr[1].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), leftOffsets[1]);
        expr[1].operand2.symbol = new FldSpec(new RelSpec(RelSpec.innerRel), rightOffsets[1]);

        expr[2] = null;
        return expr;
    }

    private CondExpr[] getRightFilter() {
        CondExpr[] expr = new CondExpr[8];

        CondExpr[] subjectCondExpr = getCondExprs(rightSubjectFilter, Quadruple.SUBJECT_NODE_INDEX);
        expr[0] = subjectCondExpr[0];
        expr[1] = subjectCondExpr[1];

        CondExpr[] predicateCondExpr = getCondExprs(rightPredicateFilter, Quadruple.PREDICTE_NODE_INDEX);
        expr[2] = predicateCondExpr[0];
        expr[3] = predicateCondExpr[1];

        CondExpr[] objectCondExpr = getCondExprs(rightObjectFilter, Quadruple.OBJECT_NODE_INDEX);
        expr[4] = objectCondExpr[0];
        expr[5] = objectCondExpr[1];

        expr[6] = new CondExpr();
        expr[6].op = new AttrOperator(AttrOperator.aopEQ);
        expr[6].next = null;
        expr[6].type1 = new AttrType(AttrType.attrSymbol);
        expr[6].type2 = new AttrType(AttrType.attrReal);
        expr[6].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Quadruple.VALUE_FLD);
        expr[6].operand2.real = (float) rightConfidenceFilter;

        expr[7] = null;

        return expr;
    }

    public CondExpr[] getCondExprs(LID id, int nodeIndex) {
        int[] offsets = Quadruple.getPageNumberAndSlot(nodeIndex);
        CondExpr[] exprs = new CondExpr[2];

        CondExpr expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[0]);
        expr.operand2.integer = id.getPageNo().pid;
        exprs[0] = expr;

        expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[1]);
        expr.operand2.integer = id.getSlotNo();
        exprs[1] = expr;

        return exprs;
    }
}
