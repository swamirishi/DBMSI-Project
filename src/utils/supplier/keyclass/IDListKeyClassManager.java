package utils.supplier.keyclass;

import btree.*;

import java.util.List;

public class IDListKeyClassManager implements KeyClassManager<List<?>> {
    
    private List<KeyClassManager> keyClassManagers;
    private int strMaxLength;
    private int intMaxLength;
    
    public IDListKeyClassManager(List<KeyClassManager> keyClassManagers, int strMaxLength, int intMaxLength) {
        this.keyClassManagers = keyClassManagers;
        this.strMaxLength = strMaxLength;
        this.intMaxLength = intMaxLength;
    }
    public String getIntKey(int paddingSize, int value){
        return String.format("%0" + paddingSize + "d", value);
    }
    
    public String getString(KeyClass k){
        if(k instanceof IntegerKey){
            return getIntKey(this.intMaxLength,((IntegerKey) k).getKey());
        }
        if(k instanceof StringKey){
            String v = ((StringKey) k).getKey();
            if(v.length()<this.strMaxLength){
                v = v+getIntKey(this.strMaxLength-v.length(),0);
            }
            return v;
        }
        return "";
    }
    
    @Override
    public KeyClass getKeyClass(List<?> obj) throws KeyTooLongException, KeyNotMatchException {
        if(obj.size()!=keyClassManagers.size()){
            throw new KeyNotMatchException();
        }
        StringBuilder idxString = new StringBuilder();
        for(int i=0;i<keyClassManagers.size();i++){
            idxString.append(getString(keyClassManagers.get(i).getKeyClass(obj.get(i))));
        }
        return StringKeyClassManager.getSupplier().getKeyClass(idxString.toString());
    }
}
