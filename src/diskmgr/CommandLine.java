package diskmgr;

import global.EID;
import global.PID;
import global.QID;
import global.SystemDefs;
import heap.*;
import quadrupleheap.Quadruple;
import quadrupleheap.TScan;

import java.io.*;
import java.util.*;

public class CommandLine {
    public static RDFDB rdfdb;

    public static void main(String[] args) throws Exception {
        SystemDefs sysdef = new SystemDefs("file", 50000, 50000, "Clock");
        rdfdb = new RDFDB(0);

        Scanner sc = new Scanner(System.in);
        String str = "batchinsert /Users/dhruv/ASU/Sem2/DBMSI/Project2/phase2_test_data.txt";
        String[] input = str.split(" ");
        String operationType = input[0];
        if (operationType.equals(Utils.BATCH_INSERT)) {
            runBatchInsert(input);
        }
        if (input[0].equals(Utils.QUERY)) {
            runQuery(Arrays.copyOfRange(input, 1, input.length));
        } else {
            runReport(Arrays.copyOfRange(input, 1, input.length));
        }
    }

    private static void runBatchInsert(String[] input) throws Exception {
        String fileName = input[1];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                if(tokens.length==4) {
                    insertTestData(tokens);
                }
                line = reader.readLine();
//                break;
            }
            reader.close();
            QID qid = new QID();
            try {
                TScan tScan = new TScan(rdfdb.getQuadrupleHeapFile());
                Quadruple q = tScan.getNext(qid);
                while (q != null) {
                    System.out.println(q);
                    q = tScan.getNext(qid);
                }
            } catch (InvalidTupleSizeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(PCounter.rcounter);
        System.out.println(PCounter.wcounter);
        Stream stream = rdfdb.openStream(1, "abc",
                null,null, null);
        Quadruple itr = stream.getNext();
        while (itr != null) {
            System.out.println(itr);
            itr = stream.getNext();
        }
    }

    private static void insertTestData(String[] tokens) throws SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException, InvalidTypeException {
        String subjectLabel = tokens[0];
        String predicateLabel = tokens[1];
        String objectLabel = tokens[2];
        float confidence = Float.valueOf(tokens[3]);
        System.out.println(subjectLabel + " " + predicateLabel + " " + objectLabel + " " + confidence);
        EID subjectId = rdfdb.insertEntity(subjectLabel);
        PID predicateId = rdfdb.insertPredicate(predicateLabel);
        EID objectId = rdfdb.insertEntity(objectLabel);

        Quadruple q = new Quadruple(subjectId, predicateId, objectId, confidence);
        rdfdb.insertQuadruple(q.getQuadrupleByteArray());
    }

    private static void runReport(String[] input) {

    }

    private static void runQuery(String[] input) {

    }
}

class Utils {
    static final String BATCH_INSERT = "batchinsert";
    static final String QUERY = "query";
    static final String REPORT = "report";
}

