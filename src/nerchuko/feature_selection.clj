(ns nerchuko.feature-selection
  "This namespace provides the primary functions for accessing
nerchuko's feature selection capabilities."
  (:use nerchuko.utils)
  (:require nerchuko.feature-selectors.chi-squared
            nerchuko.feature-selectors.document-frequency
            nerchuko.feature-selectors.collection-frequency))

(defn find-features
  "Return a set of the _best_ k features from the dataset using
  feature-selector. The docs in the dataset must all be
  features-map's."
  [feature-selector k dataset]
  (call feature-selector
        'find-features
        [k dataset]))

(defn select-features
  "Given a set of features and a dataset, filters every doc in the
  dataset to keep only those features. The docs in the dataset must
  all be features-map's. Returns the modified dataset."
  [dataset features]
  (map-on-firsts #(select-keys % features)
                 dataset))

(defn find-and-select-features
  "Find the _best_ k features using feature-selector and then filter
the features in each doc of the dataset to keep only those
features. Returns the modified dataset."
  [feature-selector k dataset]
  (->> dataset
       (find-features feature-selector k)
       (select-features dataset)))
