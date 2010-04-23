(ns test.nerchuko.classification.naive-bayes.country
  (:use nerchuko.classification
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [[[:chinese :beijing :chinese] :yes]
                       [[:chinese :chinese :shanghai] :yes]
                       [[:chinese :macao] :yes]
                       [[:tokyo :japan :chinese] :no]])

(deftest naive-bayes
  (let [multinomial-model (->> training-dataset
                               (map-firsts bag)
                               (learn-model 'nerchuko.classifiers.naive-bayes.multinomial))
        bernoulli-model (->> training-dataset
                               (map-firsts bag)
                               (learn-model 'nerchuko.classifiers.naive-bayes.bernoulli))]
    (is (= {:yes 0.6897586117634673, :no 0.31024138823653274}
           (scores multinomial-model
                   (bag [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= {:yes 0.19106678876165256, :no 0.8089332112383474}
           (scores bernoulli-model
                   (bag [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= :yes
           (classify multinomial-model
                     (bag [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= :no
           (classify bernoulli-model
                     (bag [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= {:yes 3/4, :no 1/4}
           (scores multinomial-model {})))
    (is (= {:yes 3/4, :no 1/4}
           (scores bernoulli-model {})))
    (is (= :yes
           (classify multinomial-model {})))
    (is (= :yes
           (classify bernoulli-model {})))))
