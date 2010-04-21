(ns nerchuko.text.helpers
  (:use [nerchuko.utils :only (on-submap)])
  (:use clojure.contrib.generic.functor
        [clojure.contrib.def :only (defnk)]
        [clojure.contrib.seq-utils :only (separate)])
  (:require [clj-text.tokenization :as t]))

(defn tokenize
  "Given a string, returns a list of tokens.
Uses clj.text.tokenization/tokenize of tokenization."
  [s]
  (t/tokenize s))

(defn tokenize-map
  "Given a map, replaces string values with a list of
tokens from the string. Uses tokenize of tokenization.

Additional keyword arguments :only or :except can be
passed to limit the keys whose values are acter upon.

For example:

  (def m {:a \"hello world!\"
          :b 2
          :c [\"this will never be tokenized\"]
          :d \"good bye\"})

  (tokenize-map m)
  => {:a (\"hello\" \"world!\")
      :b 2
      :c [\"this will never be tokenized\"]
      :d (\"good\" \"bye\")}

  (tokenize-map m :only #{:a :b})
  => {:a (\"hello\" \"world!\")
      :b 2
      :c [\"this will never be tokenized\"]
      :d \"good bye\"}

  (tokenize-map m :except #{:a :c})
  => {:a \"hello world!\"
      :b 2
      :c [\"this will never be tokenized\"]
      :d (\"good\" \"bye\")}"
  ([m & options]
     (apply on-submap tokenize-map m
            options))
  ([m]
     (fmap (fn [x]
             (if (string? x)
               (tokenize x)
               x))
           m)))
