(ns nerchuko.feature-selectors.collection-frequency
  "Selection of features based on the number of occurrence across the
dataset.

This feature selector works with numeric features map. If given
documents are in any other representation, they are converted to a
numeric features map using prepare-doc."
  (:use nerchuko.utils)
  (:use clojure.contrib.generic.functor))

(defmulti
  #^{:arglists '([doc])
     :doc "Converts the given doc to a Bag (multiset) of features,
represented as a map with the features as keys and the number of
occurrences of those features as the values.

How the Bag is created depends on what doc is:

If doc is a Map, returns it as it is after checking that all the vals
are numbers; if any of the vals aren't numbers, throws an
IllegalArgumentException.

If doc is a Collection, returns a map with each distinct element in
the collection as a key and its number of occurrences as the
corresponding value.

If doc is a String, tokenizes it and returns a map with each token as
a key and the number of occurrences of the token within the document
as the corresponding value. Uses nerchuko.text.helpers/bag-of-tokens.

If doc is anything else, throws an IllegalArgumentException."}
  prepare-doc type)

(defmethod prepare-doc java.util.Map
  [m]
  (if (every? number? (vals m))
       m
       (throw (IllegalArgumentException.
               "If document is a map, all the vals must be numbers."))))

(defmethod prepare-doc java.util.Collection
  [coll]
  (frequencies coll))

(defmethod prepare-doc String
  [s]
  (bag-of-tokens s))

(defmethod prepare-doc :default
  [_]
  (throw (IllegalArgumentException.
          "Document must be one of: Map, Collection, String.")))

(defn find-features
  "Returns a set of the top k most frequently occurring features.
Multiple occurrences of a feature in a document are counted
multiple times."
  [k training-dataset]
  (->> training-dataset
       (firsts)
       (map prepare-doc)
       (reduce merge-with-+)
       (largest-n-by-vals k)
       (keys)
       (set)))
