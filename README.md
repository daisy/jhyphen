[jhyphen][]
===========

jhyphen provides minimal Java bindings to the [hyphenation library from hunspell][hunspell]. 

The bindings are based on [SWIG][], so in theory it should be easy to use them to generate bindings for [Ruby][], [Clisp][] or any of the [languages that SWIG supports][supported languages].

Installation
------------

The usual `./configure && make && make install` will install a shared lib and will produce a jar which you can include in your Java projects.

Authors
-------

+ [Bert Frees](http://github.com/bertfrees)
+ [Christian Egli](http://github.com/egli)

Copyright and license
---------------------

Copyright 2012 [Swiss Library for the Blind, Visually Impaired and Print Disabled][sbs]

Licensed under GNU Lesser General Public License as published by the Free Software Foundation, either [version 3][lgpl] of the License, or (at your option) any later version.

[jhyphen]: http://github.com/sbsdev/jhyphen
[hunspell]: http://hunspell.sourceforge.net/
[swig]: http://www.swig.org/
[ruby]: http://www.ruby-lang.org/en/
[clisp]: http://www.clisp.org/
[supported languages]: http://www.swig.org/compat.html#SupportedLanguages
[sbs]: http://www.sbs.ch
[lgpl]: http://www.gnu.org/licenses/lgpl.html
