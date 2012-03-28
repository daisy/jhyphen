package ch.sbs.jhyphen;

import ch.sbs.jhyphen.swig.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The primary API entry point
 * @author Bert Frees
 */
public class Hyphenator {
  
	static {
		System.loadLibrary("jhyphen");
	}
    /**
     * The hyphenation dictionary
     */
	private final SWIGTYPE_p_HyphenDict dict;
	
	/**
	 * The encoding of the hyphenation dictionary
	 */
	private final Charset charset;
	
	/**
	 * Default constructor
	 * @param dictPath The path to the hyphenation dictionary file, e.g. /usr/share/hyphen/hyph_de_DE.dic
	 * @param charset The encoding of the dictionary, e.g. ISO-8859-1
	 * @throws FileNotFoundException
	 */
	public Hyphenator(String dictPath, Charset charset) throws FileNotFoundException {
		if (!(new File(dictPath)).exists()) {
			throw new FileNotFoundException("Dictionary file at " + dictPath + " doesn't exist.");
		}
		if (charset == null) {
			throw new NullPointerException();
		}
		dict = JHyphen.getDictionary(dictPath);
		this.charset = charset;
	}
  
	/**
	 * Returns all possible hyphenation points of a string
	 * @param text The string to be hyphenated
	 * @return An array of booleans which represents the hyphenation points.
	 * The hyphen array has the same length as the input string, and a
	 * hyphen at index i corresponds to characters i and i+1 of the string.
	 */
	public boolean[] hyphenate(String text) {
		
		//TODO what if word already contains hard of soft hyphens?
		
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
			
			// TODO assert that word.length() = wordHyphens.length()
			// TODO assert that last element of wordHyphens is not a hyphen
			
			hyphenBuffer.append(wordHyphens);
			pos = end;
		}
		
		while(pos++ < text.length()) {
			hyphenBuffer.append('0');
		}
		
		// TODO assert that hyphenBuffer.length() == text.length();
		
		boolean[] hyphens = new boolean[text.length()];
		CharacterIterator iter = new StringCharacterIterator(hyphenBuffer.toString());
		int i = 0;
		for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			hyphens[i++] = (c & 1) > 0;
		}
		return hyphens;
	}

	/**
	 * Encodes an input string into a byte array using the same encoding as the hyphenation dictionary.
	 * A dummy character "?" (0x3F in case of ISO-8859-1) is inserted when a character can not be encoded.
	 * @param str The string to be encoded
	 * @return A byte array
	 */
	private byte[] encode(String str) {
		return charset.encode(str).array();
	}
}
