(ns nerchuko.feature-selection
  (:use nerchuko.helpers)
  (:require nerchuko.feature-selection.chi-squared
            nerchuko.feature-selection.document-frequency
            nerchuko.feature-selection.collection-frequency)
  (:use [clojure.contrib.def :only (defvar)]))

(defvar *feature-selector* 'nerchuko.feature-selection.chi-squared)

(defn select [k training-dataset]
  (call *feature-selector* 'select [k training-dataset]))

(defn select-and-filter
  ([k training-dataset]
     (select-and-filter k
                        training-dataset
                        training-dataset))
  ([k dataset-for-select dataset-to-filter]
     (let [features (select k dataset-for-select)]
       (map-on-firsts #(filter features %)
                      dataset-to-filter))))
