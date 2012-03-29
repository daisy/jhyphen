
JNI_PATH = /usr/lib/jvm/java-6-openjdk/include

all: libjhyphen.so

jhyphen_wrap.c: jhyphen.i jhyphen.c
	swig -java -package ch.sbs.jhyphen.swig -outdir java/ch/sbs/jhyphen/swig $<

%.o: %.c
	gcc -c $< -I$(JNI_PATH) -fPIC

libjhyphen.so: jhyphen.o jhyphen_wrap.o
	gcc -shared -o $@ $^ -lhyphen

jar: 
	ant jar

%.class: %.java jar
	javac -classpath dist/jhyphen.jar $<

check: jar test/ch/sbs/jhyphen/Main.class
	java -cp test:dist/jhyphen.jar -Djava.library.path=. ch.sbs.jhyphen.Main

clean:
	rm -rf java/ch/sbs/jhyphen/swig/*.java test/ch/sbs/jhyphen/Main.class jhyphen_wrap.c jhyphen.o jhyphen_wrap.o libjhyphen.so
	ant clean
