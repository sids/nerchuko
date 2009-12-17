(ns test.nerchuko.classification.naive-bayes.country
  (:use nerchuko.classification.naive-bayes.multinomial
        nerchuko.helpers)
  (:use clojure.test))

(def training-data [[[:chinese :beijing :chinese] :yes]
                   [[:chinese :chinese :shanghai] :yes]
                   [[:chinese :macao] :yes]
                   [[:tokyo :japan :chinese] :no]])

(defn- transform [doc]
  (counts doc))

(defn- test-doc [doc]
  (let [model (learn-model (map-on-firsts transform training-data))]
    (classify model (transform doc))))

(deftest naive-bayes
  (is (= :yes
         (test-doc [:chinese :chinese :chinese :tokyo :japan])))
  (is (= :yes
         (test-doc []))))
