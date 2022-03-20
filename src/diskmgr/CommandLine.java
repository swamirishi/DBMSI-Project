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
    public static int numbuf;

    public static void main(String[] args) throws Exception {
//        SystemDefs.MINIBASE_RESTART_FLAG = true;
        SystemDefs sysdef1 = new SystemDefs("bablu", 50000, 50000, "Clock");
//        SystemDefs sysdef2 = new SystemDefs("bablu", 50000, 50000, "Clock");
        rdfdb = new RDFDB(0);
//        SystemDefs sysdef2 = new SystemDefs("shaitan", 50000, 50000, "Clock");

//        batchinsert D:\DBMSI-Project\phase2_test_data.txt
//        batchinsert /Users/dhruv/ASU/Sem2/DBMSI/Project2/phase2_test_data.txt 1 bablu
//        query bablu 1 1 :Jorunn_Danielsen :knows :Eirik_Newth 0.5232176791516268 50000
//        report
        String inputString = " ";
        while (!inputString.equals("exit")) {
            System.out.println("\nNew command loop: ");
            System.out.println("Type exit to stop!");

            Scanner sc = new Scanner(System.in);
            inputString = sc.nextLine();
            String[] input = inputString.split(" ");
            String operationType = input[0];

            if (operationType.equals(Utils.BATCH_INSERT)) {
                System.out.println("Running Batch Insert ....................");
                runBatchInsert(input);
            }
            if (input[0].equals(Utils.QUERY)) {
                System.out.println("Running Query ......................");
                runQuery(Arrays.copyOfRange(input, 1, input.length));

            } else if ((input[0].equals(Utils.REPORT))) {
                System.out.println("Running Report ......................");
                runReport(Arrays.copyOfRange(input, 1, input.length));
            }
        }
    }

    private static void runBatchInsert(String[] input) throws Exception {
        String fileName = input[1];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 4) {
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
                    q.setHdr();
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
        System.out.println("BATCH INSERTION process ENDs");
        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }


    private static void insertTestData(String[] tokens) throws
            SpaceNotAvailableException, HFDiskMgrException, HFException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException, FieldNumberOutOfBoundException, InvalidTypeException {
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

    private static String applyToFilter(String filter) {
        if (filter.equals("*")) {
            return null;
        }
        return filter;
    }

    private static void runQuery(String[] input) throws Exception {
        String RDFDBNAME = input[0];
        String INDEXOPTION = input[1];
        String ORDER = input[2];
        String SUBJECTFILTER = input[3];
        String PREDICATEFILTER = input[4];
        String OBJECTFILTER = input[5];
        String CONFIDENCEFILTER = input[6];
//        int NUMBUF = input[7] != null? Integer.parseInt(input[7]) : 0;

        SUBJECTFILTER = applyToFilter(SUBJECTFILTER);
        PREDICATEFILTER = applyToFilter(PREDICATEFILTER);
        OBJECTFILTER = applyToFilter(OBJECTFILTER);
        CONFIDENCEFILTER = applyToFilter(CONFIDENCEFILTER);

//        SystemDefs sysdef = new SystemDefs(RDFDBNAME, 50000, NUMBUF, "Clock");
        Double confidenceFilter = CONFIDENCEFILTER == null ? null : Double.valueOf(CONFIDENCEFILTER);
        Stream stream = rdfdb.openStream(Integer.parseInt(ORDER), SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);

        Quadruple currQuadruple = stream.getNext();

        while (currQuadruple != null) {
            System.out.println(currQuadruple.toString());
            currQuadruple = stream.getNext();
        }
    }
}

class Utils {
    static final String BATCH_INSERT = "batchinsert";
    static final String QUERY = "query";
    static final String REPORT = "report";
}

