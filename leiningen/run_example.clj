(ns leiningen.run-example
  (:use [leiningen.compile :only (eval-in-project)]))

(defn run-example
  "Runs an example from nerchuko.examples.* namespace.

USAGE: lein run-example <example-name>

Look in examples/src/nerchuko/examples/ for the possible example names."
  [project name & args]
  (if name
    (eval-in-project project
                     `(let [ns# (symbol (str "nerchuko.examples." ~name ".main"))]
                        (require ns#)
                        (@(ns-resolve ns# '~'-main) ~@args)
                        nil))
    (println (doc run-example))))
