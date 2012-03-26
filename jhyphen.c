#include <hyphen.h>

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define MAX_WORD 256

int doSomething(const char* fn, const char* word) {

  HyphenDict* dict;
  char lcword[MAX_WORD];
  char hyphword[MAX_WORD];
  char hyphens[MAX_WORD];
  int word_size;
  int i;
  int j;

  dict = hnj_hyphen_load(fn);

  word_size = strlen(word);

  for (i = 0; i < word_size; ++i) {
    lcword[i] = tolower(word[i]);
  }
  lcword[i] = '\0';
  
  hnj_hyphen_hyphenate(dict, lcword, word_size, hyphens);

  j = 0;
  for (i = 0; i < word_size; i++) {
    hyphword[j++] = word[i];
    if (hyphens[i]&1) {
      hyphword[j++] = '-';
    }
  }
  hyphword[j] = '\0';

  fprintf(stdout,"%s\n", hyphword);
  fflush(stdout);

  return 1;
}
