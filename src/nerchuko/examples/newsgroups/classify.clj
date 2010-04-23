(ns nerchuko.examples.newsgroups.classify
  (:use nerchuko.examples.newsgroups.helpers)
  (:use [nerchuko helpers classification])
  (:use [clojure.contrib command-line pprint]))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Classify a single file.
Prints the scores for each class in the model.

USAGE: test [options] <file-to-classify>"
    [[model-file "File to load the learned model from." "/tmp/nerchuko.examples.newsgroups.model"]
     file]

    (if (seq file)
      (let [model (read-string (slurp model-file))]
        (->> (first file)
             load-doc
             (scores model)
             pprint))
      (println "Error: File arguments missing."))))
