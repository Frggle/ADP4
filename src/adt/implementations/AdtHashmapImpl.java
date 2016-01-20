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

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import adt.interfaces.AdtHashmap;

public class AdtHashmapImpl implements AdtHashmap {

    private static List<String> fileInput;
    private static String[] hashTable;
    private static String STRATEGY;
    private static String FILENAME;
    private static int prime;
    private static int primeQ;

    private AdtHashmapImpl() {
        fileInput = new ArrayList<String>();
        readFile();
    }

    public static AdtHashmap create(String strategy, String filename) {
        STRATEGY = strategy;
        FILENAME = filename;

        return new AdtHashmapImpl();
    }

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
        prime = berechnePrimZahl(fileInput.size());
        primeQ = berechnePrimZahlQ(prime + 1);

        hashTable = new String[fileInput.size()];
        for (String string : fileInput) {
            insert(string);
        }
    }

    /**
     * Position 0..(n-1)
     * 
     * @return
     */
    private int sondieren(String elem) {
        int hash = divRest(elem);

        if (STRATEGY.equals("L")) {
            for (int i = hash; i < fileInput.size(); i++) {
                if (hashTable[i] == null) {
                    return i;
                }
            }
            for (int i = 0; i < hash; i++) {
                if (hashTable[i] == null) {
                    return i;
                }
            }
        } else if (STRATEGY.equals("Q")) {
            for (int i = 0; i < fileInput.size() *2; i++) {
                // TODO: warum nur n-Schritte?! -> werden alle Position
                // erreicht?
                int n = (int) (hash - (Math.pow(-1, i) * Math.pow(Math.ceil(i / 2), 2)));
                n = n < 0 ? Math.floorMod(n, primeQ) : (n % primeQ);

                n = n % fileInput.size();
                if (hashTable[n] == null) {
                    return n;
                }
            }
        } else if (STRATEGY.equals("B")) {
            // TODO: Fragen ob richtig umgesetzt?!
            int doubleHashNew = doubleHashing(elem, 0);
            if (hashTable[doubleHashNew % fileInput.size()] == null) {
                return doubleHashNew;
            } else {
                int doubleHashOld = doubleHashNew;
                String oldElem = hashTable[doubleHashOld % fileInput.size()];
                int countOld = 0;
                while (hashTable[doubleHashOld % fileInput.size()] != null) {
                    countOld++;
                    doubleHashOld = doubleHashing(oldElem, countOld);
                }
                String newElem = elem;
                int countNew = 0;
                while (hashTable[doubleHashNew % fileInput.size()] != null) {
                    countNew++;
                    doubleHashNew = doubleHashing(newElem, countNew);
                }
                if (countNew <= countOld) {
                    return doubleHashNew;
                } else {
                    return doubleHashOld;
                }
            }
        }
        return Integer.MIN_VALUE;
    }

    private int doubleHashing(String elem, int j) {
        int hash = divRest(elem);
        int n = (int) (hash - (j * divRest2(elem)));
        n = n < 0 ? Math.floorMod(n, primeQ) : (n % primeQ);

        return n;
    }

    /**
     * Wandelt einen String in ASCII um
     * 
     * @param elem
     * @return
     */
    private int divRest(String elem) {
        int result = 0;
        byte[] tmp = elem.getBytes();
        for (int i = 0; i < elem.length(); i++) {
            result = (result * 128 + tmp[i]) % prime;
        }
        return result;
    }

    private int divRest2(String elem) {
        int result = 0;
        byte[] tmp = elem.getBytes();
        for (int i = 0; i < elem.length(); i++) {
            result = result + (1 + (tmp[i] % (prime - 2)));
        }
        return result;
    }

    @Override
    public void insert(String elem) {
        int sondier = sondieren(elem);
        if (sondier == Integer.MIN_VALUE) {
            System.err.println("Element \"" + elem + "\" konnte nicht einsortiert werden");
        } else {
            int pos = sondier;
            hashTable[pos] = elem;
        }
//        out();
    }

    @Override
    public int find(String elem) {
        int hash = divRest(elem);
        int count = 0;

        if (STRATEGY.equals("L")) {
            for (int i = hash; i < fileInput.size(); i++) {
                if (hashTable[i].equals(elem)) {
                    count++;
                }
            }
            for (int i = 0; i < hash; i++) {
                if (hashTable[i].equals(elem)) {
                    count++;
                }
            }
        } else if (STRATEGY.equals("Q")) {
            for (int i = 0; i < fileInput.size() + 100; i++) {
                // TODO: zaehlt Elemente doppelt!!
                int n = (int) (hash - (Math.pow(-1, i) * Math.pow(Math.ceil(i / 2), 2)));
                n = n < 0 ? Math.floorMod(n, primeQ) : (n % primeQ);

                System.err.println(hashTable.length);
                if (hashTable[n % fileInput.size()].equals(elem)) {
                    count++;
                }
            }
        } else if (STRATEGY.equals("B")) {

        } else {
            return Integer.MIN_VALUE;
        }

        return count;
    }

    private int berechnePrimZahl(int start) {
        int n = start;
        boolean b = false;
        while (!b) {
            // check if n is a multiple of 2
            if (n % 2 == 0) {
                b = false;
                n++;
            }
            // if not, then just check the odds
            for (int i = 3; i * i <= n; i += 2) {
                if (n % i == 0) {
                    b = false;
                    n++;
                }
            }
            b = true;
        }
        return n;
    }

    private int berechnePrimZahlQ(int start) {
        int result = 0;
        boolean b = false;

        while (!b) {
            int prime = berechnePrimZahl(start);
            if (((prime - 3) / 4) % 2 == 0) {
                b = true;
                result = prime;
            }
            start = prime + 1;
        }
        return result;
    }

    public static void main(String[] args) {
        AdtHashmap hash = AdtHashmapImpl.create("L", "doc/lang.txt");

        hash.log();
        System.err.println("fertig");
    }
    
    @Override
    public void log() {
        PrintWriter writer = null;
        try {
             writer = new PrintWriter("log.txt", "UTF-8");
            
            Set<String> set = new HashSet<String>(fileInput);
            Iterator<String> iter = set.iterator();
            while(iter.hasNext()) {
                String elem = iter.next();
                writer.write(elem + ": " + this.find(elem) + "\n");
            }
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
        // TODO: laeubt ueber das #create()
        return 0;
    }
    
    @Override
    public long findRuntime(String elem) {
        long start = System.nanoTime();
        this.find(elem);
        return System.nanoTime() - start;
    }

    private static void out() {
        for (int i = 0; i < hashTable.length; i++) {
            System.err.print("hashTable[" + i + "] = " + hashTable[i] + "   ");
        }
        System.err.println();
    }
}
