package vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Join {
    @SerializedName(Constants.JOIN_NODE_POS)
    private int joinNodePosition;
    @SerializedName(Constants.JOIN_ON_SUBJECT_OBJECT)
    private int joinOnSubjectOrObject;
    @SerializedName(Constants.JOIN_RIGHT_FILTERS)
    private List<String> rightFilters;
    @SerializedName(Constants.JOIN_LEFT_NODE_POSITIONS)
    private List<Integer> leftOutNodePositions;
    @SerializedName(Constants.JOIN_OUTPUT_RIGHT_SUBJECT)
    private int outputRightSubject;
    @SerializedName(Constants.JOIN_OUTPUT_RIGHT_OBJECT)
    private int outputRightObject;
    
    public int getJoinNodePosition() {
        return joinNodePosition;
    }
    
    public int getJoinOnSubjectOrObject() {
        return joinOnSubjectOrObject;
    }
    
    public List<String> getRightFilters() {
        return rightFilters;
    }
    
    public List<Integer> getLeftOutNodePositions() {
        return leftOutNodePositions;
    }
    
    public int getOutputRightSubject() {
        return outputRightSubject;
    }
    
    public int getOutputRightObject() {
        return outputRightObject;
    }
}
