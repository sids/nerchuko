(ns test.nerchuko.feature-selection.country
  (:use nerchuko.feature-selection
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [[[:chinese :beijing :chinese] :yes]
                       [[:chinese :chinese :shanghai] :yes]
                       [[:chinese :macao] :yes]
                       [[:tokyo :japan :chinese] :no]])

(deftest feature-selection-document-frequency
  (are [k features] (= (select 'nerchuko.feature-selectors.document-frequency
                               k
                               training-dataset)
                       features)
       1 #{:chinese}
       2 #{:shanghai :chinese}
       4 #{:shanghai :tokyo :macao :chinese}))

(deftest feature-selection-collection-frequency
  (are [k features] (= (select 'nerchuko.feature-selectors.collection-frequency
                               k
                               training-dataset)
                       features)
       1 #{:chinese}
       2 #{:shanghai :chinese}
       4 #{:shanghai :tokyo :macao :chinese}))

(deftest feature-selection-chi-squared
  (are [k features] (= (select 'nerchuko.feature-selectors.chi-squared
                               k
                               training-dataset)
                       features)
       1 #{:tokyo}
       2 #{:tokyo :japan}
       4 #{:shanghai :tokyo :beijing :japan}))
