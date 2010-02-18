(ns nerchuko.helpers
  (:use nerchuko.utils))

(defmulti prepare-doc class)

(defmethod prepare-doc clojure.lang.PersistentArrayMap [doc]
  (prepare-doc (flatten-map doc)))

(defmethod prepare-doc clojure.lang.Seqable [doc]
  (counts doc))
