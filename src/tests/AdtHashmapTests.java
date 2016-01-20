package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import adt.implementations.AdtHashmapImpl;
import adt.interfaces.AdtHashmap;

public class AdtHashmapTests {
	
	@Test
	public void testFindWordKurzL() {
	    AdtHashmap hash = AdtHashmapImpl.create("L", "doc/kurz.txt");
	    
	    assertEquals(4, hash.find("diam"));
	}
	
	@Test
    public void testFindWordNegativeKurzL() {
        AdtHashmap hash = AdtHashmapImpl.create("L", "doc/kurz.txt");
        
        assertEquals(0, hash.find(""));
    }
	
	@Test
    public void testFindWordKurzQ() {
        AdtHashmap hash = AdtHashmapImpl.create("Q", "doc/kurz.txt");
        
        assertEquals(4, hash.find("diam"));
    }
    
    @Test
    public void testFindWordNegativeKurzQ() {
        AdtHashmap hash = AdtHashmapImpl.create("Q", "doc/kurz.txt");
        
        assertEquals(0, hash.find(""));
    }
    
    @Test
    public void testFindWordKurzB() {
        AdtHashmap hash = AdtHashmapImpl.create("B", "doc/kurz.txt");
        
        assertEquals(4, hash.find("diam"));
    }
    
    @Test
    public void testFindWordNegativeKurzB() {
        AdtHashmap hash = AdtHashmapImpl.create("B", "doc/kurz.txt");
        
        assertEquals(0, hash.find(""));
    }
    
    @Test
    public void testFindWordLangL() {
        AdtHashmap hash = AdtHashmapImpl.create("L", "doc/lang.txt");
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangL() {
        AdtHashmap hash = AdtHashmapImpl.create("L", "doc/lang.txt");
        
        assertEquals(0, hash.find(""));
    }
    
    @Test
    public void testFindWordLangQ() {
        AdtHashmap hash = AdtHashmapImpl.create("Q", "doc/lang.txt");
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangQ() {
        AdtHashmap hash = AdtHashmapImpl.create("Q", "doc/lang.txt");
        
        assertEquals(0, hash.find(""));
    }
    
    @Test
    public void testFindWordLangB() {
        AdtHashmap hash = AdtHashmapImpl.create("B", "doc/lang.txt");
        
        assertEquals(15, hash.find("option"));
    }
    
    @Test
    public void testFindWordNegativeLangB() {
        AdtHashmap hash = AdtHashmapImpl.create("B", "doc/lang.txt");
        
        assertEquals(0, hash.find(""));
    }
}
