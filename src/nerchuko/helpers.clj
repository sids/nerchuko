(ns nerchuko.helpers
  "Functions that help with various auxiliary tasks such
as converting documents to the necessary format etc."
  (:use nerchuko.utils
        clj.text.tokenization))

(defmulti prepare-doc
  #^{:arglists '([doc])
     :doc      "Returns a document that can be used as an
input to the various classification/feature-selection functions.
The input doc must be one of:
String, PersistentMap or any Seqable."}
  class)

(defmethod prepare-doc String [doc]
  (prepare-doc (tokenize doc)))

(defmethod prepare-doc clojure.lang.PersistentArrayMap [doc]
  (prepare-doc (flatten-map doc)))

(defmethod prepare-doc clojure.lang.Seqable [doc]
  (counts doc))
