(ns nerchuko.feature-selection
  "This namespace provides the primary functions for accessing
nerchuko's feature selection capabilities."
  (:use nerchuko.utils)
  (:require nerchuko.feature-selection.chi-squared
            nerchuko.feature-selection.document-frequency
            nerchuko.feature-selection.collection-frequency)
  (:use [clojure.contrib.def :only (defvar)]))

(defvar *feature-selector* 'nerchuko.feature-selection.chi-squared)

(defn- resolve-feature-selector [c]
  (if (.startsWith (as-str c) "nerchuko.feature-selection.")
    c
    (str "nerchuko.feature-selection." (as-str c))))

(defn select
  "Return a set of the _best_ k features from the training-dataset.
The feature selection algorithm set to *feature-selector* will be used."
  [k training-dataset]
  (call (resolve-feature-selector *feature-selector*)
        'select
        [k training-dataset]))

(defn select-and-filter
  "Select the _best_ k features based on the algorithm set to
*feature-selecter* and then filter the features in each doc of the
training-dataset. The modified training-dataset is returned."
  ([k training-dataset]
     (select-and-filter k
                        training-dataset
                        training-dataset))
  ([k dataset-for-select dataset-to-filter]
     (let [features (select k dataset-for-select)]
       (map-on-firsts #(filter features %)
                      dataset-to-filter))))
