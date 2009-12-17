(ns test.nerchuko.classification.naive-bayes.country
  (:use nerchuko.classification.naive-bayes
        nerchuko.helpers)
  (:use clojure.test))

(def training-data [[[:chinese :beijing :chinese] :yes]
                   [[:chinese :chinese :shanghai] :yes]
                   [[:chinese :macao] :yes]
                   [[:tokyo :japan :chinese] :no]])

(defn- transform [doc]
  (counts doc))

(defn- test-doc [doc]
  (let [model (generate-model (map-on-firsts transform training-data))]
    (classify model (transform doc))))

(deftest naive-bayes
  (is (= {:yes 0.6897586117634674, :no 0.3102413882365325}
         (test-doc [:chinese :chinese :chinese :tokyo :japan])))
  (is (= {:yes 3/4, :no 1/4}
         (test-doc []))))
