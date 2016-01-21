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
	
	private static List<String> fileInput;	// Strings aus der geladenene Textdatei
	private static HashObject[] hashTable;	// interne Struktur der AdtHashmap
	private static String strategie;	// die gewaehlte Sondierungsstrategie
	private String filename;	// die angegebene Datei
	
	private static int m; // Array- Groesse
	
	/**
	 * privater Konstruktor
	 * initialisiert das Array
	 */
	private AdtHashmapImpl() {
		hashTable = new HashObject[m];
	}
	
	/**
	 * Erzeugt eine neue ADT
	 * @param strategy, die Sondierungsstrategie (L=Linear, Q=Quadratisch, B=DoubleHashing nach Brent)
	 * @param size, die Array-Groesse -> m sollte eine Primzahl sein ((m - 3) / 4)
	 * @return neue ADT
	 */
	public static AdtHashmap create(String strategy, int size) {
		strategie = strategy;
		m = size;
		
		return new AdtHashmapImpl();
	}
	
	/**
	 * Datei importieren und in die ADT einfuegen
	 * @param size , sollte Primzahl sein und  >= Anzahl Woerter in Datei
	 * @param filename, die zu ladene Datei
	 */
	@Override
	public void dataImport(String filename, int size) {
		this.filename = filename;
		m = size;
		fileInput = new ArrayList<String>();
		new AdtHashmapImpl();
		readFile();
	}
	
	/**
	 * Fuehrt einen Sondierungsschritt, entsprechend der Strategie, aus
	 * MAX_VALUE => Element bereits in ADT vorhanden
	 * MIN_VALUE =>	ungueltige Strategie angegeben
	 * @param hashObject, ein HashObject, welches den Value, Kollisionen und Vorkommen haelt
	 * @param hash, der "Basis"-Hashwert
	 * @return pos, ein Schritt weiter
	 */
	private int hashFunction(HashObject hashObject, int hash) {
		if(strategie.equals("L")) {
			return linearNext(hash);
		} else if(strategie.equals("Q")) {
			return quadraticNext(hashObject, hash);
		} else if(strategie.equals("B")) {
			HashObject existingElem = hashTable[hash];
			int nextIndexOfNewElem = doubleHashing(hashObject, hash, hashObject.getKollision());
			int nextIndexOfExistingElem = doubleHashing(existingElem, hash, existingElem.getKollision() + 1);
			if(hashObject.getValue().equals(existingElem.getValue())) {
				hashTable[hash].inkrementVorkommen();
				return Integer.MAX_VALUE;
			} else if(hashTable[nextIndexOfNewElem] == null) {
				return nextIndexOfNewElem;
			} else if(hashObject.getValue().equals(hashTable[nextIndexOfNewElem].getValue())) {
				hashTable[nextIndexOfNewElem].inkrementVorkommen();
				return Integer.MAX_VALUE;
			} else if(hashTable[nextIndexOfExistingElem] == null) {
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
	 * Fuegt ein Element in die ADT
	 * Elemente werden nicht doppelt eingefuegt
	 * @param elem, das einzugfuegene Element
	 */
	@Override
	public void insert(String elem) {
		HashObject hashObject = new HashObject(elem);
		int hash = builtHashValue(hashObject.getValue());
		if(hashTable[hash] == null) {
			hashTable[hash] = hashObject;
		} else {
			if(hashTable[hash].getValue().equals(hashObject.getValue())) {
				hashTable[hash].inkrementVorkommen();
			} else {
				for(int i = 0; i < m; i++) {
					hashObject.inkrementKollision();
					int pos = hashFunction(hashObject, hash);
					if(pos != Integer.MAX_VALUE) {
						hash = pos;
						if(hashTable[hash] == null) {
							hashTable[hash] = hashObject;
							break;
						} else if(hashTable[hash].getValue().equals(hashObject.getValue())) {
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
	
	/**
	 * Sucht nach einem Element und gibt das Vorkommen innerhalb der ADT zurueck
	 * Wenn nicht enthalten dann -1
	 * @param elem, das zu suchende Element
	 * @return vorkommen, innerhalb der ADT
	 */
	@Override
	public int find(String elem) {
		HashObject hashObject = new HashObject(elem);
		int hash = builtHashValue(hashObject.getValue());
		int pos = -1;
		if(hashTable[hash].getValue().equals(hashObject.getValue())) {
			return hashTable[hash].getVorkommen();
		}
		if(strategie.equals("B")) {
			for(int i = 1; i <= m * 2; i++) {
				pos = doubleHashing(hashObject, hash, i);
				if(hashTable[pos].getValue().equals(hashObject.getValue())) {
					return hashTable[pos].getVorkommen();
				} else {
					hash = pos;
				}
			}
		} else {
			for(int i = 1; i <= m * 2; i++) {
				pos = hashFunction(hashObject, i);
				if(hashTable[pos] != null) {
					if(hashTable[pos].getValue().equals(hashObject.getValue())) {
						return hashTable[pos].getVorkommen();
					} else {
						hash = pos;
					}
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Erzeugt eine log-Datei mit den Vorkommen aller Woerter
	 */
	@Override
	public void log() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("log.txt", "UTF-8");
			int woerterZaehlen = 0;
			Set<String> set = new HashSet<String>(fileInput);
			Iterator<String> iter = set.iterator();
			while(iter.hasNext()) {
				String elem = iter.next();
				int findResult = this.find(elem);
				writer.write(elem + ": " + findResult + "\n");
				woerterZaehlen += findResult;
			}
			writer.write("\nGesamt: " + woerterZaehlen + "\n");
			System.err.println("Gesamt: " + woerterZaehlen);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}
	
	/**
	 * Misst die Laufzeit (in ns) zum Einfuegen eines Elements
	 * @param laufzeit, in ns
	 */
	@Override
	public long insertRuntime(String elem) {
		long start = System.nanoTime();
		this.insert(elem);
		return System.nanoTime() - start;
	}
	
	/**
	 * Misst die Laufzeit (in ns) zum Finden eines Elements
	 * @param laufzeit, in ns
	 */
	@Override
	public long findRuntime(String elem) {
		long start = System.nanoTime();
		this.find(elem);
		return System.nanoTime() - start;
	}
	
	/**
	 * Consolen-Ausgabe der HashTable mit Position + Value + Vorkommen
	 */
	private static void out() {
		for(int i = 0; i < m; i++) {
			if(hashTable[i] != null) {
				System.err.print("hashTable[" + i + "] = " + hashTable[i].getValue() + "(" + hashTable[i].getVorkommen() + ")" + "   ");
			}
		}
		System.err.println();
	}
	
	/**
	 * Erzeugt aus einen String den ASCII Wert und darauf den Hashwert
	 * @param elem, Element
	 * @return hash, des Elements
	 */
	private int builtHashValue(String elem) {
		int result = 0;
		byte[] tmp = elem.getBytes();
		for(int i = 0; i < elem.length(); i++) {
			result = (result * 128 + tmp[i]) % m;
		}
		return result;
	}
	
	/**
	 * Erzeugt aus einen String den ASCII Wert und darauf den Hashwert
	 * @param elem, Element
	 * @return hash, des Elements
	 */
	private int builtHashValueStrich(String elem) {
		int result = 0;
		byte[] tmp = elem.getBytes();
		for(int i = 0; i < elem.length(); i++) {
			result = result + (tmp[i] % (m - 2));
		}
		return result + 1;
	}
	
	/**
	 * Datei einlesen
	 */
	private void readFile() {
		File file = new File(filename);
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(file));
			while(scanner.hasNext()) {
				String elem = scanner.next();
				elem = elem.replace(",", "");
				elem = elem.replace(".", "");
				fileInput.add(elem.toLowerCase());
			}
		} catch(FileNotFoundException e) {
		} finally {
			scanner.close();
		}
		
		for(String string : fileInput) {
			insert(string);
		}
	}
	
	/**
	 * Lineare Sondierung
	 * Errechnet ausgehend von j die nächste Position
	 * @param j, aktuelle Position
	 * @return pos, neue Position
	 */
	private int linearNext(int j) {
		j--;
		j = j < 0 ? Math.floorMod(j, m) : (j % m);
		return j;
	}
	
	/**
	 * Quadratische Sondierung
	 * Errechnet ausgehend von j die nächste Position
	 * @param hashObject, HashObject
	 * @param j, aktuelle Position
	 * @return pos, neue Position
	 */
	private int quadraticNext(HashObject hashObject, int hash) {
		int j = hashObject.getKollision();
		int n = (int)(hash - (Math.pow(-1, j) * Math.pow(Math.ceil(j / 2.0), 2)));
		n = n < 0 ? Math.floorMod(n, m) : (n % m);
		return n;
	}
	
	/**
	 * DoubleHashing Sondierung
	 * Errechnet ausgehend von j die nächste Position
	 * @param hashObject, HashObject
	 * @param j, aktuelle Position
	 * @return pos, neue Position
	 */
	private int doubleHashing(HashObject hashObject, int hash, int j) {
		int n = (int)(hash - (j * builtHashValueStrich(hashObject.getValue())));
		n = n < 0 ? Math.floorMod(n, m) : (n % m);
		return n;
	}
	
	/**
	 * Main Methode
	 */
	public static void main(String[] args) {
		AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
		hash.dataImport("doc/kurz.txt", 101);
		
		out();
		hash.log();
		System.err.println("fertig");
	}
}
