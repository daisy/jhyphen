package ch.sbs.jhyphen;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.charset.UnsupportedCharsetException;

import org.junit.Test;

public class JHyphenTest {
	
	@Test
	public void testHyphenation() throws UnsupportedCharsetException, FileNotFoundException {
		
		Hyphenator hyphenator = new Hyphenator("de");
		
		String unhyphenatedWord = "Dampfschiff, Zucker, Schiffahrt";
		String hyphenatedWord = "Dampf=schiff, Zu=cker, Schif=fahrt";
		
        assertEquals(hyphenatedWord, hyphenator.hyphenate(unhyphenatedWord, '='));
		
		hyphenator.close();
		
	}
	
	@Test
	public void testHardHyphen() throws UnsupportedCharsetException, FileNotFoundException {
		
		Hyphenator hyphenator = new Hyphenator("de");
		
		String unhyphenatedWord = "bla-bla, bla-, bla-1, -bla";
		String hyphenatedWord = "bla-=bla, bla-, bla-1, -bla";
		
        assertEquals(hyphenatedWord, hyphenator.hyphenate(unhyphenatedWord, '='));
		
		hyphenator.close();
		
	}
}
