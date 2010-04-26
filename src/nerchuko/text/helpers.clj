(ns nerchuko.text.helpers
  (:use [nerchuko.utils :only (on-submap)])
  (:use clojure.contrib.generic.functor
        [clojure.contrib.def :only (defnk)]
        [clojure.contrib.seq-utils :only (frequencies)])
  (:require [clj-text.tokenization :as t]))

(defn tokenize
  "Given a string, returns a list of tokens.
Uses clj.text.tokenization/tokenize of tokenization."
  [s]
  (t/tokenize s))

(defn bag-of-tokens
  "Tokenizes s and constructs a bag (multiset) of the tokens"
  [s]
  (->> s
       tokenize
       frequencies))

(defn set-of-tokens
  "Tokenizes s and constructs a bag (multiset) of the tokens"
  [s]
  (->> s
       tokenize
       set))
