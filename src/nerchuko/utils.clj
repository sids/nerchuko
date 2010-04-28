(ns nerchuko.utils
  "General Clojure utilities. These are used pretty heavily within
the nerchuko code itself and might prove to be quite useful even
for applications using nerchuko."
  (:use [clojure.set :only (difference)]))

;;;;;; miscellaneus utils

(defn as-str
  "If x is a Keyword or a Symbol, returns (name x), otherwise
returns (.toString x)."
  [x]
  (cond
    (or (keyword? x) (symbol? x)) (name x)
    :default (str x)))

(defn call-in-ns
  "Resolves f in the namespace ns and calls it with args.
ns and f must be symbols or strings."
  [ns f & args]
  (-> (symbol ns)
      the-ns
      (ns-resolve (symbol f))
      (apply args)))

(defn to-probabilities
  "Given a coll of numbers, converts them to probabilities by dividing
each with their total sum. Returns a seq."
  [coll]
  (let [total (reduce + coll)]
    (map #(if-not (zero? total)
            (/ % total)
            0.0) coll)))

;;;;;; utils for transforming data structures (one data structure to another)

(defn to-seq
  "Returns a seq.
If v implements Seqable, simply calls seq on it.  Otherwise returns
a seq containing v as the only element."
  [v]
  (if (isa? (class v) clojure.lang.Seqable)
    (seq v)
    (seq (list v))))

(defn flatten-map
  "'Flattens' the map into a seq.
Elements of the seq are all 2-element vectors where the first element
is a key of m. The second elements are all derived from the values of
m as following: if a value of m is Seqable, every element of that seq
would appear as a second item; otherwise, the value itself will appear
as a second item.

For example:
    => (flatten-map {:a 1 :b [\"hello\" \"world\"] :c {:x 2 :y 3} :d \"good bye\"})
    ([:a 1] [:b \"hello\"] [:b \"world\"] [:c [:x 2]] [:c [:y 3]] [:d \"good bye\"])"
  [m]
  (seq
   (reduce into []
           (map (fn [[k v]]
                  (map #(vector k %) (to-seq v)))
                m))))

(declare firsts)

(defn select-keys-by
  "Like select-keys but the entries to select are determined based on
a selector function. The selector function is passed one entry at a
time (as two args) and must return a logical true to indicate that the
entry must be selected."
  [m f]
  (->> m
       (filter (fn [[k v]]
                 (f k v)))
       (into {})))

(defn unselect-keys
  "Opposite of select-keys: returns a map containing only those
entries whose key is not in keys."
  [m keyseq]
  (select-keys m
               (difference (set (keys m))
                           keyseq)))

(defn replace-submap
  "Selects a submap of map m and replaces it with the result of
calling f on that submap.

f must accept a map and return a map (or a seq that can be turned into
a map by calling (into {})).

The submap selection can be done either by specifying a function using
the keyword argument :selector or by specifying a list of keys to
keep/drop as keyword arguments :only/:except."
  [f m & options]
  (let [{except   :except
         only     :only
         selector :selector} (apply hash-map options)
         submap-to-replace   (cond
                              selector (select-keys-by m selector)
                              only     (select-keys m only)
                              except   (unselect-keys m except))
         submap-to-keep       (unselect-keys m (keys submap-to-replace))]
    (merge submap-to-keep
           (into {}
                 (f submap-to-replace)))))

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

(defn map-keys
  "Modifies the keys of the map m by calling f for each
of them."
  [f m]
  (->> m
       (map (fn [[k v]]
              [(f k) v])
            )
       (into {})))

(defn map-firsts
  "Takes a sequence of sequences (v1 v2 ...) and returns a lazy
  sequence of ((f v1) (f v2) ...). If a collection is given, calls seq
  on it."
  [f seq]
  (map (fn [[v1 & vs]]
         (into [(f v1)] vs))
       seq))

(defn map-lasts
  "Takes a sequence of 2-item vectors [v1 v2] and returns a lazy
  sequence of [v1 (f v2)]. If a collection is given, calls seq on it."
  [f pair-seq]
  (map (fn [[v1 v2]] [v1 (f v2)]) pair-seq))

(defn map-with-index
  "Calls map on the collections after cons-ing (iterate inc 0) to them.
So f will receive the index along with the items themselves."
  [f & colls]
  (apply map f (cons (iterate inc 0) colls)))

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
