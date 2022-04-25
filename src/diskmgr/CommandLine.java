package diskmgr;

import basicpatternheap.BasicPattern;
import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import global.EID;
import global.LID;
import global.PID;
import global.SystemDefs;
import heap.*;
import index.IndexException;
import index.IndexUtils;
import index.UnknownIndexTypeException;
import iterator.*;
import iterator.interfaces.IteratorI;
import javafx.util.Pair;
import quadrupleheap.Quadruple;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class CommandLine {
    public static RDFDB rdfdb;
    public static int numbuf;
    public static SystemDefs systemDefs;

    public static void main(String[] args) throws Exception {
//        SystemDefs.MINIBASE_RESTART_FLAG = true;

//        batchinsert /Users/swamirishi/Documents/asu/Spring_2022/DBMSI/DBMSI-Project/test.txt 3 swami_db
//        query swami_db 3 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        batchinsert /Users/dhruv/ASU/Sem2/DBMSI/Project2/test1.txt 6 popi
//        query bablu 1 1 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query bablu 1 1 :Bernhard_A_M_Seefeld :name :Bernhard_A_M_Seefeld * 5000
//        batchinsert D:\DBMSI-Project\phase3.txt 1 real_db
//        query test_db_200 1 1 :Jorunn_Danielsen * * * 10
//        query test_db 6 6 :Jorunn_Danielsen * * * 10000
//        batchinsert Users/dhruv/ASU/Sem2/DBMSI/Project2/test2.txt 1 popi
//        query real_db 1 2 * * * * 5000
//        query real_db 1 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 3 3 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 4 4 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 5 5 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
        //        query test_db 1 1 * * * * 5000

//        report
        System.out.println("By default, we are creating indexes on subject label, " +
                "object label and predicate label. This helps us in duplicate records selection.");
        System.out.println("Please choose Index Option from 1 to 5 for Batch Insert.");
        System.out.println("1. Index on Subject, Predicate, Object");
        System.out.println("2. Index on Predicate, Subject, Object");
        System.out.println("3. Index on Subject");
        System.out.println("4. Index on Predicate");
        System.out.println("5. Index on Object");
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");
        System.out.println("Please choose Index Option from 1 to 6 for Query.");
        System.out.println("1. Index on Subject, Predicate, Object");
        System.out.println("2. Index on Predicate, Subject, Object");
        System.out.println("3. Index on Subject");
        System.out.println("4. Index on Predicate");
        System.out.println("5. Index on Object");
        System.out.println("6. This doesn't use an Index. It uses the Quadruple Sort, an extension to Sort Tuples");

        String inputString = " ";
        while (!inputString.equals("exit")) {
            System.out.println("\nNew command loop: ");
            System.out.println("Type exit to stop!");
            Scanner sc = new Scanner(System.in);
            inputString = sc.nextLine();
//            inputString = "query swami_db 3 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000";
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
//            break;
        }
    }

    private static void runBatchInsert(String[] input) throws Exception {
        //batchinsert DATAFILENAME INDEXOPTION RDFDBNAME,        input => 0-indexed
        System.out.println("Insertion Started");
        long startTime = System.currentTimeMillis();
        String dbName = input[3];
        int index_option = Integer.parseInt(input[2]);
        String dbPath = dbName + "_" + index_option;

        File file = new File(dbPath);
        if (file.exists()) {
            //open existing database
            systemDefs = new SystemDefs(dbPath, 0, 10000, "Clock", index_option);
        } else {
            //create a new db
            systemDefs = new SystemDefs(dbPath, 5000, 5050, "Clock", index_option);
        }

        rdfdb = SystemDefs.JavabaseDB;
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

        SystemDefs.close();
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
        String RDFDBNAME = input[0];
        int INDEXOPTION = Integer.parseInt(input[1]);
        int ORDER = Integer.parseInt(input[2]);
        String SUBJECTFILTER = input[3];
        String PREDICATEFILTER = input[4];
        String OBJECTFILTER = input[5];
        String CONFIDENCEFILTER = input[6];
        numbuf = input[7] != null ? Integer.parseInt(input[7]) : 0;
        String dbPath = RDFDBNAME + "_" + INDEXOPTION;
        File file = new File(dbPath);

        if (file.exists()) {
            //open existing database
            systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
        } else {
            //create a new db
            systemDefs = new SystemDefs(dbPath, 5000, numbuf, "Clock", INDEXOPTION);
        }

        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.name = dbPath;

        System.out.println("Warning!: Number of Buffers changed to: " + SystemDefs.JavabaseBM.getNumBuffers());
        SUBJECTFILTER = applyToFilter(SUBJECTFILTER);
        PREDICATEFILTER = applyToFilter(PREDICATEFILTER);
        OBJECTFILTER = applyToFilter(OBJECTFILTER);
        CONFIDENCEFILTER = applyToFilter(CONFIDENCEFILTER);

        Double confidenceFilter = CONFIDENCEFILTER == null ? null : Double.valueOf(CONFIDENCEFILTER);

        System.out.println("Provide Comma Separated for First Level Join");
//        Scanner sc = new Scanner(System.in);
        String firstJoinQuery = "20,4,1,0,*,*,:Eirik_Newth,0.5232177,1,1,1";
//                sc.nextLine();
        String secondJoinQuery = "20,4,1,0,*,*,:Ms,0.5232177,1,1,1";
//                sc.nextLine();

        BPTripleJoinDriver bpTripleJoinDriver1 = getJoinDriver(firstJoinQuery);
        BPTripleJoinDriver bpTripleJoinDriver2 = getJoinDriver(secondJoinQuery);

//        openStreamAndExecuteNLJ(bpTripleJoinDriver1, bpTripleJoinDriver2, ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
//        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
//        SystemDefs.close();
//
//        systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
//        rdfdb = SystemDefs.JavabaseDB;
//        rdfdb.name = dbPath;
//        PCounter.initialize();
//        openStreamAndExecuteIndexNLJ(bpTripleJoinDriver1, bpTripleJoinDriver2, ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
//        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
//        SystemDefs.close();
////
//        systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
//        rdfdb = SystemDefs.JavabaseDB;
//        rdfdb.name = dbPath;
        openStreamAndExecuteSMJ(bpTripleJoinDriver1, bpTripleJoinDriver2, ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
        SystemDefs.close();
    }

    private static void openStreamAndExecuteSMJ(BPTripleJoinDriver bpTripleJoinDriver1, BPTripleJoinDriver bpTripleJoinDriver2, int order, String subjectfilter, String predicatefilter, String objectfilter, Double confidenceFilter) throws Exception {
        Stream stream = rdfdb.openStream(order, subjectfilter, predicatefilter, objectfilter, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getJoinIteratorSMJ(stream, 2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator =
                bpTripleJoinDriver2.getJoinIteratorSMJ(firstLevelJoinIterator.getKey(),
                        firstLevelJoinIterator.getValue());

        BasicPattern basicPattern = firstLevelJoinIterator.getKey().get_next();
//        Quadruple q = stream.getNext();
        while (basicPattern != null) {
            basicPattern.printBasicPatternValues();
            basicPattern = firstLevelJoinIterator.getKey().get_next();
        }

        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static void openStreamAndExecuteIndexNLJ(BPTripleJoinDriver bpTripleJoinDriver1, BPTripleJoinDriver bpTripleJoinDriver2, int order, String subjectfilter, String predicatefilter, String objectfilter, Double confidenceFilter) throws Exception {
        Stream stream = rdfdb.openStream(order, subjectfilter, predicatefilter, objectfilter, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getIndexNLJoinIterator(stream, 2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator =
                bpTripleJoinDriver2.getIndexNLJoinIterator(firstLevelJoinIterator.getKey(),
                        firstLevelJoinIterator.getValue());

        BasicPattern basicPattern = secondLevelJoinIterator.getKey().get_next();
//        Quadruple q = stream.getNext();
        while (basicPattern != null) {
            basicPattern.printBasicPatternValues();
            basicPattern = secondLevelJoinIterator.getKey().get_next();
        }
        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static void openStreamAndExecuteNLJ(BPTripleJoinDriver bpTripleJoinDriver1, BPTripleJoinDriver bpTripleJoinDriver2, int ORDER, String SUBJECTFILTER, String PREDICATEFILTER, String OBJECTFILTER, Double confidenceFilter) throws Exception {
        Stream stream = rdfdb.openStream(ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getNLJoinIterator(stream, 2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator =
                bpTripleJoinDriver2.getNLJoinIterator(firstLevelJoinIterator.getKey(),
                        firstLevelJoinIterator.getValue());

        BasicPattern basicPattern = secondLevelJoinIterator.getKey().get_next();
        while (basicPattern != null) {
            basicPattern.printBasicPatternValues();
            basicPattern = secondLevelJoinIterator.getKey().get_next();
        }
        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static BPTripleJoinDriver getJoinDriver(String joinQuery) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException, IteratorException, HashEntryNotFoundException, IndexException, ScanIteratorException, PinPageException, InvalidFrameNumberException, UnknownIndexTypeException, UnpinPageException, UnknownKeyTypeException, KeyNotMatchException, InvalidTupleSizeException, PageUnpinnedException, InvalidTypeException, ReplacerException, HFDiskMgrException, InvalidRelation, HFException, NestedLoopException, FileScanException, HFBufMgrException, TupleUtilsException {
        String[] firstJoinParams = joinQuery.split(",");
        int memoryAmount = Integer.parseInt(firstJoinParams[0]);
        int numLeftNodes = Integer.parseInt(firstJoinParams[1]);
        int bpJoinNodePosition = Integer.parseInt(firstJoinParams[2]);
        int joinOnSubjectOrObject = Integer.parseInt(firstJoinParams[3]);
        String rightSubjectFilter = firstJoinParams[4];
        String rightPredicateFilter = firstJoinParams[5];
        String rightObjectFilter = firstJoinParams[6];
        double rightConfidenceFilter = Double.parseDouble(firstJoinParams[7]);
        int[] leftOutNodePositions = new int[]{Integer.parseInt(firstJoinParams[8])};
        int outputRightSubject = Integer.parseInt(firstJoinParams[9]);
        int outputRightObject = Integer.parseInt(firstJoinParams[10]);

        rdfdb.initializeEntityBTreeFiles();
        rdfdb.initializePredicateBTreeFile();
        LID subjectId = IndexUtils.isLabelRecordInBtreeFound(SystemDefs.JavabaseDB.getSubjectBtreeIndexFile(),
                rightSubjectFilter);
        LID predicateId = IndexUtils.isLabelRecordInBtreeFound(SystemDefs.JavabaseDB.getPredicateBtreeIndexFile(),
                rightPredicateFilter);
        LID objectId = IndexUtils.isLabelRecordInBtreeFound(SystemDefs.JavabaseDB.getObjectBtreeIndexFile(),
                rightObjectFilter);
        rdfdb.closeEntityBTreeFile();
        rdfdb.closePredicateBTreeFile();

        BPTripleJoinDriver bpTripleJoinDriver = new BPTripleJoinDriver(memoryAmount, numLeftNodes,
                bpJoinNodePosition, joinOnSubjectOrObject,
                subjectId, predicateId,
                objectId, rightConfidenceFilter, leftOutNodePositions,
                outputRightSubject, outputRightObject);
        return bpTripleJoinDriver;
    }
}

class Utils {
    static final String BATCH_INSERT = "batchinsert";
    static final String QUERY = "query";
    static final String REPORT = "report";
}

