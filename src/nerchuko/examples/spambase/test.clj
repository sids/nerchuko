(ns nerchuko.examples.spambase.test
  (:use nerchuko.examples.spambase.helpers)
  (:use [nerchuko helpers classification])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Classify the lines in the given spambase.data file and compare
the classification with the correct class (the last attribute.) Prints
the confusion matrix.

Each line is treated as one item of the test data. Each line must
be a comma-separated list of integers/floats; each of them is treated
as the value of the attribute at that position. The last attribute is
treated as the correct class, 1 => spam, 0 => ham (not spam).

USAGE: test [options] <spambase.data-file>"
    [[model-file "File to load the learned model from." "/tmp/nerchuko.examples.spambase.model"]
     file]

    (if (seq file)
      (let [model (read-string (slurp model-file))]
        (->> (first file)
             load-dataset
             (get-confusion-matrix model)
             print-confusion-matrix))
      (println "Error: File argument missing."))))
