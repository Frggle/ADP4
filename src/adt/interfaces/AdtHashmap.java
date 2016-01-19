package adt.interfaces;

public interface AdtHashmap {

    public static AdtHashmap create(String strategy, String filename) {
        return null;
    }
    
    public void insert(String elem);
    
    public int find(String elem);
}
