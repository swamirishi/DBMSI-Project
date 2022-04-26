package Phase3;

import global.EID;
import global.LID;
//import global.NID;
import global.PID;
import heap.*;
import labelheap.Label;
import labelheap.LabelHeapFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class QueryUtils {
    public static LabelHeapFile queryEntityLabelHeapFile;

    static {
        try {
            queryEntityLabelHeapFile = new LabelHeapFile("queryEntityLabelHeapFile");
        } catch (HFException e) {
            e.printStackTrace();
        } catch (HFBufMgrException e) {
            e.printStackTrace();
        } catch (HFDiskMgrException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Heapfile queryBasicPatternHeapFile = new Heapfile("queryBasicPatternHeapFile");
//    public LabelHeapFile queryPredicateLabelHeapFile = new LabelHeapFile("queryPredicateLabelHeapFile");
//    public BasicPatternClass basicPatternClass = new BasicPatternClass();

    public QueryUtils() throws HFDiskMgrException, HFException, HFBufMgrException, IOException {
    }

    public void insertQueryDataInHeapFile(String queryFileName) {
        try {
            FileInputStream fis = new FileInputStream(queryFileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 4) {
                    insertQueryData(tokens);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException | InvalidTupleSizeException | InvalidTypeException | SpaceNotAvailableException | HFDiskMgrException | HFException | InvalidSlotNumberException | HFBufMgrException e) {
            e.printStackTrace();
        }
    }

    private void insertQueryData(String[] tokens) throws InvalidTupleSizeException, IOException, InvalidTypeException, SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, HFBufMgrException {
        String subjectLabel = tokens[0];
        String predicateLabel = tokens[1];
        String objectLabel = tokens[2];
        float confidence = Float.parseFloat(tokens[3]);

        System.out.println(subjectLabel + " " + predicateLabel + " " + objectLabel + " " + confidence);

        Label subject = new Label(subjectLabel);
        Label predicate = new Label(predicateLabel);
        Label object = new Label(objectLabel);

//        NID subjectId = queryEntityLabelHeapFile.insertRecord(subject.getLabelByteArray()).returnNid();
////        LID predicateId = queryPredicateLabelHeapFile.insertRecord(predicate.getLabelByteArray());
//        NID objectId = queryEntityLabelHeapFile.insertRecord(object.getLabelByteArray()).returnNid();
//        BasicPatternClass basicPatternClass = new BasicPatternClass(subjectId, objectId);
//        queryBasicPatternHeapFile.insertRecord(basicPatternClass.getBasicPatternByteArray());
    }

    public static LabelHeapFile getQueryEntityLabelHeapFile(){
        return queryEntityLabelHeapFile;
    }
}
