package tests;

import btree.*;
import btree.label.LIDBTreeFile;
import btree.quadraple.QIDBTreeFile;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import index.IndexException;
import index.UnknownIndexTypeException;
import index.label.LIDIndexScan;
import index.quadraple.QIDIndexScan;
import iterator.CondExpr;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.UnknownKeyTypeException;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;
import quadrupleheap.QuadrupleHeapFile;
import utils.supplier.keyclass.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuadrupleIndexTest {
    private static String data1[] = {
            "raghu", "xbao", "cychan", "leela", "ketola", "soma", "ulloa",
            "dhanoa", "dsilva", "kurniawa", "dissoswa", "waic", "susanc", "kinc",
            "marc", "scottc", "yuc", "ireland", "rathgebe", "joyce", "daode",
            "yuvadee", "he", "huxtable", "muerle", "flechtne", "thiodore", "jhowe",
            "frankief", "yiching", "xiaoming", "jsong", "yung", "muthiah", "bloch",
            "binh", "dai", "hai", "handi", "shi", "sonthi", "evgueni", "chung-pi",
            "chui", "siddiqui", "mak", "tak", "sungk", "randal", "barthel",
            "newell", "schiesl", "neuman", "heitzman", "wan", "gunawan", "djensen",
            "juei-wen", "josephin", "harimin", "xin", "zmudzin", "feldmann",
            "joon", "wawrzon", "yi-chun", "wenchao", "seo", "karsono", "dwiyono",
            "ginther", "keeler", "peter", "lukas", "edwards", "mirwais", "schleis",
            "haris", "meyers", "azat", "shun-kit", "robert", "markert", "wlau",
            "honghu", "guangshu", "chingju", "bradw", "andyw", "gray", "vharvey",
            "awny", "savoy", "meltz"};

    private static String data2[] = {
            "andyw", "awny", "azat", "barthel", "binh", "bloch", "bradw",
            "chingju", "chui", "chung-pi", "cychan", "dai", "daode", "dhanoa",
            "dissoswa", "djensen", "dsilva", "dwiyono", "edwards", "evgueni",
            "feldmann", "flechtne", "frankief", "ginther", "gray", "guangshu",
            "gunawan", "hai", "handi", "harimin", "haris", "he", "heitzman",
            "honghu", "huxtable", "ireland", "jhowe", "joon", "josephin", "joyce",
            "jsong", "juei-wen", "karsono", "keeler", "ketola", "kinc", "kurniawa",
            "leela", "lukas", "mak", "marc", "markert", "meltz", "meyers",
            "mirwais", "muerle", "muthiah", "neuman", "newell", "peter", "raghu",
            "randal", "rathgebe", "robert", "savoy", "schiesl", "schleis",
            "scottc", "seo", "shi", "shun-kit", "siddiqui", "soma", "sonthi",
            "sungk", "susanc", "tak", "thiodore", "ulloa", "vharvey", "waic",
            "wan", "wawrzon", "wenchao", "wlau", "xbao", "xiaoming", "xin",
            "yi-chun", "yiching", "yuc", "yung", "yuvadee", "zmudzin"};

    private static int NUM_RECORDS = data2.length;
    private static int LARGE = 1000;
    private static short REC_LEN1 = 32;
    private static short REC_LEN2 = 160;

    public static void main(String[] args) throws IteratorException, ConvertException, InsertException, NodeNotMatchException, InvalidSlotNumberException, LeafInsertRecException, UnknownIndexTypeException, SpaceNotAvailableException, DeleteRecException, FieldNumberOutOfBoundException, AddFileEntryException, UnknownKeyTypeException, HFException, KeyNotMatchException, IndexSearchException, ConstructPageException, IndexException, IndexInsertRecException, LeafDeleteException, GetFileEntryException, PinPageException, IOException, UnpinPageException, KeyTooLongException, HFDiskMgrException, InvalidTupleSizeException, HFBufMgrException, InvalidTypeException {
        String dbpath = "quadrupleHeapFile";
        PageId pgid = new PageId();
        pgid.pid = 0;
        SystemDefs sysdef = new SystemDefs(dbpath, 8193, 100, "Clock");
//        SystemDefs.JavabaseDB.add_file_entry("quadrupleHeapFile", pgid);
        RDFDB rdfdb = new RDFDB(0);
        QuadrupleUtils.rdfdb = rdfdb;
//        test1();
        test2();
    }

    public static void test1() throws InvalidTupleSizeException, IOException, InvalidTypeException, IndexException, HFDiskMgrException, HFException, HFBufMgrException, FieldNumberOutOfBoundException, SpaceNotAvailableException, InvalidSlotNumberException, ConstructPageException, AddFileEntryException, GetFileEntryException, IteratorException, ConvertException, InsertException, IndexInsertRecException, LeafDeleteException, NodeNotMatchException, LeafInsertRecException, PinPageException, UnpinPageException, DeleteRecException, KeyTooLongException, KeyNotMatchException, IndexSearchException, UnknownKeyTypeException, UnknownIndexTypeException {
        AttrType[] attrType = new AttrType[1];
        attrType[0] = new AttrType(AttrType.attrString);
        short[] attrSize = new short[1];
        attrSize[0] = REC_LEN2;

        // create a tuple of appropriate size
        Label t = new Label();
        t.setHdr((short) 1, attrType, attrSize);

        int size = t.size();

        // Create unsorted data file "test1.in"
        LID rid;
        LID rid2;
        LID rid3;
        LabelHeapFile f = new LabelHeapFile("test1.in");
        LabelHeapFile f1 = new LabelHeapFile("test2.in");
        QuadrupleHeapFile qf = new QuadrupleHeapFile("test4.in");


        t = new Label(size);
        t.setHdr((short) 1, attrType, attrSize);

        QIDBTreeFile<LID> qtf = new QIDBTreeFile<LID>("QIDBTreeIndex", AttrType.attrInteger, 4, 1/*delete*/) {
            @Override
            public KeyClassManager<LID> getKeyClassManager() {
                return LIDKeyClassManager.getSupplier();
            }
        };

        for (int i = 0; i < NUM_RECORDS; i++) {
            t.setStrFld(1, data1[i]);
            rid = f.insertRecord(t.returnTupleByteArray());
            rid2 = f1.insertRecord(t.returnTupleByteArray());
            rid3 = f.insertRecord(t.returnTupleByteArray());
            Quadruple q = new Quadruple(rid.returnEid(), rid2.returnPid(), rid3.returnEid(), 1);
            QID qid = qf.insertRecord(q.getQuadrupleByteArray());
            System.out.print(rid + " ");
            qtf.insert(rid, qid);
        }

        // create an scan on the heapfile
        LScan scan = new LScan(f);

        // create the index file
        LIDBTreeFile<Void> btf = new LIDBTreeFile<Void>("BTreeIndex", AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };

        System.out.println("BTreeIndex created successfully.\n");

        rid = new LID();
        Tuple temp = scan.getNext(rid);
        while (temp != null) {
            t.tupleCopy(temp);
            String key = t.getStrFld(1);
            btf.insert(new StringKey(key), rid);
            temp = scan.getNext(rid);
        }

        // close the file scan
        scan.closescan();


        System.out.println("BTreeIndex file created successfully.\n");

        FldSpec[] projlist = new FldSpec[1];
        RelSpec rel = new RelSpec(RelSpec.outer);
        projlist[0] = new FldSpec(rel, 1);

        FldSpec[] projlist2 = new FldSpec[7];
        for(int i=0;i<7;i++)
            projlist2[i] = new FldSpec(rel, i+1);

        // start index scan
        LIDIndexScan<Void> iscan = new LIDIndexScan<Void>(new IndexType(IndexType.B_Index),
                "test1.in",
                "BTreeIndex",
                attrType,
                attrSize,
                1,
                1,
                projlist,
                null,
                1,
                true) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
        IndexType indexType = new IndexType(IndexType.B_Index);
        QIDIndexScan<LID> qidScan = new QIDIndexScan<LID>(indexType, "test4.in", "QIDBTreeIndex",
                Quadruple.headerTypes, Quadruple.strSizes, 7, 7, projlist2, null, 1, false) {
            @Override
            public KeyClassManager<LID> getKeyClassManager() {
                return LIDKeyClassManager.getSupplier();
            }
        };

        int count = 0;
        Quadruple res = qidScan.get_next();
        LID outval1 = null;

        boolean flag = true;

        while (res != null) {
            outval1 = res.getSubject().returnLid();
            System.out.println(outval1);
            count++;
            res = qidScan.get_next();
        }

        // clean up
        qidScan.close();

        System.err.println("------------------- TEST 1 completed ---------------------\n");

        t = iscan.get_next();
        String outval = null;

        flag = true;

        while (t != null) {
            if (count >= NUM_RECORDS) {
                System.err.println("Test1 -- OOPS! too many records");
                flag = false;
                break;
            }
            outval = t.getStrFld(1);

            if (outval.compareTo(data2[count]) != 0) {
                System.err.println("outval = " + outval + "\tdata2[count] = " + data2[count]);

                System.err.println("Test1 -- OOPS! index scan not in sorted order");
            }
            count++;
            t = iscan.get_next();
            System.out.println(t.getLabel());
        }
        if (count < NUM_RECORDS) {
            System.err.println("Test1 -- OOPS! too few records");
        } else if (flag) {
            System.err.println("Test1 -- Index Scan OK");
        }

        // clean up
        iscan.close();

        System.err.println("------------------- TEST 1 completed ---------------------\n");
    }
    public static void test2() throws InvalidTupleSizeException, IOException, InvalidTypeException, IndexException, HFDiskMgrException, HFException, HFBufMgrException, FieldNumberOutOfBoundException, SpaceNotAvailableException, InvalidSlotNumberException, ConstructPageException, AddFileEntryException, GetFileEntryException, IteratorException, ConvertException, InsertException, IndexInsertRecException, LeafDeleteException, NodeNotMatchException, LeafInsertRecException, PinPageException, UnpinPageException, DeleteRecException, KeyTooLongException, KeyNotMatchException, IndexSearchException, UnknownKeyTypeException, UnknownIndexTypeException {
        AttrType[] attrType = new AttrType[1];
        attrType[0] = new AttrType(AttrType.attrString);
        short[] attrSize = new short[1];
        attrSize[0] = REC_LEN2;

        // create a tuple of appropriate size
        Label t = new Label();
        t.setHdr((short) 1, attrType, attrSize);

        int size = t.size();

        // Create unsorted data file "test1.in"
        LID rid;
        LID rid2;
        LID rid3;
        LabelHeapFile f = new LabelHeapFile("test1.in");
        LabelHeapFile f1 = new LabelHeapFile("test2.in");
        QuadrupleHeapFile qf = new QuadrupleHeapFile("test4.in");


        t = new Label(size);
        t.setHdr((short) 1, attrType, attrSize);
        List<KeyClassManager> list = Arrays.asList(LIDKeyClassManager.getSupplier(),LIDKeyClassManager.getSupplier());
        IDListKeyClassManager idListKeyClassManager = new IDListKeyClassManager(list,20,10);
        QIDBTreeFile<List<?>> qtf = new QIDBTreeFile<List<?>>("QIDBTreeIndex", AttrType.attrString, REC_LEN2, 1/*delete*/) {

            @Override
            public KeyClassManager<List<?>> getKeyClassManager() {
                return idListKeyClassManager;
            }
        };

        for (int i = 0; i < NUM_RECORDS; i++) {
            t.setStrFld(1, data1[i]);
            rid = f.insertRecord(t.returnTupleByteArray());
            rid2 = f1.insertRecord(t.returnTupleByteArray());
            rid3 = f.insertRecord(t.returnTupleByteArray());
            Quadruple q = new Quadruple(rid.returnEid(), rid2.returnPid(), rid3.returnEid(), (float) i);
            QID qid = qf.insertRecord(q.getQuadrupleByteArray());
            System.out.print(rid + " ");
            List l = Arrays.asList(rid, rid3);
            qtf.insert(l, qid);
        }

        // create an scan on the heapfile
        LScan scan = new LScan(f);

        // create the index file
        LIDBTreeFile<Void> btf = new LIDBTreeFile<Void>("BTreeIndex", AttrType.attrString, REC_LEN1, 1/*delete*/) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };

        System.out.println("BTreeIndex created successfully.\n");

        rid = new LID();
        Tuple temp = scan.getNext(rid);
        while (temp != null) {
            t.tupleCopy(temp);
            String key = t.getStrFld(1);
            btf.insert(new StringKey(key), rid);
            temp = scan.getNext(rid);
        }

        // close the file scan
        scan.closescan();


        System.out.println("BTreeIndex file created successfully.\n");

        FldSpec[] projlist = new FldSpec[1];
        RelSpec rel = new RelSpec(RelSpec.outer);
        projlist[0] = new FldSpec(rel, 1);

        FldSpec[] projlist2 = new FldSpec[7];
        for(int i=0;i<7;i++)
            projlist2[i] = new FldSpec(rel, i+1);

        CondExpr[] expr = new CondExpr[2];
        expr[0] = new CondExpr();
        expr[0].op = new AttrOperator(AttrOperator.aopEQ);
        expr[0].type1 = new AttrType(AttrType.attrSymbol);
        expr[0].type2 = new AttrType(AttrType.attrString);
        expr[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 1);
        expr[0].operand2.string = "yi-chun";
        expr[0].next = null;
        expr[1]=null;

        // start index scan
        LIDIndexScan<Void> iscan = new LIDIndexScan<Void>(new IndexType(IndexType.B_Index),
                "test1.in",
                "BTreeIndex",
                attrType,
                attrSize,
                1,
                1,
                projlist,
                expr,
                1,
                false) {
            @Override
            public KeyClassManager<Void> getKeyClassManager() {
                return null;
            }
        };
        IndexType indexType = new IndexType(IndexType.B_Index);
        CondExpr[] expr2 = new CondExpr[2];
        expr2[0] = new CondExpr();
        expr2[0].op = new AttrOperator(AttrOperator.aopGE);
        expr2[0].type1 = new AttrType(AttrType.attrSymbol);
        expr2[0].type2 = new AttrType(AttrType.attrReal);
        expr2[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer), 7);
        expr2[0].operand2.real = 10f;
        expr2[0].next = null;
        expr2[1]=null;

        QIDIndexScan<LID> qidScan = new QIDIndexScan<LID>(indexType, "test4.in", "QIDBTreeIndex",
                Quadruple.headerTypes, Quadruple.strSizes, 7, 7, projlist2, expr2, 1, false) {
            @Override
            public KeyClassManager<LID> getKeyClassManager() {
                return LIDKeyClassManager.getSupplier();
            }
        };

        int count = 0;
        Quadruple res = qidScan.get_next();
        LID outval1 = null;

        boolean flag = true;

        while (res != null) {
            outval1 = res.getSubject().returnLid();
            StringKey key = (StringKey) idListKeyClassManager.getKeyClass(Arrays.asList(res.getSubject().returnLid(), res.getObject().returnLid()));
//            System.out.println(key.getKey());
            System.out.println(res);
            count++;
            res = qidScan.get_next();
        }

        // clean up
        qidScan.close();

        System.err.println("------------------- TEST 1 completed ---------------------\n");

        t = iscan.get_next();
        String outval = null;

        while (t != null) {
            outval = t.getStrFld(1);
            System.out.println(outval);
            count++;
            t = iscan.get_next();
        }
        // clean up
        iscan.close();

        System.err.println("------------------- TEST 1 completed ---------------------\n");
    }

}