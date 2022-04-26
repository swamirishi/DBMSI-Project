package iterator;

import basicpatternheap.BasicPattern;
import diskmgr.RDFDB;
import global.*;
import heap.Tuple;
import heap.interfaces.HFile;
import quadrupleheap.Quadruple;

public class BasicPatternPredEval{
    public static HFile h1;
    public static HFile h2;

    public static boolean Eval(CondExpr p[], BasicPattern t1, Quadruple t2, AttrType in1[],
                               AttrType in2[]) throws Exception {
        int field1 = p[0].operand1.symbol.offset;
        int field2 = p[0].operand2.symbol.offset;

        NID nid1 = t1.getNode(field1);
        NID nid2 = field2 == Quadruple.SUBJECT_NODE_INDEX ? t2.getSubject().returnNid() : t2.getSubject().returnNid();

        return nid1.equals(nid2);
    }
}
