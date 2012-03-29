ACLOCAL_AMFLAGS = -I m4

AM_CPPFLAGS = -I /usr/lib/jvm/java-6-openjdk/include

lib_LTLIBRARIES = libjhyphen.la
libjhyphen_la_SOURCES = jhyphen.c jhyphen_wrap.c

jhyphen_wrap.c: jhyphen.i jhyphen.c
	$(SWIG) -java -package ch.sbs.jhyphen.swig -outdir java/ch/sbs/jhyphen/swig $<

CLEANFILES = jhyphen_wrap.c