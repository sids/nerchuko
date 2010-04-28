(ns nerchuko.text.helpers
  "Functions that are helpful when dealing with text documents."
  (:use [nerchuko.utils :only (flatten-map)])
  (:use clojure.contrib.generic.functor
        [clojure.contrib.def :only (defnk)]
        [clojure.contrib.seq-utils :only (frequencies flatten)])
  (:require [clj-text.tokenization :as t]))

(defmulti
  #^{:arglists '([str-or-coll])
     :doc "Given a string, returns a list of tokens.  Given any
collection, tokenizes the values in that collection (i.e. tokenizes
the elements of lists, vectors, sets etc. and tokenizes the vals of
maps.  Given any other object, returns it unchanged.  Uses
clj.text.tokenization/tokenize for tokenization."}
  tokenize type)

(defmethod tokenize String
  [s]
  (t/tokenize s))

(defmethod tokenize java.util.Collection
  [coll]
  (map tokenize coll))

(defmethod tokenize java.util.Map
  [m]
  (->> m
       (into {})
       (fmap tokenize)))

(defmethod tokenize :default
  [x] x)

(defn bag-of-tokens
  "Tokenizes s and constructs a bag (multiset) of the tokens."
  [s]
  (->> s
       tokenize
       frequencies))

(defn set-of-tokens
  "Tokenizes s and constructs a bag (multiset) of the tokens."
  [s]
  (->> s
       tokenize
       set))
