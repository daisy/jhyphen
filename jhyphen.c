#include <hyphen.h>

#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <ctype.h>

#define BUFSIZE 1000

static char* hyphens[BUFSIZE];

// word should be in lower case (really?) and can not have trailing (or leading?) periods
char* getHyphens(HyphenDict* dict, const char word[], int len) {

    // TODO dynamically allocate memory
    //  (currently this causes memory leak, even though free() is called in jhyphen_wrap)
    //char* hyphens = (char*)malloc(len * sizeof(char));
    char** rep = NULL;
    int* pos = NULL;
    int* cut = NULL;

    hnj_hyphen_hyphenate2(dict, word, len, hyphens, NULL, &rep, &pos, &cut);

    // TODO: if (rep[i]): hyphenation mark at hyphens[i] is not valid because replacements have to be done 
    // int i;
    // int j;
    // if (rep) {
    //     for (i = 0; i < len; i++) {
    //         if (rep[i]) free(rep[i]);
    //     }
    //     free(rep);
    //     free(pos);
    //     free(cut);
    // }

    return hyphens;

}
