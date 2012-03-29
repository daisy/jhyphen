package ch.sbs.jhyphen;

import java.io.FileNotFoundException;
import java.lang.StringBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Main {

   private static final String text = " Steamboat  supercalifragilisticexpialidocious. eighteen";
   //private static final String text = "stoomboot, omaatje, reëel";
   //private static final String text = "Dampfschiff, Zucker, Schiffahrt";

   public static void main(String argv[]) throws FileNotFoundException {
     
	 Hyphenator hyphenator = new Hyphenator("en_US");
	   
	 boolean[] hyphens = hyphenator.hyphenate(text);

     StringBuffer hyphenatedWord = new StringBuffer();
     int i = 0;
     CharacterIterator iter = new StringCharacterIterator(text);
     for(char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
       hyphenatedWord.append(c);
       if (i<hyphens.length && hyphens[i++]) {
           hyphenatedWord.append('-');
       }
     }
     
     System.out.println(hyphenatedWord.toString());
     
     hyphenator.close();
     
   }
 }
