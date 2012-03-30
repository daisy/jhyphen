AC_CONFIG_MACRO_DIR([m4])

AC_INIT([jhyphen], [0.1], [bert.frees@sbs.org])
AM_INIT_AUTOMAKE([-Wall -Werror foreign])
AC_PROG_CC
AC_CONFIG_HEADERS([config.h])

AC_FUNC_MALLOC
AC_PROG_CXX
AC_PROG_LIBTOOL

AC_CHECK_PROG([SWIG], [swig], [swig])

# make sure the hyphens library is around
AC_CHECK_LIB([hyphen],[hnj_hyphen_hyphenate2])

AC_CONFIG_FILES([
 Makefile
])
AC_OUTPUT
