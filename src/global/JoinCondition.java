package global;

public class JoinCondition {
    private int leftNodePosition;
    private boolean joinOnSubject;

    public JoinCondition(int leftNodePosition, boolean joinOnSubject){
        this.leftNodePosition = leftNodePosition;
        this.joinOnSubject = joinOnSubject;
    }

    public boolean isJoinOnSubject() {
        return joinOnSubject;
    }

    public int getLeftNodePosition() {
        return leftNodePosition;
    }
}
