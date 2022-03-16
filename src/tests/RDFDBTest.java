package tests;

import diskmgr.*;
import global.PageId;
import global.SystemDefs;

import java.io.IOException;

public class RDFDBTest {
    public static void main(String[] args) throws FileNameTooLongException, InvalidRunSizeException, InvalidPageNumberException, DuplicateEntryException, FileIOException, IOException, OutOfSpaceException, DiskMgrException {
        String dbpath = "quadrupleHeapFile";
        PageId pgid = new PageId();
        pgid.pid = 0;
        SystemDefs sysdef = new SystemDefs(dbpath, 8193, 100, "Clock");
        SystemDefs.JavabaseDB.add_file_entry("quadrupleHeapFile", pgid);
        RDFDB rdfdb = new RDFDB(0);
        rdfdb.insertEntity("Abhishek");
        rdfdb.deleteEntity("Abhishe");
        rdfdb.openStream()
    }
}
