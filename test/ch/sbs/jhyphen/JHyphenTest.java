package ch.sbs.jhyphen;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
		
		Map<String,String> words = new HashMap<String,String>();
		
		words.put("bla-bla", "bla-=bla");
		words.put("bla-", "bla-");
		words.put("-bla", "-bla");
		words.put(":-)", ":-)");
		words.put("3-jährig", "3-=jäh=rig");
		words.put("3-für-2-Aktion", "3-=für-=2-=Ak=ti=on");
		words.put("von 14-16 Uhr", "von 14-=16 Uhr");
		
		for (Entry<String,String> entry : words.entrySet()) {
			assertEquals(entry.getValue(), hyphenator.hyphenate(entry.getKey(), '='));
		}
		
		hyphenator.close();
		
	}
	
	@Test
	public void testExceptionWords() throws UnsupportedCharsetException, FileNotFoundException {
		
		Hyphenator hyphenator = new Hyphenator("de");
		
		Map<String,String> words = new HashMap<String,String>();
		words.put("angestarrt", "an=ge=starrt");
		
		for (Entry<String,String> entry : words.entrySet()) {
			assertEquals(entry.getValue(), hyphenator.hyphenate(entry.getKey(), '='));
		}
		
		hyphenator.close();
	}
	
	@Test
	public void testCapital() throws UnsupportedCharsetException, FileNotFoundException {
		
		Hyphenator hyphenator = new Hyphenator("de");
		
		Map<String,String> words = new HashMap<String,String>();
		words.put("schlampe", "schlam=pe");
		words.put("Schlampe", "Schlam=pe");
		
		for (Entry<String,String> entry : words.entrySet()) {
			assertEquals(entry.getValue(), hyphenator.hyphenate(entry.getKey(), '='));
		}
		
		hyphenator.close();
	}
}
