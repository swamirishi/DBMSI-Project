package diskmgr;

import global.EID;
import global.PID;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;

import java.util.Comparator;

public class SortOnOrder implements Comparator<Quadruple>
{
    private final int orderType;

    public SortOnOrder(int orderType){
        this.orderType = orderType;
    }



    public int compare(Quadruple q1, Quadruple q2){
        LabelHeapFile entityLabelHeapFile = null;
        LabelHeapFile predicateLabelHeapFile = null;
        EID subjectID;
        EID objectID;
        PID predicateID;

        if(q1.equals(q2))
            return 0;

        try {
            entityLabelHeapFile = new LabelHeapFile("entityLabelHeapFile");
            predicateLabelHeapFile = new LabelHeapFile("predicateLabelHeapFile");
        } catch (Exception e) {
            e.printStackTrace();
        }
        subjectID = q1.getSubject();
        objectID = q1.getObject();
        predicateID = q1.getPredicate();

        String subjectLabelQ1 = null;
        try {
            subjectLabelQ1 = entityLabelHeapFile.getRecord(subjectID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String objectLabelQ1 = null;
        try {
            objectLabelQ1 = entityLabelHeapFile.getRecord(objectID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String predicateLabelQ1 = null;
        try {
            predicateLabelQ1 = predicateLabelHeapFile.getRecord(predicateID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        subjectID = q2.getSubject();
        objectID = q2.getObject();
        predicateID = q2.getPredicate();

        String subjectLabelQ2 = null;
        try {
            subjectLabelQ2 = entityLabelHeapFile.getRecord(subjectID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String objectLabelQ2 = null;
        try {
            objectLabelQ2 = entityLabelHeapFile.getRecord(objectID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String predicateLabelQ2 = null;
        try {
            predicateLabelQ2 = predicateLabelHeapFile.getRecord(predicateID.returnLid()).getLabel();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(orderType == 1){
            if(subjectLabelQ1.compareTo(subjectLabelQ2) < 0){
                return -1;
            }
            else if(subjectLabelQ1.compareTo(subjectLabelQ2) == 0) {
                if (objectLabelQ1.compareTo(objectLabelQ2) < 0) {
                    return -1;
                }
                else if(objectLabelQ1.compareTo(objectLabelQ2) == 0){
                    if(predicateLabelQ1.compareTo(predicateLabelQ2) < 0)
                        return -1;
                    else if(predicateLabelQ1.compareTo(predicateLabelQ2) == 0){
                        if(q1.getValue() > q2.getValue())
                            return -1;
                    }
                }
            }
            return 1;
        }


        else if(orderType == 2){
            if(objectLabelQ1.compareTo(objectLabelQ2) < 0){
                return -1;
            }
            else if(objectLabelQ1.compareTo(objectLabelQ2) == 0) {
                if (subjectLabelQ1.compareTo(subjectLabelQ2) < 0) {
                    return -1;
                }
                else if(subjectLabelQ1.compareTo(subjectLabelQ2) == 0){
                    if(predicateLabelQ1.compareTo(predicateLabelQ2) < 0)
                        return -1;
                    else if(q1.getValue() > q2.getValue())
                        return -1;
                }
            }
            return 1;
        }


        else if(orderType == 3){
            if(subjectLabelQ1.compareTo(subjectLabelQ2) < 0){
                return -1;
            }
            else if(subjectLabelQ1.compareTo(subjectLabelQ2) == 0){
                if(q1.getValue() > q2.getValue())
                    return -1;
            }
            return 1;
        }
        else if(orderType == 4){
            if(predicateLabelQ1.compareTo(predicateLabelQ2) < 0){
                return -1;
            }
            else if(predicateLabelQ1.compareTo(predicateLabelQ2) == 0){
                if(q1.getValue() > q2.getValue())
                    return -1;
            }
            return 1;
        }
        else if(orderType == 5){
            if(objectLabelQ1.compareTo(objectLabelQ2) < 0){
                return -1;
            }
            else if(objectLabelQ1.compareTo(objectLabelQ2) == 0){
                if(q1.getValue() > q2.getValue())
                    return -1;
            }
            return 1;
        }
        else if(orderType == 6){
            if(q1.getValue() > q2.getValue())
                return -1;
            return 1;
        }

        return 0;
    }
}
