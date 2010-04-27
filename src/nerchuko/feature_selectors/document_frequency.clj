(ns nerchuko.feature-selectors.document-frequency
  "Selection of features based on the number of documents withing the
dataset they occur in.

This feature selector works with numeric features map. If given
documents are in any other representation, they are converted to a
numeric features map using prepare-doc."
  (:use nerchuko.utils)
  (:use clojure.contrib.generic.functor))

(defn find-features
  "Returns a set of the top k most frequently occurring features.
Even if a feature occurs multiple times in a document, it is counted
only as a single occurrence."  [k training-dataset]
  (->> training-dataset
       firsts
       (reduce merge-with-+)
       (largest-n-by-vals k)
       keys
       set))
