Nerchuko is a library of [Machine
Learning](http://en.wikipedia.org/wiki/Machine_learning) algorithms
written in [Clojure](http://clojure.org). Nerchuko presently focuses
on Machine Learning for textual data.

Apart from the core Machine Learning algorithms, Nerchuko includes
several helper functions that are useful when working with those
Machine Learning algorithms. For example there are helper functions
for preparing datasets, [Feature
Selection](http://en.wikipedia.org/wiki/Feature_selection),
[Cross-validation](http://en.wikipedia.org/wiki/Cross-validation_%28statistics%29)
etc.

[API Documentation](http://sids.github.com/nerchuko)

## Getting Started

Please note that Nerchuko is under active development. There may be
bugs and the API may change without notice.

### Use with *leiningen* or *maven*
Nerchujo is hosted on [Clojars](http://clojars.org/). You can find the
instructions for adding it as a dependency to your projects here:
[http://clojars.org/nerchuko](http://clojars.org/nerchuko Nerchuko on
Clojars).

### Use with *ant*, etc.
Simply add the Nerchuko jar along with the jars of all the
dependencies to your classpath and you are good to go. See below for
instructions on building the Nerchuko jar. Nerchuko's dependencies
are:

* [clojure](http://github.com/richhickey/clojure/)
* [clojure-contrib](http://github.com/richhickey/clojure-contrib/)
* [clj-text](http://github.com/sids/clj-text)

### Building from source
If you have [git](http://git-scm.com/download) installed on your
system, use the following command to get the Nerchuko source code:

    git clone git://github.com/sids/nerchuko.git

Otherwise, you can directly download the source code from here:
[http://github.com/sids/nerchuko/tarball/master](http://github.com/sids/nerchuko/tarball/master
Download Nerchuko source).

You will need
[lein](http://zef.me/2470/building-clojure-projects-with-leiningen)
installed to build Nerchuko from the source. Build the Nerchuko jar
using the following command:

    cd nerchuko
    lein jar

## Usage

The API documentation can be found here:
[http://sids.github.com/nerchuko](http://sids.github.com/nerchuko
Nerchuko API Documentation).

Look in the [examples](http://github.com/sids/nerchuko/tree/master/src/nerchuko/examples/)
directory for some of examples demonstrating the usage of Nerchuko.
This is currently the best way to learn to use Nerchuko.

## License

Copyright (C) 2010 [Siddhartha Reddy](http://www.siddhartha-reddy.com/).

Distributed under the [Apache License Version
 2.0](http://www.apache.org/licenses/LICENSE-2.0). See the file LICENSE.
