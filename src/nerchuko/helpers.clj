(ns nerchuko.helpers)

(defn as-str
  "If x is a Keyword or a Symbol, returns (name x), otherwise returns (.toString x)."
  [x]
  (cond
    (or (keyword? x) (symbol? x)) (name x)
    :default (str x)))

(defn merge-with-+
  "Calls merge-with on maps with + as the function."
  [& maps]
  (reduce (partial merge-with +) maps))

(defn counts
  "Returns a map with the items of coll as keys and the number of occurrences of the item in coll as the corresponding values."
  [coll]
  (reduce merge-with-+
          (map #(hash-map % 1) coll)))

(defn firsts [coll]
  (map first coll))

(defn lasts [coll]
  (map last coll))

(defn map-on-firsts
  "Takes a sequence of 2-item vectors [v1 v2] and returns a lazy sequence of [(f v1) v2]. If a collection is given, calls seq on it."
  [f pair-seq]
  (map (fn [[v1 v2]] [(f v1) v2]) pair-seq))

(defn map-on-lasts
  "Takes a sequence of 2-item vectors [v1 v2] and returns a lazy sequence of [v1 (f v2)]. If a collection is given, calls seq on it."
  [f pair-seq]
  (map (fn [[v1 v2]] [v1 (f v2)]) pair-seq))

(defn key-with-max-val
  "Returns the key which has the max value.
Returns on-all-equal if all the values are equal."
  [map & [on-all-equal]]
  (if (apply = (vals map))
    on-all-equal
    (first (reduce (fn [[k-max v-max] [k v]]
                     (if (> v v-max)
                       [k v] [k-max v-max]))
                   map))))

(defn to-probabilities
  "Given a coll of numbers, converts them to probabilities
by dividing each with their total sum. Returns a seq."
  [coll]
  (let [total (reduce + coll)]
    (map #(/ % total) coll)))

(defn largest-n-by-vals
  "Returns a subset of map containing the n largest entries
as determined by comparing the vals."
  [n map]
  (let [comparator (fn [v1 v2] (compare v2 v1))]
    (into {}
          (take n (sort-by val comparator map)))))
