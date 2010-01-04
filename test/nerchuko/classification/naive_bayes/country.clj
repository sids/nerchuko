(ns test.nerchuko.classification.naive-bayes.country
  (:use nerchuko.classification.naive-bayes.multinomial
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [[[:chinese :beijing :chinese] :yes]
                       [[:chinese :chinese :shanghai] :yes]
                       [[:chinese :macao] :yes]
                       [[:tokyo :japan :chinese] :no]])

(deftest naive-bayes
  (let [model (learn-model training-dataset)]
    (is (= {:yes 0.6897586117634674, :no 0.3102413882365325}
           (probabilities model [:chinese :chinese :chinese :tokyo :japan])))
    (is (= :yes
           (classify model [:chinese :chinese :chinese :tokyo :japan])))
    (is (= {:yes 3/4, :no 1/4}
           (probabilities model [])))
    (is (= :yes
           (classify model [])))))
