(ns nerchuko.examples.spambase.train
  (:gen-class)
  (:use nerchuko.examples.spambase.helpers)
  (:use [nerchuko helpers classification feature-selection])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Build a model from the spambase.data file.

Each line is treated as one item of the training data. Each line must
be a comma-separated list of integers/floats; each of them is treated
as the value of the attribute at that position. The last attribute is
treated as the correct class, 1 => spam, 0 => ham (not spam).

USAGE: train [options] <spambase.data-file>"
    [[model-file "File to save the learned model in." "/tmp/nerchuko.examples.spambase.model"]
     [features "Number of features to use for classification." "57"]
     [feature-selector "The feature-selector to use." "nerchuko.feature-selectors.chi-squared"]
     [classifier "The classifier to use." "nerchuko.classifiers.naive-bayes.multinomial"]
     file]

    (if (seq file)
      (->> (first file)
           load-dataset
           (find-and-select-features feature-selector
                                     (Integer/valueOf features))
           (learn-model classifier)
           (save-model model-file))
      (println "Error: File argument missing."))))
