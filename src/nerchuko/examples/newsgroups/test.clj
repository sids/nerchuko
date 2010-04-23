(ns nerchuko.examples.newsgroups.test
  (:use nerchuko.examples.newsgroups.helpers)
  (:use [nerchuko helpers classification])
  (:use clojure.contrib.command-line))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Classify the files in the given directories and compare the
classification with the correct class name (the directory name).
Prints the confusion matrix.

USAGE: test [options] <directory> [<directory> ...]"
    [[model-file "File to load the learned model from." "/tmp/nerchuko.examples.newsgroups.model"]
     dirs]

    (if (seq dirs)
      (let [model (read-string (slurp model-file))]
        (->> dirs
             load-test-dataset
             (get-confusion-matrix model)
             print-confusion-matrix))
      (println "Error: Directory arguments missing."))))
