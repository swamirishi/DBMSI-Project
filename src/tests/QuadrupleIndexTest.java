package tests;

import btree.*;
import btree.label.LIDBTreeFile;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import index.IndexException;
import index.IndexScan;
import index.UnknownIndexTypeException;
import index.label.LIDIndexScan;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.UnknownKeyTypeException;
import labelheap.LScan;
import labelheap.Label;
import labelheap.LabelHeapFile;
import qiterator.QuadrupleUtils;
import quadrupleheap.Quadruple;

import java.io.IOException;

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
        test1();
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
        LabelHeapFile f = new LabelHeapFile("test1.in");

        t = new Label(size);
        t.setHdr((short) 1, attrType, attrSize);

        for (int i = 0; i < NUM_RECORDS; i++) {
            t.setStrFld(1, data1[i]);
            rid = f.insertRecord(t.returnTupleByteArray());
        }

        // create an scan on the heapfile
        LScan scan = new LScan(f);

        // create the index file
        LIDBTreeFile btf = new LIDBTreeFile("BTreeIndex", AttrType.attrString, REC_LEN1, 1/*delete*/);

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

        // start index scan
        LIDIndexScan iscan = new LIDIndexScan(new IndexType(IndexType.B_Index),
                "test1.in", "BTreeIndex", attrType, attrSize,
                1, 1, projlist, null, 1, true);

        int count = 0;
        t = iscan.get_next();
        String outval = null;

        boolean flag = true;

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
}