package ch.sbs.jhyphen;

import ch.sbs.jhyphen.swig.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The primary API entry point
 * @author Bert Frees
 */
public class Hyphenator {

	/**
	 * Maps locales to dictionary files
	 */
	private final static Properties dictionaryPaths = new Properties();
	
	/**
	 * Maps dictionary files to charsets
	 */
	private final static Map<File,Charset> charsets = new HashMap<File,Charset>();
	
	static {
		System.loadLibrary("jhyphen");
		try {
			InputStream stream = ClassLoader.getSystemResourceAsStream("ch/sbs/jhyphen/dictionaries.properties");
	        if (stream != null) {	        	
	        	dictionaryPaths.load(stream);
		        stream.close();
	        }
		} catch (IOException e) {
		}
	}
    /**
     * The hyphenation dictionary
     */
	private final SWIGTYPE_p_HyphenDict dictionary;
	
	/**
	 * The encoding of the hyphenation dictionary, e.g. ISO-8859-1 for German
	 */
	private final Charset charset;
	
	/**
	 * Default constructor
	 * @param dictPath The path to the hyphenation dictionary file,
	 * 		e.g. /usr/share/hyphen/hyph_de_DE.dic
	 * @throws IOException when the dictionary file is not found.
	 * @throws UnsupportedCharsetException
	 */
	public Hyphenator(File dictionaryFile)
			throws IOException, UnsupportedCharsetException {
		
		if (!dictionaryFile.exists()) {
			throw new FileNotFoundException("Dictionary file at " + 
					dictionaryFile.getAbsolutePath() + " doesn't exist.");
		}
		charset = getCharset(dictionaryFile);
		dictionary = JHyphen.hnj_hyphen_load(dictionaryFile.getAbsolutePath());
	}
	
	/**
	 * Constructor which looks up the correct dictionary file based on the given locale
	 * @param locale The locale
	 * @throws IOException when no dictionary file found for the locale.
	 * @throws UnsupportedCharsetException
	 */
	public Hyphenator(String locale)
			throws IOException, UnsupportedCharsetException {
		
		String dictionaryPath = dictionaryPaths.getProperty(locale);
		if (dictionaryPath == null && locale.contains("-")) {
			dictionaryPath = dictionaryPaths.getProperty(locale.substring(0, locale.indexOf("-")));
		}
		if (dictionaryPath == null && locale.contains("_")) {
			dictionaryPath = dictionaryPaths.getProperty(locale.substring(0, locale.indexOf("_")));
		}
		if (dictionaryPath == null) {
			throw new FileNotFoundException("No dictionary file found for locale " + locale);
		}
		File dictionaryFile = new File(dictionaryPath);
		if (!dictionaryFile.exists()) {
			throw new FileNotFoundException("Dictionary file at " + 
					dictionaryFile.getAbsolutePath() + " doesn't exist.");
		}
		charset = getCharset(dictionaryFile);
		dictionary = JHyphen.hnj_hyphen_load(dictionaryFile.getAbsolutePath());
	}

	/**
	 * Returns the fully hyphenated string.
	 * The given hyphen is inserted at all possible hyphenation points.
	 * @param text The string to be hyphenated
	 * @param hyphen The character to be used as hyphenation mark
	 * @return
	 */
	public String hyphenate(String text, char hyphen) {
		boolean[] hyphens = hyphenate(text);
		StringBuffer hyphenatedText = new StringBuffer();
		int i;
		for (i=0; i<hyphens.length; i++) {
			hyphenatedText.append(text.charAt(i));
			if (hyphens[i]) {
				hyphenatedText.append(hyphen);
			}
		}
		hyphenatedText.append(text.charAt(i));
		return hyphenatedText.toString();
	}
	
	/**
	 * Returns all possible hyphenation points of a string
	 * @param text The string to be hyphenated
	 * @return An array of booleans which represents the hyphenation points.
	 * 		The length of the hyphen array is the length of the input string minus 1.
	 * 		A hyphen at index i corresponds to characters i and i+1 of the string.
	 */
	public boolean[] hyphenate(String text) {

		//TODO what if word already contains soft hyphens?
		
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
			String wordHyphens = JHyphen.getHyphens(dictionary, encode(word));
			
			// TODO assert that last element of wordHyphens is not a hyphen

			hyphenBuffer.append(wordHyphens.substring(0, word.length()));
			pos = end;
		}
		
		while(pos < text.length()) {
			hyphenBuffer.append('0');
			pos++;
		}

		hyphenBuffer.deleteCharAt(pos-1);
		
		boolean[] hyphens = new boolean[hyphenBuffer.length()];		
		CharacterIterator iter = new StringCharacterIterator(hyphenBuffer.toString());		
		int i = 0;
		for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
			hyphens[i++] = (c & 1) > 0;
		}
		return hyphens;
	}
	
	/**
	 * Free memory
	 */
	public void close() {
		JHyphen.hnj_hyphen_free(dictionary);
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

	/**
	 * Reads the first line of the dictionary file which is the encoding
	 * @param dictionaryFile The dictionary file
	 * @return The encoding
	 * @throws FileNotFoundException
	 * @throws UnsupportedCharsetException
	 */
	private static Charset getCharset(File dictionaryFile)
			throws UnsupportedCharsetException, IOException {
		
		Charset cs = charsets.get(dictionaryFile);
		if (cs == null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(dictionaryFile));
            	String charsetName = reader.readLine();
                charsetName = charsetName.replaceAll("\\s+", "");
    			cs = Charset.forName(charsetName);
    			charsets.put(dictionaryFile, cs);
			} finally {
	            if (reader != null) {
	            	reader.close();
	            }
            }
		}
		return cs;
	}
}
