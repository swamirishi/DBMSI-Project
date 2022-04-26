package diskmgr;

import basicpatternheap.BasicPattern;
import btree.*;
import bufmgr.HashEntryNotFoundException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;
import com.google.gson.Gson;
import global.*;
import heap.*;
import index.IndexException;
import index.IndexUtils;
import index.UnknownIndexTypeException;
import iterator.*;
import iterator.bp.BPSort;
import iterator.interfaces.IteratorI;
import javafx.util.Pair;
import quadrupleheap.Quadruple;
import vo.Join;
import vo.Query;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommandLine {
    public static RDFDB rdfdb;
    public static int numbuf;
    public static SystemDefs systemDefs;
    public static String fileName;

    public static void main(String[] args) throws Exception {
//        SystemDefs.MINIBASE_RESTART_FLAG = true;

        System.out.println("Input formats: ");
        System.out.println("batchinsert DATAFILENAME RDFDBNAME");
        System.out.println("query RDFDBNAME QUERYFILE NUMBUF");

//        batchinsert X:\Phase3\phase2.txt report_db
//        query test_db X:\Phase3\queryFile1.txt 500

//        batchinsert /Users/swamirishi/Documents/asu/Spring_2022/DBMSI/DBMSI-Project/phase1.txt 1 swami_db
//        query real_db 1 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query swami_db 3 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        batchinsert /Users/dhruv/ASU/Sem2/DBMSI/Project2/test1.txt 6 popi
//        query bablu 1 1 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query bablu 1 1 :Bernhard_A_M_Seefeld :name :Bernhard_A_M_Seefeld * 5000
//        batchinsert D:\DBMSI-Project\phase1.txt 1 real_db
//        query test_db_200 1 1 :Jorunn_Danielsen * * * 10
//        query test_db 6 6 :Jorunn_Danielsen * * * 10000
//        batchinsert Users/dhruv/ASU/Sem2/DBMSI/Project2/test2.txt 1 popi
//        query real_db 1 2 * * * * 5000
//        query swami_db 1 2 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 3 3 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 4 4 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
//        query test_db 5 5 :Jorunn_Danielsen :knows :Eirik_Newth * 5000
        //        query test_db 1 1 * * * * 5000

//        report


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
        //batchinsert DATAFILENAME RDFDBNAME,        input => 0-indexed
        System.out.println("Insertion Started");
        long startTime = System.currentTimeMillis();
        String dbName = input[2];
        int index_option = 0;
        String dbPath = dbName + "_" + index_option;

        File file = new File(dbPath);
        if (file.exists()) {
            //open existing database
            systemDefs = new SystemDefs(dbPath, 0, 10000, "Clock", index_option);
        } else {
            //create a new db
            systemDefs = new SystemDefs(dbPath, 50000, 50000, "Clock", index_option);
        }

        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.name = dbPath;

        fileName = input[1];
        
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

    private static void insertTestData(String[] tokens) throws Exception {
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

        System.out.println(
                "Record Count of Quadruples in the database(" + rdfdb.db_name() + ") = " + recordCountQuadruple);
        System.out.println(
                "Record Count of Total Entities in the database(" + rdfdb.db_name() + ") = " + recordCountEntity);
        System.out.println("Record Count of Subject in the database(" + rdfdb.db_name() + ") = " + recordCountSubject);
        System.out.println("Record Count of Objects in the database(" + rdfdb.db_name() + ") = " + recordCountObject);
        System.out.println(
                "Record Count of Predicate in the database(" + rdfdb.db_name() + ") = " + recordCountPredicate);
        if(fileName!=null){
            StatUtils.printReport(fileName);
        }
        
    }

    private static void runQuery(String[] input) throws Exception {
        //query RDFDBNAME QUERYFILE NUMBUF
        String RDFDBNAME = input[0];
        String queryFilePath = input[1];
        Query query = new Gson().fromJson(Utils.getFileAsString(queryFilePath),Query.class);
        int INDEXOPTION = 0;
        int ORDER = 0;
        String SUBJECTFILTER = query.getFilter(0);
        String PREDICATEFILTER = query.getFilter(1);
        String OBJECTFILTER = query.getFilter(2);
        String CONFIDENCEFILTER = query.getFilter(3);
        numbuf = input[2] != null ? Integer.parseInt(input[2]) : 1000;
        String dbPath = RDFDBNAME + "_" + INDEXOPTION;
        File file = new File(dbPath);

        if (file.exists()) {
            //open existing database
            systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
        } else {
            //create a new db
            systemDefs = new SystemDefs(dbPath, 5000000, numbuf, "Clock", INDEXOPTION);
        }

        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.name = dbPath;

        System.out.println("Warning!: Number of Buffers changed to: " + SystemDefs.JavabaseBM.getNumBuffers());

        Double confidenceFilter = "*".equals(CONFIDENCEFILTER) ? 0 : Double.parseDouble(CONFIDENCEFILTER);
        
        int sortNodePosition = query.getSort().getSortNodePosition();
//        int sortNodePosition = sc.nextInt();
        sortNodePosition = sortNodePosition == -1 ? BasicPattern.VALUE_FLD : BasicPattern.getOffset(sortNodePosition);
        int sortOrder = query.getSort().getSortOrder();
        TupleOrder bpOrder = new TupleOrder(sortOrder);
        int sortNumberOfPages = query.getSort().getNumberOfPages();
        BPTripleJoinDriver bpTripleJoinDriver1 = getJoinDriver(query.getJoin1());
        BPTripleJoinDriver bpTripleJoinDriver2 = getJoinDriver(query.getJoin2());
        String finalSUBJECTFILTER = SUBJECTFILTER;
        String finalPREDICATEFILTER = PREDICATEFILTER;
        String finalOBJECTFILTER = OBJECTFILTER;
        int finalSortNodePosition = sortNodePosition;
        new TimeElapsed("Nested Loop Join") {
            @Override
            public void doMethod() throws Exception {
                openStreamAndExecuteNLJ(bpTripleJoinDriver1,
                                        bpTripleJoinDriver2,
                                        ORDER, finalSUBJECTFILTER, finalPREDICATEFILTER, finalOBJECTFILTER,
                                        confidenceFilter, finalSortNodePosition,
                                        bpOrder,
                                        sortNumberOfPages);
            }
        }.run();
        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
        SystemDefs.close();
        
        systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.name = dbPath;
        new TimeElapsed("Index Based Nested Loop Join") {
            @Override
            public void doMethod() throws Exception {
                openStreamAndExecuteIndexNLJ(bpTripleJoinDriver1,
                                             bpTripleJoinDriver2,
                                             ORDER,
                                             finalSUBJECTFILTER, finalPREDICATEFILTER, finalOBJECTFILTER,
                                             confidenceFilter, finalSortNodePosition,
                                             bpOrder,
                                             sortNumberOfPages);
            }
        }.run();
        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
        SystemDefs.close();
//
        systemDefs = new SystemDefs(dbPath, 0, numbuf, "Clock", INDEXOPTION);
        rdfdb = SystemDefs.JavabaseDB;
        rdfdb.name = dbPath;
        new TimeElapsed("Sort Merge Join") {
            @Override
            public void doMethod() throws Exception {
                openStreamAndExecuteSMJ(bpTripleJoinDriver1,
                                        bpTripleJoinDriver2,
                                        ORDER,
                                        finalSUBJECTFILTER, finalPREDICATEFILTER, finalOBJECTFILTER,
                                        confidenceFilter, finalSortNodePosition, bpOrder, sortNumberOfPages
                                       );
            }
        }.run();
        System.out.println("Unpinned: " + SystemDefs.JavabaseBM.getNumUnpinnedBuffers());
        SystemDefs.close();
    }

    private static IteratorI<BasicPattern> getSortIterator(IteratorI<BasicPattern> bpIterator,
                                          int numberOfNodes,
                                          int sortField,
                                          TupleOrder sortOrder,
                                          int numberOfPages) throws IOException, SortException {
        Pair<Integer, AttrType[]> hdr = BPTripleJoinDriver.getHdr(numberOfNodes);
        return new BPSort(hdr.getValue(),
                hdr.getKey().shortValue(),
                BasicPattern.strSizes,
                bpIterator,
                sortField,
                sortOrder,
                4,
                numberOfPages) {
            @Override
            public boolean isReferenceBased() {
                return true;
            }
        };
    }

    private static void openStreamAndExecuteSMJ(BPTripleJoinDriver bpTripleJoinDriver1,
                                                BPTripleJoinDriver bpTripleJoinDriver2,
                                                int order,
                                                String subjectfilter,
                                                String predicatefilter,
                                                String objectfilter,
                                                Double confidenceFilter,
                                                int sortNodePosition,
                                                TupleOrder tupleOrder,
                                                int sortNumberOfPages) throws Exception {
        Stream stream = rdfdb.openStream(order, subjectfilter, predicatefilter, objectfilter, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getJoinIteratorSMJ(
                        stream,
                        2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator = bpTripleJoinDriver2.getJoinIteratorSMJ(
                firstLevelJoinIterator.getKey(),
                firstLevelJoinIterator.getValue());
        IteratorI<BasicPattern> sortIterator = getSortIterator(secondLevelJoinIterator.getKey(),
                secondLevelJoinIterator.getValue(),
                sortNodePosition,
                tupleOrder,
                sortNumberOfPages);
        BasicPattern basicPattern = sortIterator.get_next();
        int cnt =0;
//        Quadruple q = stream.getNext();
        while (basicPattern != null) {
            cnt+=1;
            basicPattern.printBasicPatternValues();
            basicPattern = sortIterator.get_next();
        }
        System.out.println(String.format("Total Number of Records = %d",cnt));
        sortIterator.close();
        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static void openStreamAndExecuteIndexNLJ(BPTripleJoinDriver bpTripleJoinDriver1,
                                                     BPTripleJoinDriver bpTripleJoinDriver2,
                                                     int order,
                                                     String subjectfilter,
                                                     String predicatefilter,
                                                     String objectfilter,
                                                     Double confidenceFilter,
                                                     int sortNodePosition,
                                                     TupleOrder tupleOrder,
                                                     int sortNumberOfPages) throws Exception {
        Stream stream = rdfdb.openStream(order, subjectfilter, predicatefilter, objectfilter, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getIndexNLJoinIterator(
                        stream,
                        2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator = bpTripleJoinDriver2.getIndexNLJoinIterator(
                firstLevelJoinIterator.getKey(),
                firstLevelJoinIterator.getValue());

        IteratorI<BasicPattern> sortIterator = getSortIterator(secondLevelJoinIterator.getKey(),
                secondLevelJoinIterator.getValue(),
                sortNodePosition,
                tupleOrder,
                sortNumberOfPages);
        BasicPattern basicPattern = sortIterator.get_next();
//        Quadruple q = stream.getNext();
        int cnt =0;
        while (basicPattern != null) {
            cnt+=1;
            basicPattern.printBasicPatternValues();
            basicPattern = sortIterator.get_next();
        }
        System.out.println(String.format("Total Number of Records = %d",cnt));
        sortIterator.close();
        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static void openStreamAndExecuteNLJ(BPTripleJoinDriver bpTripleJoinDriver1,
                                                BPTripleJoinDriver bpTripleJoinDriver2,
                                                int ORDER,
                                                String SUBJECTFILTER,
                                                String PREDICATEFILTER,
                                                String OBJECTFILTER,
                                                Double confidenceFilter,
                                                int sortNodePosition,
                                                TupleOrder tupleOrder,
                                                int sortNumberOfPages) throws Exception {
        
        Stream stream = rdfdb.openStream(ORDER, SUBJECTFILTER, PREDICATEFILTER, OBJECTFILTER, confidenceFilter);
        Pair<IteratorI<BasicPattern>, Integer> firstLevelJoinIterator = bpTripleJoinDriver1.
                getNLJoinIterator(
                        stream,
                        2);

        Pair<IteratorI<BasicPattern>, Integer> secondLevelJoinIterator = bpTripleJoinDriver2.getNLJoinIterator(
                firstLevelJoinIterator.getKey(),
                firstLevelJoinIterator.getValue());

        IteratorI<BasicPattern> sortIterator = getSortIterator(secondLevelJoinIterator.getKey(),
                secondLevelJoinIterator.getValue(),
                sortNodePosition,
                tupleOrder,
                sortNumberOfPages);
        BasicPattern basicPattern = sortIterator.get_next();
        int cnt = 0;
//        Quadruple q = stream.getNext();
        while (basicPattern != null) {
            basicPattern.printBasicPatternValues();
            basicPattern = sortIterator.get_next();
            cnt+=1;
        }
        System.out.println(String.format("Total Number of Records = %d",cnt));

        sortIterator.close();
        secondLevelJoinIterator.getKey().close();
        firstLevelJoinIterator.getKey().close();
        stream.closeStream();

        System.out.println("Disk page READ COUNT: " + PCounter.rcounter);
        System.out.println("Disk page WRITE COUNT: " + PCounter.wcounter);
    }

    private static BPTripleJoinDriver getJoinDriver(Join join) throws ConstructPageException, AddFileEntryException, GetFileEntryException, IOException, IteratorException, HashEntryNotFoundException, IndexException, ScanIteratorException, PinPageException, InvalidFrameNumberException, UnknownIndexTypeException, UnpinPageException, UnknownKeyTypeException, KeyNotMatchException, InvalidTupleSizeException, PageUnpinnedException, InvalidTypeException, ReplacerException, HFDiskMgrException, InvalidRelation, HFException, NestedLoopException, FileScanException, HFBufMgrException, TupleUtilsException {
//        System.out.println(String.format("Enter Memory Amount for %d Level Join",joinNumber));
        int memoryAmount = 20;
//        System.out.println(String.format("Enter Memory Amount for %d Level Join",joinNumber));
        int numLeftNodes = 10;
        int bpJoinNodePosition = join.getJoinNodePosition();
        int joinOnSubjectOrObject = join.getJoinOnSubjectOrObject();
        String[] rightFilters = join.getRightFilters().toArray(new String[join.getRightFilters().size()]);
        String rightSubjectFilter = rightFilters[0];
        String rightPredicateFilter = rightFilters[1];
        String rightObjectFilter = rightFilters[2];
        String inputConfidenceFilter = rightFilters[3];
        double rightConfidenceFilter = "*".equals(inputConfidenceFilter)?0:Double.parseDouble(inputConfidenceFilter);
        int[] leftOutNodePositions = join.getLeftOutNodePositions().stream().mapToInt(i->i).toArray();
        int outputRightSubject = join.getOutputRightSubject();
        int outputRightObject = join.getOutputRightObject();

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

        BPTripleJoinDriver bpTripleJoinDriver = new BPTripleJoinDriver(memoryAmount,
                numLeftNodes,
                bpJoinNodePosition,
                joinOnSubjectOrObject,
                subjectId,
                predicateId,
                objectId,
                rightConfidenceFilter,
                leftOutNodePositions,
                outputRightSubject,
                outputRightObject);
        return bpTripleJoinDriver;
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

