(ns nerchuko.classification.naive-bayes.multinomial
  (:use nerchuko.helpers)
  (:use [clojure.set :only (intersection)]
        clojure.contrib.generic.math-functions
        clojure.contrib.generic.functor))

(defn- get-model-aggregates [training-set]
  (reduce (fn [[classes features class-counts class-features] [doc cls]]
            [(conj classes cls) ; classes, a set
             (into features (keys doc)) ; features, a set
             (merge-with-+ class-counts {cls 1}) ; class-counts, a map
             (merge-with merge-with-+ class-features {cls doc})]) ; class-features, a map (with maps as values)
          [#{} #{} {} {}]
          training-set))

(defn generate-model [training-set]
  (let [[classes features class-counts class-features] (get-model-aggregates training-set)
        total-docs (reduce + (vals class-counts))
        features-0 (fmap dec (counts features))
        features-count (count features)]
    {:classes classes
     :features features
     :priors (fmap #(/ % total-docs) class-counts)
     :cond-probs (fmap (fn [cls-features]
                         (let [cls-features (merge features-0 cls-features)
                               cls-features-total (reduce + (vals cls-features))
                               denominator (+ cls-features-total features-count)]
                           (fmap #(/ (inc %) denominator) cls-features)))
                       class-features)}))

(defn- calculate-cls-likelihood [doc cls-cond-probs]
  (reduce (fn [likelihood [feature cnt]]
            (* likelihood (pow (cls-cond-probs feature) cnt)))
          1
          doc))

(defn key-with-max-val
  "Returns the key which has the max value."
  [map]
  (first (reduce (fn [[k-max v-max] [k v]]
                   (if (> v v-max)
                     [k v] [k-max v-max]))
                 map)))

(defn classify [model doc]
  (let [doc (select-keys doc (:features model))
        numerators (reduce into
                          (map (fn [cls]
                                 (let [prior ((:priors model) cls)
                                       likelihood (calculate-cls-likelihood doc ((:cond-probs model) cls))]
                                   {cls (* prior likelihood)}))
                               (:classes model)))
        denominator (reduce + (vals numerators))]
    (key-with-max-val
      (reduce merge (map (fn [[cls numerator]]
                           {cls (/ numerator denominator)})
                         numerators)))))
