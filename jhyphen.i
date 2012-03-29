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
%apply int * OUTPUT { int* hyphens };

%{
#include <hyphen.h>
extern HyphenDict* hnj_hyphen_load (const char* fn);
extern void hnj_hyphen_free (HyphenDict* dict);
extern char* getHyphens(HyphenDict* dict, const char word[], int len);
%}

extern HyphenDict* hnj_hyphen_load (const char* fn);
extern void hnj_hyphen_free (HyphenDict* dict);
extern char* getHyphens(HyphenDict* dict, const char word[], int len);


