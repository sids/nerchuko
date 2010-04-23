(ns nerchuko.examples.spambase.classify
  (:use nerchuko.examples.spambase.helpers)
  (:use [nerchuko helpers classification])
  (:use [clojure.contrib command-line pprint]))

(defn -main [& args]
  (with-command-line (if (seq args) args ["--help"])
    "Classify a single instance of the spambase data.
Prints the scores for each class in the model.

USAGE: test [options] <spambase-instance line>"
    [[model-file "File to load the learned model from." "/tmp/nerchuko.examples.spambase.model"]
     line]

    (if (seq line)
      (let [model (read-string (slurp model-file))]
        (->> (first line)
             load-doc
             (scores model)
             pprint))
      (println "Error: Instance specification argument missing."))))
