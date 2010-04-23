(ns nerchuko.examples.newsgroups.main
  (:require [nerchuko.examples.newsgroups train test cross-validate classify])
  (:use [nerchuko.utils :only (call-in-ns)])
  (:use clojure.contrib.command-line))

(def actions #{"train" "test" "cross-validate" "classify"})

(defn -main [& [action & action-args]]
  (if (actions action)
    (apply call-in-ns (str "nerchuko.examples.newsgroups." action) '-main
           action-args)
    (println "Demonstrates use of Nerchuko for classification using the 20-Newsgroups dataset.
(http://people.csail.mit.edu/jrennie/20Newsgroups/)

Call this with commandline args: <actiom> [args-for-action]

<action> must be one of:
    train            Train a classifier and save the model.
    test             Test a classifier using an already-saved model.
    cross-validate   Cross-validate the performance of a classifier.
    classify         Classify a single instance (file) using a saved model and print the scores.

Calling an action without any args will print a help explaining the args it accepts/expects.")))
