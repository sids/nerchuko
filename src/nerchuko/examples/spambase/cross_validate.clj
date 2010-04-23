(ns nerchuko.examples.spambase.cross-validate
  (:use nerchuko.examples.spambase.helpers)
  (:use [nerchuko utils classification feature-selection])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Run n-fold cross-validation on the spambase.data file and print
the confusion matrices.

Each line is treated as one item of the training data. Each line must
be a comma-separated list of integers/floats; each of them is treated
as the value of the attribute at that position. The last attribute is
treated as the correct class, 1 => spam, 0 => ham (not spam).

USAGE: cross-validate [options] <spambase.data-file>"
    [[folds      "Number of folds for cross-validation." "10"]
     [features   "Number of features to use for classification." "57"]
     [feature-selector "The feature-selector to use." "nerchuko.feature-selectors.chi-squared"]
     [classifier "The classifier to use." "nerchuko.classifiers.naive-bayes.multinomial"]
     file]

    (if (seq file)
      (->> (first file)
           load-dataset
           (find-and-select-features feature-selector
                                     (Integer/valueOf features))
           (cross-validate classifier
                           (Integer/valueOf folds)))
      (println "Error: File argument missing."))))
