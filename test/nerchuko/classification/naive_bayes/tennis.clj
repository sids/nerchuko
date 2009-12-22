(ns test.nerchuko.classification.naive-bayes.tennis
  (:use nerchuko.classification.naive-bayes.multinomial
        nerchuko.helpers)
  (:use clojure.test))

(def training-data [[#{:sunny :hot :high :weak} :no]
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

(defn- transform [doc]
  (counts doc))

(deftest naive-bayes
  (let [training-dataset (map-on-firsts transform training-data)
        model (learn-model training-dataset)]
    (is (= {:yes 0.3131320321244134, :no 0.6868679678755866}
           (probabilities model (transform #{:sunny :hot :high :weak}))))
    (is (= :no
           (classify model (transform #{:sunny :hot :high :weak}))))
    (is (= {:yes 9/14, :no 5/14}
           (probabilities model (transform []))))
    (is (= :yes
           (classify model (transform []))))))
