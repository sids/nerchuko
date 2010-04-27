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

## Getting Started

Please note that Nerchuko is under active development. There may be
bugs and the API may change without notice.

The API documentation can be found here:
[http://sids.github.com/nerchuko](http://sids.github.com/nerchuko).

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

Otherwise, you can download the source code from here:
[http://github.com/sids/nerchuko/tarball/master](http://github.com/sids/nerchuko/tarball/master).

You will need
[lein](http://zef.me/2470/building-clojure-projects-with-leiningen)
installed to build Nerchuko from the source. Build the Nerchuko jar
using the following command:

    cd nerchuko
    lein jar

## Classification

Nerchuko's classification capabilities can be accessed through
[nerchuko.classification](http://sids.github.com/nerchuko/classification-api.html).
Documentation for the namespace provides a simple example of how to use
it. For a more elaborate example, look at the [20 Newsgroups
example](http://github.com/sids/nerchuko/tree/master/src/nerchuko/examples/newsgroups/).

The nerchuko.classification namespace also includes other functions
that might be useful when dealing with classification tasks: n-fold
cross validation; produce, manipulate & print confusion matrices.
More helper functions can be found in the namespaces
[nerchuko.helpers](http://sids.github.com/nerchuko/helpers-api.html).

When working on text classification, functions in the
[nerchuko.text.helpers](http://sids.github.com/nerchuko/helpers-api.html)
namespace might be useful.

Nerchuko includes implementations for the following classifiers:
 * [Naive Bayes Multinomial](http://sids.github.com/nerchuko/classifiers.naive-bayes.multinomial-api.html)
 * [Naive Bayes Bernoulli](http://sids.github.com/nerchuko/classifiers.naive-bayes.bernoulli-api.html)

#### Feature Selection

Nerchuko's feature selection capabilities can be accessed through
[nerchuko.feature-selection](http://sids.github.com/nerchuko/feature-selection-api.html).
Documentation for the namespace provides simple example of how to use
it. For a more elaborate example, look at the [20 Newsgroups
example](http://github.com/sids/nerchuko/tree/master/src/nerchuko/examples/newsgroups/).

Nerchuko includes implementations for the following feature selection
techniques:
 * [Chi-Square](http://sids.github.com/nerchuko/feature-selectors.chi-squared-api.html)
 * [Document Frequency](http://sids.github.com/nerchuko/feature-selectors.document-frequency-api.html)
 * [Collection Frequency](http://sids.github.com/nerchuko/feature-selectors.collection-frequency-api.html)

## Examples

Look in the
[examples/](http://github.com/sids/nerchuko/tree/master/src/nerchuko/examples/)
directory for some examples demonstrating the usage of Nerchuko. These
examples use Nerchuko to work with some standard machine learning
datasets. This is currently the best way to learn to use Nerchuko.

You can run the examples using the command

    lein run-example

This will print out a short help with instructions on running specific examples.

### [20 Newsgroups Data Set](http://people.csail.mit.edu/jrennie/20Newsgroups/)

> The 20 Newsgroups data set is a collection of approximately 20,000
> newsgroup documents, partitioned (nearly) evenly across 20 different
> newsgroups.

This is a very simple and good example demonstrating the usage of
Nerchuko for text classification/categorization.

Download the data set from the above link and then run this using the
command:

    lein run-example newsgroups

### [Spambade Data Set](http://archive.ics.uci.edu/ml/datasets/Spambase)

> The "spam" concept is diverse: advertisements for products/web sites,
> make money fast schemes, chain letters, pornography...
>
> Our collection of spam e-mails came from our postmaster and
> individuals who had filed spam. Our collection of non-spam e-mails
> came from filed work and personal e-mails, and hence the word 'george'
> and the area code '650' are indicators of non-spam. These are useful
> when constructing a personalized spam filter. One would either have to
> blind such non-spam indicators or get a very wide collection of
> non-spam to generate a general purpose spam filter.

Although this might seem like another example for text classification,
the the text has been preprocessed and we are presented with a numeric
data set.

Download the data set from the above link and then run this using the
command:

    lein run-example spambase

## License

Copyright (C) 2010 [Siddhartha Reddy](http://www.siddhartha-reddy.com/).

Distributed under the [Apache License Version
 2.0](http://www.apache.org/licenses/LICENSE-2.0). See the file LICENSE.
