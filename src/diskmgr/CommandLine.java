package diskmgr;

import bufmgr.BufMgr;
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
    public static String dbpath="databases/";


    public static void main(String[] args) throws Exception {

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
                runBatchInsert(Arrays.copyOfRange(input, 1, input.length));
            }
            if (input[0].equals(Utils.QUERY)) {
                System.out.println("Running Query ......................");
                runQuery(Arrays.copyOfRange(input, 1, input.length));

            } if ((input[0].equals(Utils.REPORT))) {
                System.out.println("Running Report ......................");
                runReport(Arrays.copyOfRange(input, 1, input.length));
            }
        }
    }

    private static void runBatchInsert(String[] input) throws Exception {
        String dataFileName = input[0];
        int indexOption = Integer.parseInt(input[1]); //TODO will have to figure this out.
        String rdfDBName = input[2];

        if(Utils.checkIfDBExists(rdfDBName)){
            SystemDefs.MINIBASE_RESTART_FLAG = true;
        }

        //it works for if exist
        SystemDefs systemDefs = new SystemDefs(dbpath + rdfDBName, 50000, 50000, "Clock");
        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.init(1);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFileName));
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
//            try {
//                TScan tScan = new TScan(rdfdb.getQuadrupleHeapFile());
//                Quadruple q = tScan.getNext(qid);
//                while (q != null) {
//                    System.out.println(q);
//                    q = tScan.getNext(qid);
//                }
//            } catch (InvalidTupleSizeException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
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
//        System.out.println(subjectLabel + " " + predicateLabel + " " + objectLabel + " " + confidence);
        EID subjectId = rdfdb.insertEntity(subjectLabel);
        PID predicateId = rdfdb.insertPredicate(predicateLabel);
        EID objectId = rdfdb.insertEntity(objectLabel);
        rdfdb.incrementSubject();
        rdfdb.incrementObject();
        Quadruple q = new Quadruple(subjectId, predicateId, objectId, confidence);
        rdfdb.insertQuadruple(q.getQuadrupleByteArray());
    }

    private static void runReport(String[] input) throws HFDiskMgrException, InvalidSlotNumberException, InvalidTupleSizeException, HFBufMgrException, IOException {
        rdfdb = SystemDefs.JavabaseDB;
        SystemDefs.MINIBASE_RESTART_FLAG = true;
        SystemDefs systemDefs = new SystemDefs(rdfdb.db_name(), 50000, 50000, "Clock");
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
        String rdfDBName = input[0];
        int indexOption = Integer.parseInt(input[1]);
        String order = input[2];
        String subjectFilter = input[3];
        String predicateFilter = input[4];
        String objectFilter = input[5];
        int numbuf = Integer.parseInt(input[7]);

        if(!Utils.checkIfDBExists(rdfDBName)){
//            //Initialize the database
//            SystemDefs systemDefs = new SystemDefs(dbpath + rdfDBName, 50000, 50000, "Clock");
//            rdfdb = SystemDefs.JavabaseDB;
//            rdfdb.init(indexOption);
            System.out.println("Database with name : " + rdfDBName + " does not exist!");
            return;
        }
        else{
            rdfdb = SystemDefs.JavabaseDB;
//            rdfdb.closeDB();
//            rdfdb.openDB(dbpath + rdfDBName);
        }


        String ConfidenceFilter = input[6];
        subjectFilter = applyToFilter(subjectFilter);
        predicateFilter = applyToFilter(predicateFilter);
        objectFilter = applyToFilter(objectFilter);
        ConfidenceFilter = applyToFilter(ConfidenceFilter);
        Double confidenceFilter = ConfidenceFilter == null ? null : Double.valueOf(ConfidenceFilter);

        Stream stream = rdfdb.openStream(Integer.parseInt(order), subjectFilter, predicateFilter, objectFilter, confidenceFilter);

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

    public static boolean checkIfDBExists(String dbName){
        String[] pathnames;
        File file = new File(CommandLine.dbpath);
        pathnames = file.list();

        if(pathnames == null)
            return false;

        for(String eachPath : pathnames){
            if(eachPath.equals(dbName))
                return true;
        }
        return false;
    }
}

