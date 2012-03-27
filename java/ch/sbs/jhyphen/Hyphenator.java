package ch.sbs.jhyphen;

import ch.sbs.jhyphen.swig.*;
import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hyphenator {
  
	static {
		System.loadLibrary("jhyphen");
	}

	private final SWIGTYPE_p_HyphenDict dict;
	private final Charset charset;

	// dictFile: e.g. /usr/share/hyphen/hyph_de_DE.dic
	// charset: encoding of the dictFile: e.g. ISO-8859-1
	public Hyphenator(String dictFile, Charset charset) {
		dict = JHyphen.getDictionary(dictFile);
		this.charset = charset;
	}
  
	// returns boolean[] with as many elements as there are characters in the String
	public boolean[] hyphenate(String text) {
		
		//TODO: what if word contains soft-hyphen?
		
		Matcher matcher = Pattern.compile("\\p{L}+").matcher(text);
		
		StringBuffer hyphenBuffer = new StringBuffer();
		
		int pos = 0;
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			while(pos++ < start) {
				hyphenBuffer.append('0');
			}
			String word = text.substring(start, end);
			String wordHyphens = JHyphen.getHyphens(dict, encode(word));
			
			// TODO: assert that word.length() = wordHyphens.length()
			// TODO: assert that last element of wordHyphens is not a hyphen
			
			hyphenBuffer.append(wordHyphens);
			pos = end;
		}
		
		while(pos++ < text.length()) {
			hyphenBuffer.append('0');
		}
		
		// TODO: assert that hyphenBuffer.length() == text.length();
		
		boolean[] hyphens = new boolean[text.length()];
		CharacterIterator iter = new StringCharacterIterator(hyphenBuffer.toString());
		int i = 0;
		for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			hyphens[i++] = (c & 1) > 0;
		}
		return hyphens;
	}

	// \0x3F or "?" (in case of ISO-8859-1) is inserted when character can not be encoded 
	private byte[] encode(String str) {
		return charset.encode(str).array();
	}
}
