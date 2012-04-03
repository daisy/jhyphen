ACLOCAL_AMFLAGS = -I m4

SUBDIRS = java
EXTRA_DIST = build.xml

AM_CPPFLAGS = -I /usr/lib/jvm/java-6-openjdk/include

lib_LTLIBRARIES = libjhyphen.la
libjhyphen_la_SOURCES = jhyphen.c jhyphen_wrap.c jhyphen.i
libjhyphen_la_LDFLAGS = -version-info 0:1

jhyphen_wrap.c: jhyphen.i jhyphen.c
	$(SWIG) -java -package ch.sbs.jhyphen.swig -outdir java/ch/sbs/jhyphen/swig $<

CLEANFILES = jhyphen_wrap.c

.PHONY : jar
jar:
	ant -f ${srcdir}/build.xml -Dbasedir=$(builddir) -Dversion=$(VERSION)

clean-local:
	ant -f ${srcdir}/build.xml -Dbasedir=$(builddir) clean
