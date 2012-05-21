package ch.sbs.jhyphen;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.nio.ByteBuffer;

public interface HyphenLibrary extends Library {

	public static final String JNA_LIBRARY_NAME = "hyphen";
	public static final NativeLibrary JNA_NATIVE_LIB =
			NativeLibrary.getInstance(HyphenLibrary.JNA_LIBRARY_NAME);
	public static final HyphenLibrary INSTANCE =
			(HyphenLibrary)Native.loadLibrary(HyphenLibrary.JNA_LIBRARY_NAME, HyphenLibrary.class);
	
	/**
	 * Load a hyphenation dictionary
	 * 
	 * @param fn The path to the hyphenation dictionary
	 * @return Pointer to the HyphenDict
	 */
	Pointer hnj_hyphen_load(String fn);
	
	/*
	 * Reading full HyphenDict struct causes crash on production server!
	 * 
	 * @param fn The path to the hyphenation dictionary
	 * @return The HyphenDict object
	 *
	HyphenDict hnj_hyphen_load(String fn); */

	/**
	 * Free memory
	 * @param dict A pointer to a HyphenDict object
	 */
	void hnj_hyphen_free(Pointer dict);
	
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
	int hnj_hyphen_hyphenate(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens);
	
	/**
	 * Non-standard hyphenation
	 */
	int hnj_hyphen_hyphenate2(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens,
			ByteBuffer hyphenated_word, PointerByReference rep, PointerByReference pos,
			PointerByReference cut);
	
	int hnj_hyphen_hyphenate3(Pointer dict, byte[] word, int word_size, ByteBuffer hyphens,
			ByteBuffer hyphword, PointerByReference rep, PointerByReference pos,
			PointerByReference cut, int lhmin, int rhmin, int clhmin, int crhmin);

}
