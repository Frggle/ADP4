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

    private int linearNext(int j) {
        j--;
        j = j < 0 ? Math.floorMod(j, m) : (j % m);
        return j;
    }

    private int quadraticNext(HashObject hashObject, int hash) {
        int j = hashObject.getKollision();
        int n = (int) (hash - (Math.pow(-1, j) * Math.pow(Math.ceil(j / 2.0), 2)));
        n = n < 0 ? Math.floorMod(n, m) : (n % m);
        return n;
    }

    private int doubleHashing(HashObject hashObject, int hash, int j) {
        int n = (int) (hash - (j * builtHashValueStrich(hashObject.getValue())));
        n = n < 0 ? Math.floorMod(n, m) : (n % m);

        return n;
    }

    /**
     * Position 0..(n-1)
     * 
     * @param j
     *            , Anzahl der Kollisionen
     * @return
     */
    private int hashFunction(HashObject hashObject, int hash) {
        if (STRATEGY.equals("L")) {
            return linearNext(hash);
        } else if (STRATEGY.equals("Q")) {
            return quadraticNext(hashObject, hash);
        } else if (STRATEGY.equals("B")) {
            HashObject existingElem = hashTable[hash];
            int nextIndexOfNewElem = doubleHashing(hashObject, hash, hashObject.getKollision());
            int nextIndexOfExistingElem = doubleHashing(existingElem, hash, existingElem.getKollision() + 1);
            if (hashObject.getValue().equals(existingElem.getValue())) {
                hashTable[hash].inkrementVorkommen();
                return Integer.MAX_VALUE;
            } else if (hashTable[nextIndexOfNewElem] == null) {
                return nextIndexOfNewElem;
            } else if (hashObject.getValue().equals(hashTable[nextIndexOfNewElem].getValue())) {
                hashTable[nextIndexOfNewElem].inkrementVorkommen();
                return Integer.MAX_VALUE;
            } else if (hashTable[nextIndexOfExistingElem] == null) {
                hashTable[nextIndexOfExistingElem] = existingElem;
                hashTable[hash] = null;
                return hash;
            } else {
                hashObject.inkrementKollision();
                hashFunction(hashObject, nextIndexOfNewElem);
            }
        }
        return Integer.MIN_VALUE;
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
        int hash = builtHashValue(hashObject.getValue());
        if (hashTable[hash] == null) {
            hashTable[hash] = hashObject;
        } else {
            if (hashTable[hash].getValue().equals(hashObject.getValue())) {
                hashTable[hash].inkrementVorkommen();
            } else {
                for (int i = 0; i < m; i++) {
                    hashObject.inkrementKollision();
                    int pos = hashFunction(hashObject, hash);
                    if (pos != Integer.MAX_VALUE) {
                        hash = pos;
                        if (hashTable[hash] == null) {
                            hashTable[hash] = hashObject;
                            break;
                        } else if (hashTable[hash].getValue().equals(hashObject.getValue())) {
                            hashTable[hash].inkrementVorkommen();
                            hashTable[hash].inkrementKollision();
                            break;
                        } else {
                            hashTable[hash].inkrementKollision();
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int find(String elem) {
        HashObject hashObject = new HashObject(elem);
        int hash = builtHashValue(hashObject.getValue());
        int pos = -1;
        if (hashTable[hash].getValue().equals(hashObject.getValue())) {
            return hashTable[hash].getVorkommen();
        }
        if (STRATEGY.equals("B")) {
            for(int i = 1; i <= m * 2; i++) {
                pos = doubleHashing(hashObject, hash, i);
                if (hashTable[pos].getValue().equals(hashObject.getValue())) {
                    return hashTable[pos].getVorkommen();
                } else {
                     hash = pos;
                }
            }
        } else {
            for (int i = 1; i <= m * 2; i++) {
                pos = hashFunction(hashObject, i);
                if(hashTable[pos] != null) {
                    if (hashTable[pos].getValue().equals(hashObject.getValue())) {
                        return hashTable[pos].getVorkommen();
                    } else {
                         hash = pos;
                    }
                }
            }
        }

        return -1;
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
                System.err.println(elem + ": " + findResult);
                if(elem.equals("lorem")) {
                    System.err.println();
                }
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
        hash.dateiImport("doc/kurz.txt", 101);

        out();

        System.err.println(hash.find("sea"));
        hash.log();
        System.err.println("fertig");
    }

}
