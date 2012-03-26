public class Main {
   public static void main(String argv[]) {
     System.loadLibrary("jhyphen");
     System.out.println('jhyphen.doSomething("hyphen.tex", "steamboat") = ' + jhyphen.doSomething("hyphen.tex", "steamboat"));
   }
 }
