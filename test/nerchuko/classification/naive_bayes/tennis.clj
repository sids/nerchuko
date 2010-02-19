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
  (binding [*classifier* 'nerchuko.classification.naive-bayes.multinomial]
    (let [training-dataset (map-on-firsts prepare-doc
                                          training-dataset)
          model (learn-model training-dataset)]
      (is (= {:yes 0.3131320321244135, :no 0.6868679678755865}
             (scores model
                     (prepare-doc
                      #{:sunny :hot :high :weak}))))
      (is (= :no
             (classify model
                       (prepare-doc
                        #{:sunny :hot :high :weak}))))
      (is (= {:yes 9/14, :no 5/14}
             (scores model {})))
      (is (= :yes
             (classify model {}))))))
