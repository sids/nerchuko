(ns nerchuko.feature-selection
  "This namespace provides the primary interface to nerchuko's feature
selection capabilities.

Here is a typical (simplified) workflow for feature selection:

    ;; access feature selection capabilities through this namespace
    (require '[nerchuko.feature-selection :as fs])

    ;; also keep some helper functions handy
    (require '[nerchuko.helpers :as h])

    ;; load training dataset: training dataset must be a seq of
    ;; 2-element vectors where the first element is a document
    ;; and the second element the correct class of that document
    (def training-data '([\"\" :interesting]
                         [\"\" :not-interesting]
                         [\"\" :not-interesting]
                         [\"\" :interesting]))

    ;; convert all the documents in the training data into numeric
    ;; documents -- most feature selection algorithms are able to
    ;; work only with numeric documents
    (def training-dataset (h/numeric-dataset training-data))

    ;; pick a feature-selector: all the feature-selectors are in the
    ;; namespace nerchuko.feature-selectors
    (def feature-selector 'nerchuko.feature-selectors.chi-squared)

    ;; decide the number k of features to select
    (def k 40)
    ;; you'll probably want to experiment with different values to
    ;; determine what works best for your dataset

    ;; select the best k features from the dataset
    (def features
         (fs/find-features feature-selector k training-dataset))

    ;; create an altered dataset that contains only the selected
    ;; features
    (def better-training-dataset
         (fs/select-features training-dataset features))

    ;; or do both the above in one shot
    (def better-training-dataset
         (fs/find-and-select-features feature-selector k training-dataset))

    ;; better-training-dataset can now be used for classification etc."
  (:use nerchuko.utils)
  (:require [nerchuko.feature-selectors chi-squared document-frequency collection-frequency]))

(defn find-features
  "Return a set of the _best_ k features from the dataset using
  feature-selector. The docs in the dataset must all be
  features-map's."
  [feature-selector k dataset]
  (call-in-ns feature-selector 'find-features
              k dataset))

(defn select-features
  "Given a set of features and a dataset, filters every doc in the
  dataset to keep only those features. The docs in the dataset must
  all be features-map's. Returns the modified dataset."
  [dataset features]
  (map-firsts #(select-keys % features)
              dataset))

(defn find-and-select-features
  "Find the _best_ k features using feature-selector and then filter
the features in each doc of the dataset to keep only those
features. Returns the modified dataset."
  [feature-selector k dataset]
  (->> dataset
       (find-features feature-selector k)
       (select-features dataset)))
