(ns test.run
  (:use clojure.test)
  (:require test.nerchuko.classifiers.naive-bayes))

(defn run []
  (run-all-tests #"^test.nerchuko..*"))

(defn run-ant []
  (let [rpt report]
    (binding [;; binding to *err* because, in ant, when the test target
              ;; runs after compile-clojure, *out* doesn't print anything
              *out* *err*
              *test-out* *err*
              report (fn report [m]
                       (if (= :summary (:type m))
                         (do (rpt m)
                             (if (or (pos? (:fail m)) 
                                     (pos? (:error m)))
                               (System/exit 1)))
                         (rpt m)))]
      (run))))

(defn -main
  "Run all defined tests from the command line"
  [& args]
  (run)
  (System/exit 0))
