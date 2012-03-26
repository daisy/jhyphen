# TODO:
#  - nieuwe c-file maken met functie die hnj_hyphen_load en hnj_hyphen_hyphenate combineert
#  - enkel nieuwe .so maken van nieuwe c-file + hyphen_wrap.c
#  - en linken naar bestaande libhyphen.so


make clean
./configure
make
sudo make install

swig -java jhyphen.i

gcc -c jhyphen.c jhyphen_wrap.c /usr/local/lib/libhyphen.so \
    -I/usr/local/include \
    -I/usr/lib/jvm/java-6-openjdk/include \
    -fPIC

gcc -shared jhyphen.o jhyphen_wrap.o \
    -o libjhyphen.so


# Test:

javac Main.java
java -Djava.library.path=. Main
