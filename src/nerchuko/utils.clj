(ns nerchuko.utils
  "General Clojure utilities. These are used pretty heavily within
the nerchuko code itself and might prove to be quite useful even
for applications using nerchuko.")

;;;;;; miscellaneus utils

(defn as-str
  "If x is a Keyword or a Symbol, returns (name x),
otherwise returns (.toString x)."
  [x]
  (cond
    (or (keyword? x) (symbol? x)) (name x)
    :default (str x)))

(defmacro call
  "Macro that expands to a call to function f in the given namespace."
  ([ns f]
     `(call ns f []))
  ([ns f args]
     `(apply (ns-resolve (the-ns (symbol ~ns)) ~f) ~args)))

(defn to-probabilities
  "Given a coll of numbers, converts them to probabilities
by dividing each with their total sum. Returns a seq."
  [coll]
  (let [total (reduce + coll)]
    (map #(if-not (zero? total)
            (/ % total)
            0.0) coll)))

;;;;;; utils for transforming data structures (one data structure to another)

(declare merge-with-+)

(defn counts
  "Returns a map with the items of coll as keys and the number
of occurrences of the item in coll as the corresponding values."
  [coll]
  (if (seq coll)
    (reduce merge-with-+
            (map #(hash-map % 1) coll))
    {}))

(defn to-seq
  "Returns a seq.
If v implements Sequential, simply calls seq on it.
Otherwise returns a seq containing v as the only element."
  [v]
  (if (sequential? v)
    (seq v)
    (seq (list v))))

(defn pairs
  "Returns a hash-map of vectors where each vector is a pairing of key
with a value from vals."
  [key vals]
  (map #(vector key %) vals))

(defn flatten-map
  "'Flattens' the map into a seq.
Each key contributes one of more elements to the seq. The elements
corresponding to any given key are obtained by calling pairs on
the to-seq of its value."
  [m]
  (reduce into
          (map (fn [[k v]] (pairs k (to-seq v)))
               m)))

(defn partition-random
  "Randomly divides the items in coll into n partitions.
Returns a vector of vectors."
  [n coll]
  (let [ret (into [] (repeat n []))]
    (reduce (fn [ret x]
              (let [pos (rand-int n)]
                (assoc ret pos (conj (ret pos) x))))
            ret
            coll)))

;;;;;; utils for transforming seqs

(defn merge-with-+
  "Calls merge-with on maps with + as the function."
  [& maps]
  (reduce (partial merge-with +) maps))

(defn firsts [coll]
  (map first coll))

(defn lasts [coll]
  (map last coll))

(defn map-on
  "A map that accepts the mapping function as the last argument instead of the first."
  [& colls-and-f]
  (let [f (last colls-and-f)
        colls (-> colls-and-f
                  reverse
                  rest
                  reverse)]
    (apply map f colls)))

(defn map-on-firsts
  "Takes a sequence of sequences (v1 v2 ...) and returns a lazy sequence of
((f v1) (f v2) ...). If a collection is given, calls seq on it."
  [f seq]
  (map (fn [[v1 & vs]]
         (into [(f v1)] vs))
       seq))

(defn map-on-lasts
  "Takes a sequence of 2-item vectors [v1 v2] and returns a lazy sequence
of [v1 (f v2)]. If a collection is given, calls seq on it."
  [f pair-seq]
  (map (fn [[v1 v2]] [v1 (f v2)]) pair-seq))

(defn map-with-index
  "Calls map on the collections after cons-ing (iterate inc 0) to them.
So f will receive the index along with the items themselves."
  [f & colls]
  (apply map f (cons (iterate inc 0) colls)))

(defn map-with-index-on
  "Calls map on the collections after cons-ing (iterate inc 0) to them.
So f will receive the index along with the items themselves."
  [& colls-and-f]
  (apply map-on (cons (iterate inc 0) colls-and-f)))

;;;;;; utils for selecting subsets of seqs

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

(defn largest-n-by-vals
  "Returns a subset of map containing the n largest entries
as determined by comparing the vals."
  [n map]
  (let [comparator (fn [v1 v2] (compare v2 v1))]
    (into {}
          (take n (sort-by val comparator map)))))
