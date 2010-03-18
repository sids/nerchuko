(ns nerchuko.feature-selectors.document-frequency
  (:use nerchuko.utils)
  (:use clojure.contrib.generic.functor))

(defn select
  "Returns a set of the top k most frequently occurring features.
Even if a feature occurs multiple times in a document, it is counted
only as a single occurrence."  [k training-dataset]
  (->> training-dataset
       firsts
       (reduce merge-with-+)
       (largest-n-by-vals k)
       keys
       set))
