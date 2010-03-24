(ns test.nerchuko.feature-selection.tennis
  (:use nerchuko.feature-selection
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

(deftest feature-selection-document-frequency
  (are [k features] (= (select 'nerchuko.feature-selectors.document-frequency
                               k
                               training-dataset)
                       features)
       1 #{:weak}
       2 #{:weak :normal}
       4 #{:strong :weak :normal :high}))

(deftest feature-selection-collection-frequency
  (are [k features] (= (select 'nerchuko.feature-selectors.collection-frequency
                               k
                               training-dataset)
                       features)
       1 #{:weak}
       2 #{:weak :normal}
       4 #{:strong :weak :normal :high}))

(deftest feature-selection-chi-squared
  (are [k features] (= (select 'nerchuko.feature-selectors.chi-squared
                               k
                               training-dataset)
                       features)
       1 #{:overcast}
       2 #{:overcast :normal}
       4 #{:sunny :overcast :normal :high}))
