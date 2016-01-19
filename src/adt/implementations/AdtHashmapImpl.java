package adt.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import adt.interfaces.AdtHashmap;

public class AdtHashmapImpl implements AdtHashmap {

    private static List<String> fileInput;
    private static String[] hashTable;
    private static String STRATEGY;
    private static String FILENAME;
    private static int prime;

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
        prime = berechnePrimZahl();
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

        } else if (STRATEGY.equals("B")) {

        }
        return -1;
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

    @Override
    public void insert(String elem) {
        int pos = sondieren(elem);
        hashTable[pos] = elem;
    }

    @Override
    public int find(String elem) {
        // TODO Auto-generated method stub
        return 0;
    }

    private int berechnePrimZahl() {
        int n = fileInput.size();
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
    
    public static void main(String[] args) {
        AdtHashmap hash = AdtHashmapImpl.create("L", "doc/test.txt");
        System.err.println(hashTable[0]);
        System.err.println(hashTable[1]);
        System.err.println(hashTable[2]);
        System.err.println(fileInput);
    }
}
