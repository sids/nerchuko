(ns test.nerchuko.feature-selection.country
  (:use nerchuko.feature-selection
        nerchuko.utils
        nerchuko.helpers)
  (:use clojure.test))

(def training-dataset [["chinese beijing chinese" :yes]
                       ["chinese chinese shanghai" :yes]
                       ["chinese macao shanghai" :yes]
                       ["tokyo japan chinese" :no]])

(deftest feature-selection-document-frequency
  (are [k features] (= features
                       (find-features 'nerchuko.feature-selectors.document-frequency
                                      k training-dataset))
       1 #{"chinese"}
       2 #{"shanghai" "chinese"}
       6 #{"shanghai" "tokyo" "macao" "chinese" "japan" "beijing"}))

(deftest feature-selection-collection-frequency
  (are [k features] (= features
                       (find-features 'nerchuko.feature-selectors.collection-frequency
                                      k training-dataset))
       1 #{"chinese"}
       2 #{"shanghai" "chinese"}
       6 #{"shanghai" "tokyo" "macao" "chinese" "japan" "beijing"}))

(deftest feature-selection-chi-squared
  (are [k features] (= features
                       (find-features 'nerchuko.feature-selectors.chi-squared
                               k training-dataset))
       1 #{"tokyo"}
       2 #{"tokyo" "japan"}
       6 #{"shanghai" "tokyo" "beijing" "japan" "chinese" "macao"}))
