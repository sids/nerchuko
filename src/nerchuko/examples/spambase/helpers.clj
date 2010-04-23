(ns nerchuko.examples.spambase.helpers
  (:use [nerchuko helpers utils])
  (:use [clojure.contrib.duck-streams :only (read-lines)]
        [clojure.contrib.str-utils :only (re-split)]))

(def class-names {1 :spam
                  0 :ham})

(defmulti load-doc type)

(defmethod load-doc :default
  [vals]
  (->> vals
       (map #(Double/parseDouble %))
       (zipmap (iterate inc 0))))

(defmethod load-doc String
  [line]
  (->> line
       (re-split #",")
       load-doc))

(defn load-dataset
  "Given the path to the spambase.data file, returns the training/test dataset."
  [file]
  (->> file
       read-lines
       (map (fn [line]
              (let [vals (re-split #"," line)
                    doc  (-> vals
                             drop-last
                             load-doc)
                    cls  (->> vals
                              last
                              (Integer/parseInt)
                              (get class-names))]
                [doc cls])))))
