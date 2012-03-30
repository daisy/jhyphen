package ch.sbs.jhyphen;

import java.io.IOException;

public class Main {

   //private static final String text = " Steamboat  supercalifragilisticexpialidocious. eighteen";
   //private static final String text = "stoomboot, omaatje, reëel";
   private static final String text = "Dampfschiff, Zucker, Schiffahrt";

   public static void main(String argv[]) throws IOException {
     
	 Hyphenator hyphenator = new Hyphenator("de");
	   
	 String hyphenatedWord = hyphenator.hyphenate(text, '-');
     
     System.out.println(hyphenatedWord.toString());
     
     hyphenator.close();
     
   }
 }
