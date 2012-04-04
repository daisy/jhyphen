#include <hyphen.h>

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

/* I guess it's a bit presumtuous to assume that words are never
   larger than 1024 chars. But since we are planig to move to jna
   anyway (and hence dumping this stuff), this is good for now. */
#define BUFSIZE 1024

static char* hyphens[BUFSIZE];

// word should be in lower case (really?) and can not have trailing (or leading?) periods
char* getHyphens(HyphenDict* dict, const char word[], int len) {

    char** rep = NULL;
    int* pos = NULL;
    int* cut = NULL;

    hnj_hyphen_hyphenate2(dict, word, len, hyphens, NULL, &rep, &pos, &cut);

    return hyphens;
}
