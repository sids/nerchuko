(ns nerchuko.classification.naive-bayes.multinomial
  (:use nerchuko.helpers)
  (:use [clojure.set :only (intersection)]
        clojure.contrib.generic.math-functions
        clojure.contrib.generic.functor))

(defn- get-model-aggregates
  "Returns the aggregate information gleaned from the training data.
The aggregate information is returned as a map with the following keys:
:class is a set of the classes appearing the training data.
:features is a set of the features appearing in the training data.
:priors is a map of the prior probability for each class.
:cond-probs is a map of the conditional probabilities of each feature
            for each class. It is returned as a map with the classes as keys
            and the values themselves as maps with the features as the keys
            (and the conditional probabilities as the values)."
  [training-data]
  (reduce (fn [[classes features class-counts class-features] [doc cls]]
            [(conj classes cls) ; classes, a set
             (into features (keys doc)) ; features, a set
             (merge-with-+ class-counts {cls 1}) ; class-counts, a map
             (merge-with merge-with-+ class-features {cls doc})]) ; class-features, a map (with maps as values)
          [#{} #{} {} {}]
          training-data))

(defn learn-model
  "Returns a classifier model learned as a Naive Bayes learner.
The training data must be a sequence of training examples
each of which must be a 2-item vector of the document and the corresponding class."
  [training-data]
  (let [[classes features class-counts class-features] (get-model-aggregates training-data)
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

(defn- cls-likelihood
  "Given a doc and the conditional probabilities of the features for a class,
returns the likelihood that the document belongs to the class.
Essentially, this multiplies the conditional probabilities of the features
that appear in the doc."
  [doc cls-cond-probs]
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

(defn classify
  "Classifies the doc using a Naive Bayes classifier based on model.
Returns the most probable class."
  [model doc]
  (let [doc (select-keys doc (:features model))
        numerators (reduce into
                          (map (fn [cls]
                                 (let [prior ((:priors model) cls)
                                       likelihood (cls-likelihood doc ((:cond-probs model) cls))]
                                   {cls (* prior likelihood)}))
                               (:classes model)))
        denominator (reduce + (vals numerators))]
    (key-with-max-val
      (reduce merge (map (fn [[cls numerator]]
                           {cls (/ numerator denominator)})
                         numerators)))))

(defn classifier
  "Returns (partial classify model).
The return value can be used as a classifier function that classifies
a given doc."
  [model]
  (partial classify model))
