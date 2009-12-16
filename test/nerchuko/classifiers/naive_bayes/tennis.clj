(ns test.nerchuko.classifiers.naive-bayes.tennis
  (:use nerchuko.classifiers.naive-bayes
        nerchuko.helpers)
  (:use clojure.test))

(def training-set [[#{:sunny :hot :high :weak} :no]
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

(def unclassified-doc #{:sunny :hot :high :weak})

(defn- transform [doc]
  (counts doc))

(def model (generate-model (map-on-firsts transform training-set)))

(deftest naive-bayes
  (is (= 1 (- 3 1))))
