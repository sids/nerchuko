(ns nerchuko.feature-selection.document-frequency
  (:use nerchuko.helpers)
  (:use clojure.contrib.generic.functor))

(defn- largest-n-by-vals
  "Returns a subset of the map containing the n largest entries as
determined by comparing the vals."  [n map]
  (let [comparator (fn [v1 v2] (compare v2 v1))]
    (into {}
          (take n (sort-by val comparator map)))))

(defn select
  "Returns a set of the top k most frequently occurring features.
Even if a feature occurs multiple times in a document, it is counted
only as a single occurrence."  [k training-dataset]
  (->> training-dataset
       firsts
       (map (comp counts keys))
       (reduce merge-with-+)
       (largest-n-by-vals k)
       keys
       set))
