(ns nerchuko.examples.newsgroups.train
  (:gen-class)
  (:use nerchuko.examples.newsgroups.helpers)
  (:use [nerchuko helpers classification feature-selection])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Build a model from the files in the directories.
The directory names are taken as the class names.

USAGE: train [options] <directory> [<directory> ...]"
    [[model-file "File to save the learned model in." "/tmp/nerchuko.examples.newsgroups.model"]
     [features "Number of features to use for classification." "100"]
     [feature-selector "The feature-selector to use." "nerchuko.feature-selectors.chi-squared"]
     [classifier "The classifier to use." "nerchuko.classifiers.naive-bayes.multinomial"]
     dirs]

    (if (seq dirs)
      (->> dirs
           load-training-dataset
           (find-and-select-features feature-selector
                                     (Integer/valueOf features))
           (learn-model classifier)
           (save-model model-file))
      (println "Error: Directory arguments missing."))))
