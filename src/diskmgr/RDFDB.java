package diskmgr;

import global.EID;
import global.PID;

public class RDFDB extends DB {
    public RDFDB(int type) {

    }

    public int getQuadrupleCnt() {
        return 0;
    }

    public int getEntityCnt() {
        return 0;
    }

    public int getPredicateCnt() {
        return 0;
    }

    public int getSubjectCnt() {
        return 0;
    }

    public int getObjectCnt() {
        return 0;
    }

    //Need to return EID
    public EID insertEntity(String entityLabel) {
        return null;
    }

    public boolean deleteEntity(String entityLabel) {
        return false;
    }

    //Need to return PID
    public PID insertPredicate(String predicateLabel) {
        return null;
    }

    public boolean deletePredicate(String predicateLabel) {
        return false;
    }

    //Need to return QID. Change type void to QID
    public void insertQuadruple(byte[] quadruplePtr) {
        return;
    }

    public boolean deleteQuadruple(byte[] quadruplePtr) {
        return false;
    }

    public Stream openStream(int orderType, String subjectFilter, String predicateFilter,
                             String objectFilter, double confidenceFilter){
        return null;
    }
}
