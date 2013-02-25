package ch.sbs.jhyphen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.Pointer;

/**
 * The primary API entry point
 * @author Bert Frees
 */
public class Hyphenator {
	
	private final static HyphenLibrary libhyphen = HyphenLibrary.INSTANCE;
	
	// Don't allocate new memory for each word
	private static ByteBuffer wordHyphens = ByteBuffer.allocate(50);
	
	/**
	 * Maps locales to dictionary files
	 */
	private final static Properties dictionaryPaths = new Properties();
	
	/**
	 * Maps dictionary files to charsets
	 */
	private final static Map<File,Charset> charsets = new HashMap<File,Charset>();
	
	static {
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
	private final Pointer dictionary;
	
	/**
	 * The encoding of the hyphenation dictionary, e.g. ISO-8859-1 for German
	 */
	private final Charset charset;
	
	/**
	 * Default constructor
	 * @param dictPath The path to the hyphenation dictionary file,
	 * 		e.g. /usr/share/hyphen/hyph_de_DE.dic
	 * @throws FileNotFoundException if the dictionary file cannot be found.
	 * @throws UnsupportedCharsetException if the encoding of the file is not supported.
	 */
	public Hyphenator(File dictionaryFile) throws UnsupportedCharsetException, FileNotFoundException {
		
		if (!dictionaryFile.exists()) {
			throw new FileNotFoundException("Dictionary file at " + 
					dictionaryFile.getAbsolutePath() + " doesn't exist.");
		}
		charset = getCharset(dictionaryFile);
		dictionary = libhyphen.hnj_hyphen_load(dictionaryFile.getAbsolutePath());
	}
	
	/**
	 * Constructor which looks up the correct dictionary file based on the given locale
	 * @param locale The locale
	 * @throws FileNotFoundException if no dictionary file can be found for the locale.
	 * @throws UnsupportedCharsetException if the encoding of the file is not supported.
	 */
	public Hyphenator(String locale) throws UnsupportedCharsetException, FileNotFoundException {
		
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
		dictionary = libhyphen.hnj_hyphen_load(dictionaryFile.getAbsolutePath());
	}

	/**
	 * Returns the fully hyphenated string.
	 * The given hyphen is inserted at all possible hyphenation points.
	 * @param text The string to be hyphenated
	 * @param hyphen The character to be used as hyphenation mark
	 * @return The hyphenated string
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
			// libhyphen requires that word is lowercase
			word = word.toLowerCase();
			byte[] wordBytes = encode(word);
			int wordSize = wordBytes.length;
			if (wordSize > wordHyphens.capacity()) {
				wordHyphens = ByteBuffer.allocate(wordSize * 2);
			}
			
			libhyphen.hnj_hyphen_hyphenate(dictionary, wordBytes, wordSize, wordHyphens);
			
			// TODO assert that last element of wordHyphens is not a hyphen

			hyphenBuffer.append(new String(wordHyphens.array(), 0, word.length()));
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
		
		// Add hyphen points after hard hyphens ("-" followed and preceded by a letter or number)
		matcher = Pattern.compile("[\\p{L}\\p{N}]-(?=[\\p{L}\\p{N}])").matcher(text);
		while (matcher.find()) {
			hyphens[matcher.start()+1] = true;
		}
		
		return hyphens;
	}
	
	/**
	 * Free memory
	 */
	public void close() {
		libhyphen.hnj_hyphen_free(dictionary);
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
	 * @throws FileNotFoundException if the dictionary file cannot be found.
	 * @throws UnsupportedCharsetException if the encoding of the file is not supported.
	 */
	private static Charset getCharset(File dictionaryFile)
			throws UnsupportedCharsetException, FileNotFoundException {
		
		Charset cs = charsets.get(dictionaryFile);
		if (cs == null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(dictionaryFile));
            	String charsetName = reader.readLine();
                charsetName = charsetName.replaceAll("\\s+", "");
    			cs = Charset.forName(charsetName);
    			charsets.put(dictionaryFile, cs);
			} catch (IOException e) {
				throw new RuntimeException("Could not read first line of file");
			} finally {
	            if (reader != null) {
	            	try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
            }
		}
		return cs;
	}
}
