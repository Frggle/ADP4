package adt.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import adt.HashObject;
import adt.interfaces.AdtHashmap;

public class AdtHashmapImpl implements AdtHashmap {

    private static List<String> fileInput;
    private static HashObject[] hashTable;
    private static String STRATEGY;
    private static String FILENAME;

    private static int m; // Array- Groesse

    private AdtHashmapImpl() {
        hashTable = new HashObject[m];
    }

    /**
     * Initialisieren
     * 
     * @param strategy
     * @param size
     * @return
     */
    public static AdtHashmap create(String strategy, int size) {
        STRATEGY = strategy;
        m = size;

        return new AdtHashmapImpl();
    }

    /**
     * Datei importieren
     * 
     * @param size
     *            , sollte Prim > Anzahl Woerter in Datei
     * @param filename
     */
    @Override
    public void dateiImport(String filename, int size) {
        FILENAME = filename;
        m = size;
        fileInput = new ArrayList<String>();
        new AdtHashmapImpl();
        readFile();
    }

    /**
     * Datei importieren, intern
     */
    private void readFile() {
        File file = new File(FILENAME);
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(file));
            while (scanner.hasNext()) {
                String elem = scanner.next();
                elem = elem.replace(",", "");
                elem = elem.replace(".", "");
                fileInput.add(elem.toLowerCase());
            }
        } catch (FileNotFoundException e) {
        } finally {
            scanner.close();
        }

        for (String string : fileInput) {
            insert(string);
        }
    }

    /**
     * Position 0..(n-1)
     * 
     * @return
     */
    private int hashFunction(HashObject hashObject) {
        int hash = builtHashValue(hashObject.getValue());

        if (STRATEGY.equals("L")) {
            for (int i = hash; i >= 0; i--) {
                i = i % m;
                if (hashTable[i] == null) {
                    return i;
                } else if (hashObject.getValue().equals(hashTable[i].getValue())) {
                    hashTable[i].inkrementVorkommen();
                    return Integer.MAX_VALUE;// doppelte Vorkommen erkannt
                }
                hashTable[i].inkrementKollision();
            }
            for (int i = m; i >= hash; i--) {
                int j = i % m;
                if (hashTable[j] == null) {
                    return i;
                } else if (hashObject.getValue().equals(hashTable[j].getValue())) {
                    hashTable[j].inkrementVorkommen();
                    return Integer.MAX_VALUE;// doppelte Vorkommen erkannt
                }
            }
        } else if (STRATEGY.equals("Q")) {
            for (int i = 0; i < m * 2; i++) {
                int n = (int) (hash - (Math.pow(-1, i) * Math.pow(Math.ceil(i / 2), 2)));
                n = n < 0 ? Math.floorMod(n, m) : (n % m);

                if (hashTable[n] == null) {
                    return n;
                } else if (hashObject.getValue().equals(hashTable[n].getValue())) {
                    hashTable[n].inkrementVorkommen();
                    return Integer.MAX_VALUE;// doppelte Vorkommen erkannt
                } else {
                    hashTable[n].inkrementKollision();
                }
            }
        } else if (STRATEGY.equals("B")) {
            for (int i = 0; i < m * 2; i++) {
                out();
                int currentHash = doubleHashing(hash, hashObject.getValue(), hashObject.getKollision());
                if (hashTable[currentHash] == null) {
                    return currentHash;
                } else {
                    HashObject existingElem = hashTable[hash];
                    hashObject.inkrementKollision();
                    int nextIndexOfNewElem = doubleHashing(hash, hashObject.getValue(), hashObject.getKollision());
                    int nextIndexOfExistingElem = doubleHashing(hash, existingElem.getValue(), existingElem.getKollision() + 1);
                    if(existingElem.getValue().equals(hashObject.getValue())) {
                        existingElem.inkrementVorkommen();
                        existingElem.inkrementKollision();
                        return Integer.MAX_VALUE; // doppelte Vorkommen erkannt
                    } else {
                        if(hashTable[nextIndexOfNewElem] == null) {
//                            hashObject.inkrementKollision();
                            return nextIndexOfNewElem;
                        } else if(hashTable[nextIndexOfExistingElem] == null) {
                            hashTable[nextIndexOfExistingElem] = existingElem;
                            hashTable[currentHash] = null;
                            hashObject.inkrementKollision();
                            return currentHash;
                        } else {
                            hashObject.inkrementKollision();
                        }
                    }
                }
//                else {
//                    if (hashObject.getValue().equals(hashTable[currentHash].getValue())) {
//                        hashTable[currentHash].inkrementVorkommen();
//                        return Integer.MAX_VALUE;// doppelte Vorkommen erkannt
//                    }
//
//                    hashObject.inkrementKollision();
//                    int nextIndexOfNewElem = doubleHashing(hash, hashObject.getValue(), hashObject.getKollision());
//                    if (hashTable[nextIndexOfNewElem] != null) {
//                        HashObject existingElem = hashTable[hash];
//                        int nextIndexOfExistingElem = doubleHashing(hash, existingElem.getValue(),
//                                existingElem.getKollision() + 1);
//                        if (hashTable[nextIndexOfExistingElem] == null) {
//                            hashTable[nextIndexOfExistingElem] = existingElem;
//                            hashTable[hash] = null;
//                        }
//                    } else {
//                        hashObject.dekrementKollision();
//                        return nextIndexOfNewElem; // ergaenzt .. loest nicht das Problem
//                    }
//                }
            }
        }
        return Integer.MIN_VALUE;
    }

    private int doubleHashing(int hash, String elem, int j) {
        int n = (int) (hash - (j * builtHashValueStrich(elem)));
        n = n < 0 ? Math.floorMod(n, m) : (n % m);

        return n;
    }

    /**
     * Wandelt einen String in ASCII um und gibt den Hashwert zurueck
     * 
     * @param elem
     * @return
     */
    private int builtHashValue(String elem) {
        int result = 0;
        byte[] tmp = elem.getBytes();
        for (int i = 0; i < elem.length(); i++) {
            result = (result * 128 + tmp[i]) % m;
        }
        return result;
    }

    private int builtHashValueStrich(String elem) {
        int result = 0;
        byte[] tmp = elem.getBytes();
        for (int i = 0; i < elem.length(); i++) {
            result = result + (tmp[i] % (m - 2));
        }
        return result + 1;
    }

    @Override
    public void insert(String elem) {
        HashObject hashObject = new HashObject(elem);
        int pos = hashFunction(hashObject);
        if (pos == Integer.MIN_VALUE) {
            System.err.println("Element \"" + elem + "\" konnte nicht einsortiert werden");
        } else if (pos != Integer.MAX_VALUE) {
            hashTable[pos] = hashObject;
        }
        // out();
    }

    @Override
    public int find(String elem) {
        HashObject hashObject = new HashObject(elem);
        int pos = hashFunction(hashObject);
        if(pos == Integer.MAX_VALUE) {
            System.err.println(0);
            return 0;
        } else if(hashTable[pos] == null) {
            System.err.println(-1);
            return -1;
        } else {
            System.err.println(pos);
            System.err.println(hashTable[pos].getValue());
            out();
            return hashTable[pos].getVorkommen();
        }
    }

    @Override
    public void log() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("log.txt", "UTF-8");
            int woerterZaehlen = 0;
            Set<String> set = new HashSet<String>(fileInput);
            Iterator<String> iter = set.iterator();
            while (iter.hasNext()) {
                String elem = iter.next();
                int findResult = this.find(elem);
                writer.write(elem + ": " + findResult + "\n");
                woerterZaehlen += findResult;
            }
            writer.write("\nGesamt: " + woerterZaehlen + "\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    @Override
    public long insertRuntime(String elem) {
        // TODO: laeuft ueber das #create()
        return 0;
    }

    @Override
    public long findRuntime(String elem) {
        long start = System.nanoTime();
        this.find(elem);
        return System.nanoTime() - start;
    }

    private static void out() {
        for (int i = 0; i < m; i++) {
            if (hashTable[i] != null) {
                System.err.print("hashTable[" + i + "] = " + hashTable[i].getValue() + "("
                        + hashTable[i].getVorkommen() + ")" + "   ");
            }
        }
        System.err.println();
    }
    
    public static void main(String[] args) {
        AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
        hash.dateiImport("doc/test.txt", 11);

//        out();

        System.err.println(hash.find("lorem"));
        hash.log();
        System.err.println("fertig");
    }

}
