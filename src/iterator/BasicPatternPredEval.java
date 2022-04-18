package iterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.*;
import heap.Tuple;
import heap.interfaces.HFile;

public class BasicPatternPredEval{
    public static HFile h1;
    public static HFile h2;

    public static boolean Eval(CondExpr p[], BasicPattern t1, BasicPattern t2, AttrType in1[],
                               AttrType in2[]) throws Exception {
        RDFDB rdfdb = SystemDefs.JavabaseDB;

        int field1 = p[0].operand1.symbol.offset;
        int field2 = p[0].operand2.symbol.offset;

        NID nid1 = t1.getNode(field1);
        NID nid2 = t2.getNode(field2);

        Tuple labelTuple1 = SystemDefs.JavabaseDB.getQueryEntityLabelHeapFile().getRecord(nid1.returnLid());
        Tuple labelTuple2 = SystemDefs.JavabaseDB.getEntityLabelHeapFile().getRecord(nid2.returnLid());

        CondExpr[] expr = new CondExpr[0];
        expr[0].next  = null;
        expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrSymbol);
        expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
        expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

        return PredEval.Eval(expr, labelTuple1, labelTuple2, in1, in2);
    }
}
