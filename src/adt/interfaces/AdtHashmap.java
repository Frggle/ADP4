package adt.interfaces;

public interface AdtHashmap {

    public static AdtHashmap create(String strategy, int size) {
        return null;
    }
    
    public void insert(String elem);
    
    public void dateiImport(String filename, int size);
    
    public int find(String elem);
    
    public void log();
    
    public long insertRuntime(String elem);
    
    public long findRuntime(String elem);
}
