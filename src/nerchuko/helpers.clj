(ns nerchuko.helpers
  (:use nerchuko.utils
        clj.text.tokenization))

(defmulti prepare-doc class)

(defmethod prepare-doc String [doc]
  (prepare-doc (tokenize doc)))

(defmethod prepare-doc clojure.lang.PersistentArrayMap [doc]
  (prepare-doc (flatten-map doc)))

(defmethod prepare-doc clojure.lang.Seqable [doc]
  (counts doc))
