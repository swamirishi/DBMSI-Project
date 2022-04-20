package tests;

import basicpatternheap.BasicPattern;
import basicpatternheap.BasicPatternHeapFile;
import bpiterator.BPSort;
import diskmgr.RDFDB;
import global.*;
import iterator.BPFileScan;
import iterator.FldSpec;
import iterator.interfaces.IteratorI;
import labelheap.Label;
import labelheap.LabelHeapFile;

public class BPSortTest {
    public static void main(String args[]) throws Exception {
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

        AttrType[] basicPatternAttrTypes = BasicPattern.headerTypes;
        int basicPatternAttrTypesLen = BasicPattern.numberOfFields;

        FldSpec[] basicPatternProjectionList = BasicPattern.getProjectListForAllColumns();

        IteratorI<BasicPattern> bpFileScan = new BPFileScan("basicPatternHeapFile",
                BasicPattern.headerTypes, BasicPattern.strSizes, (short) 0, basicPatternProjectionList.length,
                basicPatternProjectionList, null);

        iterator.bp.BPIterator bpIterator = (iterator.bp.BPIterator) bpFileScan;
        BPSort bpSort = new BPSort(rdfDB, attrTypes, len, strSizes,
                bpIterator, 1, new BPOrder(BPOrder.Ascending), 1, 1024);

        BasicPattern bpTemp;
        int i = 0;
        while((bpTemp = bpSort.get_next()) != null){
            System.out.println("ID: " + i++ + "  Value: " + bpTemp.toString());
        }


    }
}
