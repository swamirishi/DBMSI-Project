package utils.supplier.btfile;

import btree.BTreeFile;
import btree.ConstructPageException;
import btree.GetFileEntryException;
import btree.PinPageException;
import btree.interfaces.BTreeFileI;
import btree.quadraple.QIDBTreeFile;
import global.QID;
import global.RID;
import heap.Tuple;
import quadrupleheap.Quadruple;

public class QIDBTreeFileSupplier implements BTreeFileSupplier<QID, Quadruple> {
    @Override
    public BTreeFileI<QID, Quadruple> getBTreeFile(String name) throws ConstructPageException, GetFileEntryException, PinPageException {
        return new QIDBTreeFile(name);
    }
    
    private QIDBTreeFileSupplier() {
    }
    
    private static QIDBTreeFileSupplier supplier;
    public static QIDBTreeFileSupplier getSupplier(){
        if(supplier == null){
            synchronized (QIDBTreeFileSupplier.class){
                if(supplier == null){
                    supplier = new QIDBTreeFileSupplier();
                }
            }
        }
        return supplier;
    }
}
