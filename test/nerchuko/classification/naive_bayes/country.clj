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
  (let [training-dataset (map-on-firsts prepare-doc
                                        training-dataset)
        model (learn-model 'nerchuko.classifiers.naive-bayes.multinomial
                           training-dataset)]
    (is (= {:yes 0.6897586117634673, :no 0.31024138823653274}
           (scores model
                   (prepare-doc
                    [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= :yes
           (classify model
                     (prepare-doc
                      [:chinese :chinese :chinese :tokyo :japan]))))
    (is (= {:yes 3/4, :no 1/4}
           (scores model {})))
    (is (= :yes
           (classify model {})))))
