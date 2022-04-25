package iterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import index.indexOptions.IDIndexOptions;
import index.indexOptions.QIDIndexOptions;
import iterator.bp.BPFileScan;
import iterator.bp.BPIndexNestedLoopJoins;
import iterator.bp.BPIterator;
import iterator.bp.BPNestedLoopJoin;
import iterator.interfaces.IndexNestedLoopsJoinsI;
import iterator.interfaces.IteratorI;
import quadrupleheap.Quadruple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BPTripleJoinDriver {
    int memoryAmount;
    int numLeftNodes;
    int bpJoinNodePosition;
    int joinOnSubjectOrObject;
    LID rightSubjectFilter;
    LID rightPredicateFilter;
    LID rightObjectFilter;
    Double rightConfidenceFilter;
    int[] leftOutNodePositions;
    int outputRightSubject;
    int outputRightObject;

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

    public IteratorI<BasicPattern> getNLJoinIterator(IteratorI<BasicPattern> basicPatternIterator) throws HFDiskMgrException, HFException, HFBufMgrException, IOException, InvalidTupleSizeException, NestedLoopException, InvalidRelation, FileScanException, TupleUtilsException {
        CondExpr[] rightFilter = getRightFilter();
        CondExpr[] outputFilter = getOutputFilter();
        FldSpec[] projectionList = getProjectionList();
        AttrType[] basicPatternAttrTypes = BasicPattern.headerTypes;
        int basicPatternAttrTypesLen = BasicPattern.numberOfFields;

        IteratorI<BasicPattern> bpNestedLoopJoin = new BPNestedLoopJoin(basicPatternAttrTypes, basicPatternAttrTypesLen,
                BasicPattern.strSizes, Quadruple.headerTypes, Quadruple.numberOfFields, Quadruple.strSizes, memoryAmount,
                basicPatternIterator, RDFDB.quadrupleHeapFileName, outputFilter, rightFilter, projectionList, projectionList.length) {
        };
        return bpNestedLoopJoin;
    }

    public IteratorI<BasicPattern> getIndexNLJoinIterator(IteratorI<BasicPattern> basicPatternIterator) throws HFDiskMgrException, HFException, HFBufMgrException, IOException, InvalidTupleSizeException, NestedLoopException, InvalidRelation, FileScanException, TupleUtilsException {
        boolean joinOnSubject = joinOnSubjectOrObject == 0;
        JoinCondition joinCondition = new JoinCondition(bpJoinNodePosition, joinOnSubject);
        FldSpec[] projectionList = getProjectionList();
        AttrType[] basicPatternAttrTypes = BasicPattern.headerTypes;
        int basicPatternAttrTypesLen = BasicPattern.numberOfFields;

        IDIndexOptions indexOptions = new QIDIndexOptions();
        IteratorI<BasicPattern> bpNestedLoopJoin = new BPIndexNestedLoopJoins(basicPatternAttrTypes, basicPatternAttrTypesLen,
                BasicPattern.strSizes, Quadruple.headerTypes, Quadruple.numberOfFields, Quadruple.strSizes, memoryAmount,
                basicPatternIterator, RDFDB.quadrupleHeapFileName, joinCondition,
                rightSubjectFilter, rightPredicateFilter, rightObjectFilter, new Float(rightConfidenceFilter), indexOptions,
                projectionList, projectionList.length);
        return bpNestedLoopJoin;
    }

    private FldSpec[] getProjectionList() {
        List<FldSpec> projectionList = new ArrayList<FldSpec>();
        projectionList.add(BasicPattern.getValueProject(new RelSpec(RelSpec.outer)));
        for (int pos : leftOutNodePositions) {
            projectionList.addAll(BasicPattern.getProjectListForNode(pos, new RelSpec(RelSpec.outer)));
        }
        if (outputRightSubject == 1) {
            projectionList.addAll(BasicPattern.getProjectListForNode(Quadruple.SUBJECT_NODE_INDEX, new RelSpec(RelSpec.innerRel)));
        }
        if (outputRightObject == 1) {
            projectionList.addAll(BasicPattern.getProjectListForNode(Quadruple.OBJECT_NODE_INDEX, new RelSpec(RelSpec.innerRel)));
        }
        return projectionList.toArray(new FldSpec[projectionList.size()]);
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
        List<CondExpr> exprs = new ArrayList<>();
        if (rightSubjectFilter != null) {
            List<CondExpr> subjectCondExpr = getCondExprs(rightSubjectFilter, Quadruple.SUBJECT_NODE_INDEX);
            exprs.addAll(subjectCondExpr);
        }

        if (rightPredicateFilter != null) {
            List<CondExpr> predicateCondExpr = getCondExprs(rightPredicateFilter, Quadruple.PREDICTE_NODE_INDEX);
            exprs.addAll(predicateCondExpr);
        }

        if (rightObjectFilter != null) {
            List<CondExpr> objectCondExpr = getCondExprs(rightObjectFilter, Quadruple.OBJECT_NODE_INDEX);
            exprs.addAll(objectCondExpr);
        }

        if (rightConfidenceFilter != null) {
            CondExpr expr = new CondExpr();
            expr.op = new AttrOperator(AttrOperator.aopGE);
            expr.next = null;
            expr.type1 = new AttrType(AttrType.attrSymbol);
            expr.type2 = new AttrType(AttrType.attrReal);
            expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), Quadruple.VALUE_FLD);
            expr.operand2.real = rightConfidenceFilter.floatValue();
        }

        CondExpr[] condExprs = new CondExpr[exprs.size() + 1];
        for (int i = 0; i < exprs.size(); i++) {
            condExprs[i] = exprs.get(i);
        }
        condExprs[exprs.size()] = null;

        return condExprs;
    }

    public List<CondExpr> getCondExprs(LID id, int nodeIndex) {
        int[] offsets = Quadruple.getPageNumberAndSlot(nodeIndex);
        List<CondExpr> exprs = new ArrayList<>();

        CondExpr expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[0]);
        expr.operand2.integer = id.getPageNo().pid;
        exprs.add(expr);

        expr = new CondExpr();
        expr.op = new AttrOperator(AttrOperator.aopEQ);
        expr.next = null;
        expr.type1 = new AttrType(AttrType.attrSymbol);
        expr.type2 = new AttrType(AttrType.attrInteger);
        expr.operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), offsets[1]);
        expr.operand2.integer = id.getSlotNo();
        exprs.add(expr);

        return exprs;
    }
}
