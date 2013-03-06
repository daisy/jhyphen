package ch.sbs.jhyphen;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Hyphen {
	
	private static File libraryPath = null;
	
	public static void setLibraryPath(File path) {
		libraryPath = path;
	}
	
	private static HyphenLibrary instance;
	
	public static HyphenLibrary getLibrary() {
		if (instance == null) {
			try {
				String name = (libraryPath != null) ? libraryPath.getCanonicalPath() : "hyphen";
				instance = (HyphenLibrary)Native.loadLibrary(name, HyphenLibrary.class); }
			catch (IOException e) {
				throw new RuntimeException("Could not load libhyphen", e); }}
		return instance;
	}
	
	public interface HyphenLibrary extends Library {
		
		/**
		 * Load a hyphenation dictionary
		 * 
		 * @param fn The path to the hyphenation dictionary
		 * @return Pointer to the HyphenDict
		 */
		public Pointer hnj_hyphen_load(String fn);
		
		/**
		 * Free memory
		 * @param dict A pointer to a HyphenDict object
		 */
		public void hnj_hyphen_free(Pointer dict);
		
		/**
		 * Standard hyphenation
		 * 
		 * @param dict A pointer to a HyphenDict object
		 *             Before using this function, a call to write() should be done when the HyphenDict has changed.
		 * @param word A byte array, which is the result of encoding the unhyphenated word.
		 *             The same encoding as the hyphenation table should be used.
		 * @param word_size The length of the byte array
		 * @param hyphens The return value: the hyphen points.
		 *                A minimum of 'word_size' bytes must be allocated in memory.
		 *                Only the first n bytes are useful, were n is the length the unhyphenated word.
		 *                Each byte is a natural number encoded in ASCII. An odd number means a hyphen point.
		 * @return 0
		 */
		public int hnj_hyphen_hyphenate(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens);
		
		/**
		 * Non-standard hyphenation
		 */
		public int hnj_hyphen_hyphenate2(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens,
				ByteBuffer hyphenated_word, PointerByReference rep, PointerByReference pos,
				PointerByReference cut);
		
		public int hnj_hyphen_hyphenate3(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens,
				ByteBuffer hyphword, PointerByReference rep, PointerByReference pos,
				PointerByReference cut, int lhmin, int rhmin, int clhmin, int crhmin);
		
	}
}
