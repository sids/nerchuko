(ns nerchuko.classification
  (:use nerchuko.helpers)
  (:require nerchuko.classification.naive-bayes.multinomial)
  (:use [clojure.contrib.def :only (defvar)]
        [clojure.contrib.duck-streams :only (spit)])
  (:require [clojure.contrib.str-utils2 :as str-utils2]))

(defvar *classifier* 'nerchuko.classification.naive-bayes.multinomial)

(defn learn-model [training-dataset]
  (call *classifier* 'learn-model [training-dataset]))

(defn classify [model doc]
  (call *classifier* 'classify [model doc]))

(defn save-model [file model]
  (binding [*print-dup* true]
    (spit file
          (pr-str model))))

(defn get-confusion-matrix [model test-dataset]
  (let [empty-matrix (reduce (fn [h class]
                               (assoc h class {}))
                             {}
                             (:classes model))]
    (->> test-dataset
         (map (fn [[doc class]]
                {class {(classify model doc) 1}}))
         (reduce (partial merge-with merge-with-+))
         (merge empty-matrix))))

(defn merge-confusion-matrices [& matrices]
  (reduce (partial merge-with merge-with-+) matrices))

(declare print-confusion-matrix)

(defn cross-validate [partitions]
  (let [confusion-matrices
        (doall
         (map-with-index-on partitions
           (fn [idx test-dataset]
             (println "\nTrial" (inc idx) (str-utils2/repeat "=" 32) "\n")
             (let [training-dataset (apply concat (concat (take idx partitions)
                                                          (rest (drop idx partitions))))
                   model (learn-model training-dataset)]
               (->> test-dataset
                    (get-confusion-matrix model)
                    print-confusion-matrix)))))]
    (println "\nSummary" (str-utils2/repeat "=" 32) "\n")
    (->> confusion-matrices
         (reduce merge-confusion-matrices)
         print-confusion-matrix)))

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
       (map #(reduce max (vals %)))
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

(defn print-confusion-matrix [matrix]
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
                     (map-with-index-on classes
                       (fn [index _]
                         (format header-index-format (inc index)))))
             (format total-format "total")
             "correct"
             "\n")

                                        ; rows
    (dorun (map-with-index-on classes
             (fn [index class]
               (let [row (get matrix class {})]
                 (println (format index-format (inc index))
                          (format class-name-format (as-str class))
                          (reduce str
                                  (map-on classes
                                          (fn [class-2]
                                            (format count-format (get row class-2 0)))))
                          (let [total (reduce + 0 (vals row))]
                            (str (format total-format total)
                                 (if-not (zero? total)
                                   (format " %5.2f%%" (* 100.0 (/ (get row class 0) total))))))))))))
  matrix)
