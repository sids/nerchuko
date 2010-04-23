(ns test.nerchuko.classification.naive-bayes.tennis
  (:use nerchuko.classification
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [[#{:sunny :hot :high :weak} :no]
                       [#{:sunny :hot :high :strong} :no]
                       [#{:overcast :hot :high :weak} :yes]
                       [#{:rain :mild :high :weak} :yes]
                       [#{:rain :cool :normal :weak} :yes]
                       [#{:rain :cool :normal :strong} :no]
                       [#{:overcast :cool :normal :strong} :yes]
                       [#{:sunny :mild :high :weak} :no]
                       [#{:sunny :cool :normal :weak} :yes]
                       [#{:rain :mild :normal :weak} :yes]
                       [#{:sunny :mild :normal :strong} :yes]
                       [#{:overcast :mild :high :strong} :yes]
                       [#{:overcast :hot :normal :weak} :yes]
                       [#{:rain :mild :high :strong} :no]])

(deftest naive-bayes
  (let [multinomial-model (->> training-dataset
                   (map-firsts bag)
                   (learn-model 'nerchuko.classifiers.naive-bayes.multinomial))
        bernoulli-model (->> training-dataset
                   (map-firsts bag)
                   (learn-model 'nerchuko.classifiers.naive-bayes.bernoulli))]
    (is (= {:yes 0.3131320321244135, :no 0.6868679678755865}
           (scores multinomial-model
                   (bag #{:sunny :hot :high :weak}))))
    (is (= {:no 0.8415604739564735, :yes 0.15843952604352654}
           (scores bernoulli-model
                   (bag #{:sunny :hot :high :weak}))))
    (is (= :no
           (classify multinomial-model
                     (bag #{:sunny :hot :high :weak}))))
    (is (= :no
           (classify bernoulli-model
                     (bag #{:sunny :hot :high :weak}))))
    (is (= {:yes 9/14, :no 5/14}
           (scores multinomial-model {})))
    (is (= {:no 0.2848800102948953, :yes 0.7151199897051047}
           (scores bernoulli-model #{})))
    (is (= :yes
           (classify multinomial-model {})))
    (is (= :yes
           (classify bernoulli-model #{})))))
