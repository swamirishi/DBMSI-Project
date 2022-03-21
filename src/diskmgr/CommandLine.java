package diskmgr;

import btree.*;
import btree.label.LIDBTreeFile;
import global.*;
import heap.*;
import index.label.LIDIndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import quadrupleheap.TScan;
import utils.supplier.keyclass.KeyClassManager;

import java.io.*;
import java.util.*;

public class CommandLine {
    public static RDFDB rdfdb;
    public static int numbuf;

    public static void main(String[] args) throws Exception {
//        SystemDefs.MINIBASE_RESTART_FLAG = true;

//        SystemDefs sysdef2 = new SystemDefs("shaitan", 50000, 50000, "Clock");

//        batchinsert D:\DBMSI-Project\phase2_test_data.txt
//        batchinsert D:\DBMSI-Project\phase2_test_data.txt 1 bablu
//        query bablu 1 1 :Jorunn_Danielsen :knows :Eirik_Newth 0.5232176791516268 50000
//        report
        String[] inputStrings = {"batchinsert D:\\DBMSI-Project\\phase2_test_data.txt 1 bablu","query bablu 1 1 :Jorunn_Danielsen :knows :Eirik_Newth 0.5232176791516268 50000"};
//        while (!inputString.equals("exit")) {
        for(String inputString:inputStrings){
            System.out.println("\nNew command loop: ");
            System.out.println("Type exit to stop!");

//            Scanner sc = new Scanner(System.in);
//            inputString = sc.nextLine();
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
        //batchinsert DATAFILENAME INDEXOPTION RDFDBNAME,        input => 0-indexed

        String dbName = input[3];
        int index_option = Integer.parseInt(input[2]);
        String dbPath= dbName + "_" + index_option;

        SystemDefs sysdef1 = new SystemDefs("databases/dbPath", 50000, 50000, "Clock");
        rdfdb = new RDFDB(index_option);

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
            Exception {
        String subjectLabel = tokens[0];
        String predicateLabel = tokens[1];
        String objectLabel = tokens[2];
        float confidence = Float.parseFloat(tokens[3]);

        System.out.println(subjectLabel + " " + predicateLabel + " " + objectLabel + " " + confidence);

        EID subjectId = rdfdb.insertEntity(subjectLabel, true);
        PID predicateId = rdfdb.insertPredicate(predicateLabel);
        EID objectId = rdfdb.insertEntity(objectLabel, false);
        Quadruple q = new Quadruple(subjectId, predicateId, objectId, confidence);
        rdfdb.insertQuadruple(q.getQuadrupleByteArray());
    }
    private static void runReport(String[] input) throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        int recordCountQuadruple = rdfdb.getQuadrupleCnt();
        int recordCountEntity = rdfdb.getEntityCnt();
        int recordCountSubject = rdfdb.getSubjectCnt();
        int recordCountObject = rdfdb.getObjectCnt();
        int recordCountPredicate = rdfdb.getPredicateCnt();

        System.out.println("Record Count of Quadruples in the database(" + rdfdb.db_name()+") = " + recordCountQuadruple);
        System.out.println("Record Count of Total Entities in the database(" + rdfdb.db_name()+") = " + recordCountEntity);
        System.out.println("Record Count of Subject in the database(" + rdfdb.db_name()+") = " + recordCountSubject);
        System.out.println("Record Count of Objects in the database(" + rdfdb.db_name()+") = " + recordCountObject);
        System.out.println("Record Count of Predicate in the database(" + rdfdb.db_name()+") = " + recordCountPredicate);
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
        int ORDER = Integer.parseInt(input[2]);
        String SUBJECTFILTER = input[3];
        String PREDICATEFILTER = input[4];
        String OBJECTFILTER = input[5];
        String CONFIDENCEFILTER = input[6];
//        int NUMBUF = input[7] != null? Integer.parseInt(input[7]) : 0;

        SUBJECTFILTER = applyToFilter(SUBJECTFILTER);
        PREDICATEFILTER = applyToFilter(PREDICATEFILTER);
        OBJECTFILTER = applyToFilter(OBJECTFILTER);
        CONFIDENCEFILTER = applyToFilter(CONFIDENCEFILTER);

        Double confidenceFilter = CONFIDENCEFILTER == null ? null : Double.valueOf(CONFIDENCEFILTER);

        Stream stream = rdfdb.openStream(ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
        Quadruple currQuadruple = stream.getNext();

//        while (currQuadruple != null) {
//            System.out.println(currQuadruple.toString());
//            currQuadruple = stream.getNext();
//        }
    }
}

class Utils {
    static final String BATCH_INSERT = "batchinsert";
    static final String QUERY = "query";
    static final String REPORT = "report";
}

