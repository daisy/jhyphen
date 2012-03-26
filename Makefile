
JNI_PATH = /usr/lib/jvm/java-6-openjdk/include

all: libjhyphen.so

jhyphen_wrap.c: jhyphen.i jhyphen.c
	swig -java $<

%.o: %.c
	gcc -c $< -I$(JNI_PATH) -fPIC

libjhyphen.so: jhyphen.o jhyphen_wrap.o
	gcc -shared -o $@ $^ -lhyphen

%.class: %.java
	javac $<

check: Main.class
	java -Djava.library.path=. Main

clean:
	rm -rf jhyphen_wrap.c jhyphen.o jhyphen_wrap.o jhyphen.java jhyphenJNI.java Main.class libjhyphen.so *.class
