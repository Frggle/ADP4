package adt.interfaces;

public interface AdtHashmap {

    public static AdtHashmap create(String strategy, String filename) {
        return null;
    }
    
    public void insert(String elem);
    
    public int find(String elem);
    
    public void log();
    
    public long insertRuntime(String elem);
    
    public long findRuntime(String elem);
}
