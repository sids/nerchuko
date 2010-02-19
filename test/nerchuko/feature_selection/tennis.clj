(ns test.nerchuko.feature-selection.tennis
  (:use nerchuko.utils
        nerchuko.helpers)
  (:require [nerchuko.feature-selection.document-frequency :as document-frequency]
            [nerchuko.feature-selection.collection-frequency :as collection-frequency]
            [nerchuko.feature-selection.chi-squared :as chi-squared])
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
  (is (= (document-frequency/select 1 training-dataset)
         #{:weak}))
  (is (= (document-frequency/select 2 training-dataset)
         #{:weak :normal}))
  (is (= (document-frequency/select 4 training-dataset)
         #{:strong :weak :normal :high})))

(deftest feature-selection-collection-frequency
  (is (= (collection-frequency/select 1 training-dataset)
         #{:weak}))
  (is (= (collection-frequency/select 2 training-dataset)
         #{:weak :normal}))
  (is (= (collection-frequency/select 4 training-dataset)
         #{:strong :weak :normal :high})))

(deftest feature-selection-chi-squared
  (is (= (chi-squared/select 1 training-dataset)
         #{:overcast}))
  (is (= (chi-squared/select 2 training-dataset)
         #{:overcast :normal}))
  (is (= (chi-squared/select 4 training-dataset)
         #{:sunny :overcast :normal :high})))
