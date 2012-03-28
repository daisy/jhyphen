#include <hyphen.h>

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define MAX_WORD 512
#define BUFSIZE 1000

// TODO: check for memory leaks
// word should be in lower case and can not have trailing (or leading?) periods
char* getHyphens(HyphenDict* dict, const char word[], int len) {

    char* lcword;
    char* hyphens;
    char* hyphword;
    int i;
    int j;
    char** rep;
    int* pos;
    int* cut;

    hyphens = (char*)malloc(len * sizeof(char));
    hyphword = (char*)malloc(2 * len * sizeof(char));
    
    rep = NULL;
    pos = NULL;
    cut = NULL;
    hyphword[0] = '\0';

    hnj_hyphen_hyphenate2(dict, word, len, hyphens, hyphword, &rep, &pos, &cut);

    free(hyphword);

    // TODO: if (rep[i]): hyphenation mark at hyphens[i] is not valid because replacements have to be done 

    if (rep) {
        for (i = 0; i < len; i++) {
          if (rep[i]) free(rep[i]);
        }
        free(rep);
        free(pos);
        free(cut);
    }

    return hyphens;

}

// TODO: bijkomende binding maken: (hyphens, rep, pos, cut) = getHyphens(dict, word)
//       non-standard hyphenation info (= rep, pos & cut): in Java gebruiken



