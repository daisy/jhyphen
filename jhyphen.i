%module JHyphen
%include "typemaps.i"

%typemap(in)     (char * BYTE, int LENGTH) {
/* Functions from jni.h */
$1 = (char *) JCALL2(GetByteArrayElements, jenv, $input, 0);
$2 = (int)    JCALL1(GetArrayLength,       jenv, $input);
}
%typemap(jni)    (char * BYTE, int LENGTH) "jbyteArray"
%typemap(jtype)  (char * BYTE, int LENGTH) "byte[]"
%typemap(jstype) (char * BYTE, int LENGTH) "byte[]"
%typemap(javain) (char * BYTE, int LENGTH) "$javainput"

%apply (char * BYTE, int LENGTH) { (const char word[], int len) };

%{
#include <hyphen.h>
extern HyphenDict* getDictionary(char* fn);
extern char* getHyphens(HyphenDict* dict, const char word[], int len);
%}

extern HyphenDict* getDictionary(char* fn);
extern char* getHyphens(HyphenDict* dict, const char word[], int len);

