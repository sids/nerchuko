(ns nerchuko.text.helpers
  (:use [nerchuko.utils :only (on-submap)]
        [nerchuko.helpers :only (bag)])
  (:use clojure.contrib.generic.functor
        [clojure.contrib.def :only (defnk)]
        [clojure.contrib.seq-utils :only (separate)])
  (:require [clj-text.tokenization :as t]))

(defn tokenize
  "Given a string, returns a list of tokens.
Uses clj.text.tokenization/tokenize of tokenization."
  [s]
  (t/tokenize s))

(defn tokenize-vals
  "Given a map, replaces its string vals with a list of tokens from
the string. Uses tokenize for tokenization.

Additional keyword arguments :only or :except can be passed to limit
the keys whose values are acter upon.

For example:

  => (def m {:a \"hello world!\"
            :b 2
            :c [\"this will never be tokenized\"]
            :d \"good bye\"})

  => (tokenize-vals m)
  {:a (\"hello\" \"world!\")
   :b 2
   :c [\"this will never be tokenized\"]
   :d (\"good\" \"bye\")}

  => (tokenize-vals m :only #{:a :b})
  {:a (\"hello\" \"world!\")
   :b 2
   :c [\"this will never be tokenized\"]
   :d \"good bye\"}

  => (tokenize-vals m :except #{:a :c})
  {:a \"hello world!\"
   :b 2
   :c [\"this will never be tokenized\"]
   :d (\"good\" \"bye\")}"
  ([m & options]
     (apply on-submap tokenize-vals m
            options))
  ([m]
     (fmap (fn [x]
             (if (string? x)
               (tokenize x)
               x))
           m)))

(defn bag-of-words
  "Tokenizes s and constructs a bag (multiset) of the tokens"
  [s]
  (->> s
       tokenize
       bag))

(defn set-of-words
  "Tokenizes s and constructs a bag (multiset) of the tokens"
  [s]
  (->> s
       tokenize
       set))
