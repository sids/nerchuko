(ns test.nerchuko.feature-selection.country
  (:use nerchuko.helpers)
  (:require [nerchuko.feature-selection.document-frequency :as document-frequency]
            [nerchuko.feature-selection.document-frequency :as collection-frequency])
  (:use clojure.test))

(def training-data [[[:chinese :beijing :chinese] :yes]
                   [[:chinese :chinese :shanghai] :yes]
                   [[:chinese :macao] :yes]
                   [[:tokyo :japan :chinese] :no]])

(defn- transform [doc]
  (counts doc))

(deftest feature-selection-document-frequency
  (let [training-dataset (map-on-firsts transform training-data)]
    (is (= (document-frequency/select 1 training-dataset)
           #{:chinese}))
    (is (= (document-frequency/select 2 training-dataset)
           #{:shanghai :chinese}))
    (is (= (document-frequency/select 4 training-dataset)
           #{:shanghai :tokyo :macao :chinese}))))

(deftest feature-selection-collection-frequency
  (let [training-dataset (map-on-firsts transform training-data)]
    (is (= (collection-frequency/select 1 training-dataset)
           #{:chinese}))
    (is (= (collection-frequency/select 2 training-dataset)
           #{:shanghai :chinese}))
    (is (= (collection-frequency/select 4 training-dataset)
           #{:shanghai :tokyo :macao :chinese}))))
