package ch.sbs.jhyphen;

import java.io.FileNotFoundException;
import java.lang.StringBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.nio.charset.Charset;

public class Main {

   private static final String DICT = "/usr/share/hyphen/hyph_en_US.dic";
   //private static final String DICT = "/usr/share/hyphen/hyph_nl_NL.dic";
   //private static final String DICT = "/home/frees/src/hyphen-2.8.3/hyphen.tex";
   //private static final String DICT = "/usr/share/hyphen/hyph_de_DE.dic";

   private static final String text = " Steamboat  supercalifragilisticexpialidocious. eighteen";
   //private static final String[] words = new String[]{"stoomboot", "omaatje", "reëel"};
   //private static final String[] words = new String[]{"Dampfschiff", "Zucker", "Schiffahrt"};

   public static void main(String argv[]) throws FileNotFoundException {
     
	 Hyphenator hyphenator = new Hyphenator(DICT, Charset.forName("ISO-8859-1"));
	   
	 boolean[] hyphens = hyphenator.hyphenate(text);

     StringBuffer hyphenatedWord = new StringBuffer();
     int i = 0;
     CharacterIterator iter = new StringCharacterIterator(text);
     for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
       hyphenatedWord.append(c);
       if (hyphens[i++]) {
           hyphenatedWord.append('-');
       }
     }
     
     System.out.println(hyphenatedWord.toString());
     
     hyphenator.close();
     
   }
 }
