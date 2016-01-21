package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import adt.implementations.AdtHashmapImpl;
import adt.interfaces.AdtHashmap;

public class AdtHashmapTests {
	
	@Test
	public void testFindWordKurzL() {
	    AdtHashmap hash = AdtHashmapImpl.create("L", 10);
	    hash.dataImport("doc/kurz.txt", 101);
	    
	    assertEquals(4, hash.find("diam"));
	}
	
	@Test
    public void testFindWordNegativeKurzL() {
		AdtHashmap hash = AdtHashmapImpl.create("L", 10);
	    hash.dataImport("doc/kurz.txt", 101);
        
	    assertEquals(-1, hash.find("nichtenthalten"));
    }
	
	@Test
    public void testFindWordKurzQ() {
		AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
	    hash.dataImport("doc/kurz.txt", 101);
        
        assertEquals(4, hash.find("diam"));
    }
    
    @Test
    public void testFindWordNegativeKurzQ() {
    	AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
	    hash.dataImport("doc/kurz.txt", 101);
        
        assertEquals(-1, hash.find("nichtenthalten"));
    }
    
    @Test
    public void testFindWordKurzB() {
    	AdtHashmap hash = AdtHashmapImpl.create("B", 10);
	    hash.dataImport("doc/kurz.txt", 101);
        
        assertEquals(4, hash.find("diam"));
    }
    
    @Test
    public void testFindWordNegativeKurzB() {
    	AdtHashmap hash = AdtHashmapImpl.create("B", 10);
	    hash.dataImport("doc/kurz.txt", 101);
        
	    assertEquals(-1, hash.find("nichtenthalten"));
    }
    
    @Test
    public void testFindWordLangL() {
    	AdtHashmap hash = AdtHashmapImpl.create("L", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangL() {
    	AdtHashmap hash = AdtHashmapImpl.create("L", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
	    assertEquals(-1, hash.find("nichtenthalten"));
    }
    
    @Test
    public void testFindWordLangQ() {
    	AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangQ() {
    	AdtHashmap hash = AdtHashmapImpl.create("Q", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
	    assertEquals(-1, hash.find("nichtenthalten"));
    }
    
    @Test
    public void testFindWordLangB() {
    	AdtHashmap hash = AdtHashmapImpl.create("B", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangB() {
    	AdtHashmap hash = AdtHashmapImpl.create("B", 10);
	    hash.dataImport("doc/lang.txt", 13453);
        
	    assertEquals(-1, hash.find("nichtenthalten"));
    }
}
