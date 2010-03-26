(ns nerchuko.feature-selectors.collection-frequency
  (:use nerchuko.utils)
  (:use clojure.contrib.generic.functor))

(defn find-features
  "Returns a set of the top k most frequently occurring features.
Multiple occurrences of a feature in a document are counted
multiple times."
  [k training-dataset]
  (->> training-dataset
       firsts
       (reduce merge-with-+)
       (largest-n-by-vals k)
       keys
       set))
