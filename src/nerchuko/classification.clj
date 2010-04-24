(ns nerchuko.classification
  "This namespace provides the primary functions for accessing
nerchuko's classification capabilities."
  (:use nerchuko.utils)
  (:require nerchuko.classifiers.naive-bayes.multinomial)
  (:use [clojure.contrib.def :only (defnk)])
  (:require [clojure.contrib.str-utils2 :as str-utils2]))

(defn learn-model
  "Uses the classifier implementation set to *classifier* to learn from
the training-dataset and generate a model. Returns the model.

training-dataset must be a sequence of training examples each of which
must be a 2-item vector of the document and the corresponding class.
Each document should be a map with the features as the keys and the
'quantity' of those features as the values."
  [classifier training-dataset]
  (call-in-ns classifier 'learn-model
              training-dataset))

(defn scores
  "Classifies doc using model and returns the scores for each class as a map."
  [model doc]
  (call-in-ns (:classifier model) 'scores
              model doc))

(defn classify
  "Classifies doc using model and returns the class with the maximum score."
  [model doc]
  (call-in-ns (:classifier model) 'classify
              model doc))

(defn get-confusion-matrix
  "Generates a confusion matrix by classifying every document in test-dataset
using model."
  [model test-dataset]
  (let [empty-matrix (reduce (fn [h class]
                               (assoc h class {}))
                             {}
                             (:classes model))]
    (->> test-dataset
         (map (fn [[doc class]]
                {class {(classify model doc) 1}}))
         (reduce (partial merge-with merge-with-+))
         (merge empty-matrix))))

(defn merge-confusion-matrices
  "Merges together any number of confusion matrices by adding together the
corresponding values."
  [& matrices]
  (reduce (partial merge-with merge-with-+) matrices))

(declare print-confusion-matrix)

(defnk get-confusion-matrices
  [classifier n training-dataset :print? true]
  (let [folds (partition-random n
                                training-dataset)]
    (doall
     (->> folds
          (map-with-index
            (fn [idx test-dataset]
              (let [training-dataset (apply concat
                                            (concat (take idx folds)
                                                    (rest (drop idx folds))))
                    model            (learn-model classifier
                                                  training-dataset)
                    confusion-matrix (get-confusion-matrix model
                                                           test-dataset)]
                (if print?
                  (do
                    (println "\nTrial" (inc idx) (str-utils2/repeat "=" 32) "\n")
                    (print-confusion-matrix confusion-matrix)))
                confusion-matrix)))))))

(defnk cross-validate
  "Performs n-fold cross-validation of training-dataset using
classifier. Takes an optional boolean argument :print?; if
true (default is true), prints the confusion matrices of each trial as
well as a summary confusion matrix. The summary confusion matrix is
obtained by adding together the confusion matrices from the
trials. Returns the summary confusion matrix."
  [classifier n training-dataset :print? true]
  (let [confusion-matrices (get-confusion-matrices classifier n
                                                   training-dataset
                                                   :print? print?)
        summary-matrix     (apply merge-confusion-matrices
                                  confusion-matrices)]
    (if print?
      (do
        (println "\nSummary" (str-utils2/repeat "=" 32) "\n")
        (print-confusion-matrix summary-matrix)))
    summary-matrix))

(defn- get-total-docs [matrix]
  (->> matrix
       vals
       (map #(reduce + (vals %)))
       (reduce +)))

(defn- get-total-correct-classifications [matrix]
  (->> matrix
       (map (fn [[class classifications]]
              (get classifications class 0)))
       (reduce +)))

(defn- get-max-class-name-length [classes]
  (->> classes
       (map as-str)
       (map #(.length %))
       (reduce max)))

(defn- get-max-index-length [classes]
  (->> classes
       count
       str
       .length))

(defn- get-max-count-length [matrix]
  (->> matrix
       vals
       (map #(reduce max (cons 0 (vals %))))
       (reduce max)
       str
       .length))

(defn- get-max-total-length [matrix]
  (->> matrix
       vals
       (map #(reduce + (vals %)))
       (reduce max)
       str
       .length))

(defn print-confusion-matrix
  "Pretty prints a confusion matrix."
  [matrix]
  (let [total (get-total-docs matrix)
        correct (get-total-correct-classifications matrix)
        accuracy (if-not (zero? total) (* 100.0 (/ correct total)))]
    (println "Correct:" correct
             "out of" total
             "(" (format "%5.2f%%" accuracy) "accuracy )"
             "\n"))

  (let [classes (sort (keys matrix))
        index-length      (get-max-index-length classes)
        class-name-length (get-max-class-name-length classes)
        count-length      (get-max-count-length matrix)
        total-length      (max (get-max-total-length matrix)
                               (.length "total"))

        index-format      (str "%" index-length "d")
        class-name-format (str "%" class-name-length "s")
        count-format      (str "%" (inc count-length) "d")
        total-format      (str "%" (+ 3 total-length) "s")

        header-first-format (str "%" (inc (+ index-length class-name-length)) "s")
        header-index-format (str "%" (inc (max index-length count-length)) "s")]
    
                                        ; header
    (println (format header-first-format "class")
             (reduce str
                     (map-with-index (fn [index _]
                                       (format header-index-format (inc index)))
                       classes))
             (format total-format "total")
             "correct"
             "\n")

                                        ; rows
    (dorun (->> classes
                (map-with-index
                  (fn [index class]
                    (let [row (get matrix class {})]
                      (println (format index-format (inc index))
                               (format class-name-format (as-str class))
                               (reduce str
                                       (map (fn [class-2]
                                              (format count-format (get row class-2 0)))
                                            classes))
                               (let [total (reduce + 0 (vals row))]
                                 (str (format total-format total)
                                      (if-not (zero? total)
                                        (format " %5.2f%%" (* 100.0 (/ (get row class 0) total)))))))))))))
  matrix)
