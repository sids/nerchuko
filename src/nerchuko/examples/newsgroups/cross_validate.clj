(ns nerchuko.examples.newsgroups.cross-validate
  (:use nerchuko.examples.newsgroups.helpers)
  (:use [nerchuko utils classification feature-selection])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Run n-fold cross-validation and print the results.

USAGE: cross-validate [options] <directory> [<directory> ...]"
    [[folds      "Number of folds for cross-validation." "10"]
     [features   "Number of features to use for classification." "100"]
     [feature-selector "The feature-selector to use." "nerchuko.feature-selectors.chi-squared"]
     [classifier "The classifier to use." "nerchuko.classifiers.naive-bayes.multinomial"]
     dirs]

    (if (seq dirs)
      (->> dirs
           load-training-dataset
           (find-and-select-features feature-selector
                                     (Integer/valueOf features))
           (cross-validate classifier
                           (Integer/valueOf folds)))
      (println "Error: Directory arguments missing."))))
