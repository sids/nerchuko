(ns nerchuko.feature-selection
  "This namespace provides the primary functions for accessing
nerchuko's feature selection capabilities."
  (:use [nerchuko utils helpers])
  (:require nerchuko.feature-selectors.chi-squared
            nerchuko.feature-selectors.document-frequency
            nerchuko.feature-selectors.collection-frequency))

(defn select
  "Return a set of the _best_ k features from the training-dataset.
The feature selection algorithm set to *feature-selector* will be used."
  [feature-selector k training-dataset]
  (call feature-selector
        'select
        [k (prepare-dataset training-dataset)]))

(defn select-and-filter
  "Select the _best_ k features based on the algorithm set to
*feature-selecter* and then filter the features in each doc of the
training-dataset. The modified training-dataset is returned."
  ([feature-selector k training-dataset]
     (let [training-dataset (prepare-dataset training-dataset)]
       (select-and-filter feature-selector
                          k
                          training-dataset
                          training-dataset)))
  ([feature-selector k dataset-for-select dataset-to-filter]
     (let [features (select feature-selector
                            k
                            dataset-for-select)]
       (map-on-firsts #(select-keys % features)
                      (prepare-dataset dataset-to-filter)))))
