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

(deftest naive-bayes
  (let [training-dataset (map-on-firsts transform training-data)
        model (learn-model training-dataset)]
    (is (= {:yes 0.6897586117634674, :no 0.3102413882365325}
           (probabilities model (transform [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= :yes
           (classify model (transform [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= {:yes 3/4, :no 1/4}
           (probabilities model (transform []))))
    (is (= :yes
           (classify model (transform []))))))
