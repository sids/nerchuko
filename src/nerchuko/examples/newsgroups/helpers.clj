(ns nerchuko.examples.newsgroups.helpers
  (:use [nerchuko helpers utils]
        nerchuko.text.helpers)
  (:use [clojure.contrib.str-utils2 :only (lower-case)]))

(defn- get-files
  "Given a seq of directories, returns a seq of all the files in them
  as java.io.File objects."
  [dirs]
  (->> dirs
       (map #(->> %
                  java.io.File.
                  .listFiles
                  vec))
       (reduce concat)))

(defn load-doc
  [file]
  (-> file
      slurp
      tokenize
      bag))

(defn load-training-dataset
  "Given a seq of directories, returns a training dataset loaded from
  the files contained in the directories. The training dataset is a
  seq of 2-item vectors, the first item being the doc (contents of a
  file), and the second item the correct class (name of the directory
  containing the file)."
  [dirs]
  (->> dirs
       get-files
       (map #(vector (load-doc (str %))
                     (.. %
                         (getParentFile)
                         (getName))))))

(defn load-test-dataset
  "Given a seq of directories, returns a test dataset loaded from the
  files contained in the directories. The test dataset is a seq of
  3-item vectors, the first item being the doc (contents of a file),
  the second item the correct class (name of the directory containing
  the file) and the third item the path to the file."
  [dirs]
  (->> dirs
       get-files
       (map #(vector (load-doc (str %))
                     (.. %
                         (getParentFile)
                         (getName))
                     (str %)))))
