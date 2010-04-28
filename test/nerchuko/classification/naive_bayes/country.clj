(ns test.nerchuko.classification.naive-bayes.country
  (:use nerchuko.classification
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [["chinese beijing chinese" :yes]
                       ["chinese chinese shanghai" :yes]
                       ["chinese macao" :yes]
                       ["tokyo japan chinese" :no]])

(deftest naive-bayes-multinomial
  (let [model (learn-model 'nerchuko.classifiers.naive-bayes.multinomial
                           training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         "chinese chinese chinese tokyo japan" {:yes 0.6897586117634673, :no 0.31024138823653274}
         ""                                    {:yes 3/4, :no 1/4})
    (are [doc result] (= result
                         (classify model doc))
         "chinese chinese chinese tokyo japan" :yes
         ""                                    :yes)))

(deftest naive-bayes-bernouli
  (let [model   (learn-model 'nerchuko.classifiers.naive-bayes.bernoulli
                             training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         "chinese chinese chinese tokyo japan" {:yes 0.19106678876165256, :no 0.8089332112383474}
         ""                                    {:yes 0.8831539824861842, :no 0.1168460175138158})
    (are [doc result] (= result
                         (classify model doc))
         "chinese chinese chinese tokyo japan" :no
         ""                                    :yes)))

(deftest naive-bayes-complement
  (let [model (learn-model 'nerchuko.classifiers.naive-bayes.complement
                           training-dataset)]
    (are [doc result] (= result
                         (scores model doc))
         "chinese chinese chinese tokyo japan" {:yes 0.6897586117634675, :no 0.3102413882365326}
         ""                                    {:yes 3/4, :no 1/4})
    (are [doc result] (= result
                         (classify model doc))
         "chinese chinese chinese tokyo japan" :yes
         ""                                    :yes)))
