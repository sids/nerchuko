(ns test.nerchuko.feature-selection.country
  (:use nerchuko.feature-selection
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-data [[[:chinese :beijing :chinese] :yes]
                    [[:chinese :chinese :shanghai] :yes]
                    [[:chinese :macao] :yes]
                    [[:tokyo :japan :chinese] :no]])

(def training-dataset
     (map-on-firsts prepare-doc
                    training-data))

(deftest feature-selection-document-frequency
  (binding [*feature-selector* 'nerchuko.feature-selection.document-frequency]
    (is (= (select 1 training-dataset)
           #{:chinese}))
    (is (= (select 2 training-dataset)
           #{:shanghai :chinese}))
    (is (= (select 4 training-dataset)
           #{:shanghai :tokyo :macao :chinese}))))

(deftest feature-selection-collection-frequency
  (binding [*feature-selector* 'nerchuko.feature-selection.collection-frequency]
    (is (= (select 1 training-dataset)
           #{:chinese}))
    (is (= (select 2 training-dataset)
           #{:shanghai :chinese}))
    (is (= (select 4 training-dataset)
           #{:shanghai :tokyo :macao :chinese}))))

(deftest feature-selection-chi-squared
  (binding [*feature-selector* 'nerchuko.feature-selection.chi-squared]
    (is (= (select 1 training-dataset)
           #{:tokyo}))
    (is (= (select 2 training-dataset)
           #{:tokyo :japan}))
    (is (= (select 4 training-dataset)
           #{:shanghai :tokyo :beijing :japan}))))
