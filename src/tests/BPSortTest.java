package tests;

import basicpatternheap.BasicPattern;
import basicpatternheap.BasicPatternHeapFile;
import bpiterator.BPSort;
import diskmgr.RDFDB;
import global.*;
import heap.*;
import iterator.BPIterator;
import iterator.Iterator;
import iterator.SortException;
import labelheap.Label;
import labelheap.LabelHeapFile;
import quadrupleheap.Quadruple;
import basicpatternheap.TScan;

import java.io.IOException;

public class BPSortTest {
    public static void main(String args[]) throws HFDiskMgrException, HFException, HFBufMgrException, IOException, InvalidTupleSizeException, InvalidTypeException, SpaceNotAvailableException, InvalidSlotNumberException, SortException {
        SystemDefs sysdef = new SystemDefs("test1", 8193, 10000, "Clock");
        RDFDB rdfDB = SystemDefs.JavabaseDB;
        AttrType[] attrTypes = BasicPattern.headerTypes;
        short len = (short) attrTypes.length;
        short[] strSizes = BasicPattern.strSizes;

        LabelHeapFile bpEntityHeapFile = new LabelHeapFile("bpEntityHeapFile");
        BasicPattern bp1 = new BasicPattern();
        BasicPattern bp2 = new BasicPattern();

        bp1.setValue(1);
        NID nid1 = bpEntityHeapFile.insertRecord(new Label("entity1").getLabelByteArray()).returnNid();
        NID nid2 = bpEntityHeapFile.insertRecord(new Label("entity2").getLabelByteArray()).returnNid();
        NID nid3 = bpEntityHeapFile.insertRecord(new Label("entity3").getLabelByteArray()).returnNid();
        NID nid4 = bpEntityHeapFile.insertRecord(new Label("entity4").getLabelByteArray()).returnNid();

        bp1.addNode(nid1);
        bp1.addNode(nid2);
        bp2.addNode(nid3);
        bp2.addNode(nid4);

        BasicPatternHeapFile bpHeapFile = new BasicPatternHeapFile("bpHeapFile");
        BPID bpid1 = bpHeapFile.insertRecord(bp1.returnTupleByteArray());
        BPID bpid2 = bpHeapFile.insertRecord(bp2.returnTupleByteArray());

        rdfDB.setBpEntityLabelHeapFile(bpEntityHeapFile);
        rdfDB.setBpHeafFile(bpHeapFile);

        BPIterator bpIterator = new BPIterator(bpHeapFile);
        BPSort bpSort = new BPSort(rdfDB, attrTypes, len, strSizes,
                bpIterator, 1, new BPOrder(BPOrder.Ascending), 1, 1024);

    }
}
