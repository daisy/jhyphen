#include <string.h>
#include <hyphen.h>

int doSomething(const char* fn, const char* word) {

  HyphenDict* dict;
  char* lcword;
  char* hyphword;
  int word_size;

  dict = hnj_hyphen_load(fn);

  lcword = tolower(word);
  word_size = strlen(lcword);

  //hnj_hyphen_hyphenate(dict, lcword, word_size, hyphens);


  return 1;
}
