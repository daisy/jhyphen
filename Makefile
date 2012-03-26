
JNI_PATH = /usr/lib/jvm/java-6-openjdk/include

all: libjhyphen.so

jhyphen_wrap.c: jhyphen.i jhyphen.c
	swig -java $<

%.o: %.c
	gcc -c $< -I/usr/local/include -I$(JNI_PATH) -fPIC

libjhyphen.so: jhyphen.o jhyphen_wrap.o
	gcc -shared $^ -o $@

clean:
	rm -rf jhyphen_wrap.c jhyphen.o jhyphen_wrap.o jhyphen.java jhyphenJNI.java