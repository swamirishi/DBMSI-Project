package iterator;

import basicpatternheap.BasicPattern;
import global.AttrType;
import heap.FieldNumberOutOfBoundException;
import heap.Tuple;

import java.io.IOException;

public class BasicPatternProjection {
    public static void Join(BasicPattern t1, AttrType type1[],
                            BasicPattern t2, AttrType type2[],
                            BasicPattern Jtuple, FldSpec perm_mat[],
                            int nOutFlds
    )
            throws UnknowAttrType,
            FieldNumberOutOfBoundException,
            IOException {
        {
            Projection.Join(t1, type1, t2, type2, Jtuple, perm_mat, nOutFlds);
            float leftConfidenceValue = t1.getValue();
            float rightConfidenceValue = t2.getValue();
            float minConfidenceValue = Math.min(leftConfidenceValue, rightConfidenceValue);
            Jtuple.setValue(minConfidenceValue);
        }
    }
}
