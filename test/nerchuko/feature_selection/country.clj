(ns test.nerchuko.feature-selection.country
  (:use nerchuko.helpers)
  (:require [nerchuko.feature-selection.document-frequency :as document-frequency]
            [nerchuko.feature-selection.document-frequency :as collection-frequency]
            [nerchuko.feature-selection.chi-squared :as chi-squared])
  (:use clojure.test))

(def training-dataset [[[:chinese :beijing :chinese] :yes]
                       [[:chinese :chinese :shanghai] :yes]
                       [[:chinese :macao] :yes]
                       [[:tokyo :japan :chinese] :no]])

(deftest feature-selection-document-frequency
  (is (= (document-frequency/select 1 training-dataset)
         #{:chinese}))
  (is (= (document-frequency/select 2 training-dataset)
         #{:shanghai :chinese}))
  (is (= (document-frequency/select 4 training-dataset)
         #{:shanghai :tokyo :macao :chinese})))

(deftest feature-selection-collection-frequency
  (is (= (collection-frequency/select 1 training-dataset)
         #{:chinese}))
  (is (= (collection-frequency/select 2 training-dataset)
         #{:shanghai :chinese}))
  (is (= (collection-frequency/select 4 training-dataset)
         #{:shanghai :tokyo :macao :chinese})))

(deftest feature-selection-chi-squared
  (is (= (chi-squared/select 1 training-dataset)
         #{:tokyo}))
  (is (= (chi-squared/select 2 training-dataset)
         #{:tokyo :japan}))
  (is (= (chi-squared/select 4 training-dataset)
         #{:shanghai :tokyo :beijing :japan})))
