(ns nerchuko.feature-selection.collection-frequency
  (:use nerchuko.helpers)
  (:use clojure.contrib.generic.functor))

(defn select
  "Returns a set of the top k most frequently occurring features.
Multiple occurrences of a feature in a document are counted
multiple times."
  [k training-dataset]
  (->> training-dataset
       firsts
       (map counts)
       (reduce merge-with-+)
       (largest-n-by-vals k)
       keys
       set))
