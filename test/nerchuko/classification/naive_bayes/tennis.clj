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

(deftest naive-bayes-multinomial
  (let [model (learn-model 'nerchuko.classifiers.naive-bayes.multinomial
                           training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         #{:sunny :hot :high :weak} {:yes 0.3131320321244135, :no 0.6868679678755865}
         #{}                        {:yes 9/14, :no 5/14})
    (are [doc result] (= result
                         (classify model doc))
         #{:sunny :hot :high :weak} :no
         #{}                        :yes)))

(deftest naive-bayes-bernoulli
  (let [model   (learn-model 'nerchuko.classifiers.naive-bayes.bernoulli
                             training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         #{:sunny :hot :high :weak} {:no 0.8415604739564735, :yes 0.15843952604352654}
         #{}                        {:no 0.2848800102948953, :yes 0.7151199897051047})
    (are [doc result] (= result
                         (classify model doc))
         #{:sunny :hot :high :weak} :no
         #{}                        :yes)))

(deftest naive-bayes-complement
  (let [model (learn-model 'nerchuko.classifiers.naive-bayes.complement
                           training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         #{:sunny :hot :high :weak} {:yes 0.31313203212441315, :no 0.6868679678755869}
         #{}                        {:yes 9/14, :no 5/14})
    (are [doc result] (= result
                         (classify model doc))
         #{:sunny :hot :high :weak} :no
         #{}                        :yes)))
