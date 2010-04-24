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
[http://clojars.org/nerchuko](http://clojars.org/nerchuko).

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
[http://github.com/sids/nerchuko/tarball/master](http://github.com/sids/nerchuko/tarball/master).

You will need
[lein](http://zef.me/2470/building-clojure-projects-with-leiningen)
installed to build Nerchuko from the source. Build the Nerchuko jar
using the following command:

    cd nerchuko
    lein jar

## API

The API documentation can be found here:
[http://sids.github.com/nerchuko](http://sids.github.com/nerchuko).

## Usage: Classification

Here is a quick example of using Nerchuko for classification:

    (require 'nerchuko.classification)

    (def training-dataset '(["" :interesting]
                            ["" :not-interesting]
                            ["" :not-interesting]
                            ["" :interesting]))
    (def doc-to-classify "")
    (def classifier 'nerchuko.classifiers.naive-bayes.multinomial)

    (def model (learn-model classifier
                            training-dataset))

    (classify model
              doc-to-classify)
    (scores model
            doc-to-classify)

All the classifiers that ship with Nerchuko are in the namespace
nerchuko.classifiers.*. nerchuko.classification provides some helpers
that accept the name of the classifier and call the appropriate
functions in it (learn-model, classify, scores etc.); this should be
preferred over directly calling the functions in the classifiers
(apart from providing a unified interface, this has the added
advantage of easy experimentation -- by allowing use of different
classifiers simply by changing a name at one place.)

Typically, using Nerchuko for classification tasks involves the
following tasks:

### Load training dataset

Training dataset must be a seq of examples, each example being a
2-item vector comprising a document and the correct class for the
document.

### Prepare the documents for use with Nerchuko:

Different Nerchuko classifiers require the documents to be represented
in different ways. Typically, the documents need to be represented as
a map of attributes ("features") mapped to values. Depending on the
classifier, the values could be numeric, categorical or a mixture of
both.

Refer the documentation of the classifier you are using for the
representation it requires. Most of the classifiers will automatically
convert documents to the representation they require. Refer the
documentation of the prepare-doc function in the classifier to learn
how this transformation will be done.

Nerchuko provides several helper functions that could be very useful when
preparing the documents:

* [nerchuko.helpers/bag](http://sids.github.com/nerchuko/helpers-api.html#nerchuko.helpers/bag)
* [nerchuko.text.helpers/tokenize](http://sids.github.com/nerchuko/text.helpers-api.html#nerchuko.text.helpers/tokenize)
* [nerchuko.text.helpers/tokenize-vals](http://sids.github.com/nerchuko/text.helpers-api.html#nerchuko.text.helpers/tokenize-vals)
* [nerchuko.text.helpers/bag-of-words](http://sids.github.com/nerchuko/text.helpers-api.html#nerchuko.text.helpers/bag-of-words)
* [nerchuko.text.helpers/set-of-words](http://sids.github.com/nerchuko/text.helpers-api.html#nerchuko.text.helpers/bag-of-words)

**Important**: Please note that documents should usually be prepared
  in the exact same way when used as a part of the training/test
  datasets and when being directly used for classification.

### Feature selection:

### Learning a model:

### Classify new instances:

## Examples

Look in the
[examples/](http://github.com/sids/nerchuko/tree/master/src/nerchuko/examples/)
directory for some examples demonstrating the usage of Nerchuko. These
examples use Nerchuko to work with some standard machine learning
datasets. This is currently the best way to learn to use Nerchuko.

### [20 Newsgroups Data Set](http://people.csail.mit.edu/jrennie/20Newsgroups/)

> The 20 Newsgroups data set is a collection of approximately 20,000
> newsgroup documents, partitioned (nearly) evenly across 20 different
> newsgroups.



## License

Copyright (C) 2010 [Siddhartha Reddy](http://www.siddhartha-reddy.com/).

Distributed under the [Apache License Version
 2.0](http://www.apache.org/licenses/LICENSE-2.0). See the file LICENSE.
