(ns test.nerchuko.feature-selection.tennis
  (:use nerchuko.feature-selection
        nerchuko.utils
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

(def training-dataset
     (map-on-firsts prepare-doc
                    training-data))

(deftest feature-selection-document-frequency
  (binding [*feature-selector* 'nerchuko.feature-selection.document-frequency]
    (is (= (select 1 training-dataset)
           #{:weak}))
    (is (= (select 2 training-dataset)
           #{:weak :normal}))
    (is (= (select 4 training-dataset)
           #{:strong :weak :normal :high}))))

(deftest feature-selection-collection-frequency
  (binding [*feature-selector* 'nerchuko.feature-selection.collection-frequency]
    (is (= (select 1 training-dataset)
           #{:weak}))
    (is (= (select 2 training-dataset)
           #{:weak :normal}))
    (is (= (select 4 training-dataset)
           #{:strong :weak :normal :high}))))

(deftest feature-selection-chi-squared
  (binding [*feature-selector* 'nerchuko.feature-selection.chi-squared]
    (is (= (select 1 training-dataset)
           #{:overcast}))
    (is (= (select 2 training-dataset)
           #{:overcast :normal}))
    (is (= (select 4 training-dataset)
           #{:sunny :overcast :normal :high}))))
