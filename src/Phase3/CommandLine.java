package Phase3;

import diskmgr.PCounter;
import diskmgr.RDFDB;
import diskmgr.Stream;
import global.EID;
import global.PID;
import global.SystemDefs;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import quadrupleheap.Quadruple;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommandLine {
    public static RDFDB rdfdb;
    public static int numbuf;

    public static void main(String[] args) throws Exception {

        String inputString = " ";
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
            long startTime = System.currentTimeMillis();
            runQuery(Arrays.copyOfRange(input, 1, input.length));
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Time Elapsed in Query " + elapsedTime);

        } else if ((input[0].equals(Utils.REPORT))) {
            System.out.println("Running Report ......................");
            runReport(Arrays.copyOfRange(input, 1, input.length));
        }
    }

    private static void runBatchInsert(String[] input) throws Exception {
        //batchinsert DATAFILENAME INDEXOPTION RDFDBNAME,        input => 0-indexed
        System.out.println("Insertion Started");
        long startTime = System.currentTimeMillis();
        String dbName = input[3];
        int index_option = Integer.parseInt(input[2]);
        String dbPath = dbName + "_" + index_option;

        SystemDefs sysdef1 = new SystemDefs(dbPath, 50000, 50000, "Clock");
        rdfdb = new RDFDB();
        rdfdb.setRDFDBProperties(index_option);
        rdfdb.name = dbPath;

        String fileName = input[1];
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null) {
                String[] tokens = line.split("\\s+");
                if (tokens.length == 4) {
                    insertTestData(tokens);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("BATCH INSERTION process ENDs");
        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Time Elapsed in Insertion " + elapsedTime);
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

        System.out.println("Record Count of Quadruples in the database(" + rdfdb.db_name() + ") = " + recordCountQuadruple);
        System.out.println("Record Count of Total Entities in the database(" + rdfdb.db_name() + ") = " + recordCountEntity);
        System.out.println("Record Count of Subject in the database(" + rdfdb.db_name() + ") = " + recordCountSubject);
        System.out.println("Record Count of Objects in the database(" + rdfdb.db_name() + ") = " + recordCountObject);
        System.out.println("Record Count of Predicate in the database(" + rdfdb.db_name() + ") = " + recordCountPredicate);
    }

    private static String applyToFilter(String filter) {
        if (filter.equals("*")) {
            return null;
        }
        return filter;
    }

    private static void runQuery(String[] input) throws Exception {
        QueryUtils queryUtils = new QueryUtils();
        String rdfDBName = input[0];
        String queryFileName = input[1];
        queryUtils.insertQueryDataInHeapFile(queryFileName);
    }
}

class Utils {
    static final String BATCH_INSERT = "batchinsert";
    static final String QUERY = "query";
    static final String REPORT = "report";
    
    public static String getFileAsString(String path) throws IOException {
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            List<String> content = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.add(line);
            }
            return content.stream().collect(Collectors.joining("\n"));
        } catch (FileNotFoundException e) {
            return "";
        }
    }
}

