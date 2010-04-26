(ns nerchuko.classifiers.naive-bayes.bernoulli
  "An implementation of the Naive Bayes classification technique,
using the multivariate Bernoulli model."
  (:use [nerchuko utils helpers]
        nerchuko.text.helpers)
  (:use [clojure.set]
        [clojure.contrib.seq-utils :only (frequencies)]
        [clojure.contrib.generic math-functions functor]))

(defmulti
  #^{:arglists '([doc])
     :doc "Converts the given doc to a Set of features.

How the Set is created depends on what doc is:

If doc is a Collection, each distinct element of the collection is
considered a feature.

If doc is a String, it is tokenized and the tokens are considered as
the features. Uses nerchuko.text.helpers/set-of-words.

If doc is a Map, it is treated as an attreibute map and the keys are
considered as the features. The values must all be numbers; otherwise,
an IllegalArgumentException is thrown.

If doc is anything else, an IllegalArgumentException is thrown."}
  prepare-doc type)

(defmethod prepare-doc java.util.Set
  [x] x)

(defmethod prepare-doc java.util.Collection
  [coll]
  (set coll))

(defmethod prepare-doc String
  [s]
  (set-of-tokens s))

(defmethod prepare-doc java.util.Map
  [m]
  (if (every? number? (vals m))
    (->> m
       keys
       set)
    (throw (IllegalArgumentException.
            "If document is a map, all the vals must be numbers."))))

(defmethod prepare-doc :default
  [x]
  (throw (IllegalArgumentException.
          "Document must be one of: String, Collection, Map.")))

(defn- aggregate
  "Returns the aggregate information gleaned from the training dataset.

The aggregate information is returned as a map:
:classes, all the classes in the training dataset, as a hash-set.
:features, all the features in the training dataset, as a hash-set.
:classes-counts, the number of documents belonging to each class,
  as a map { classes => counts }.
:classes-features-counts, for each class and feature, the number of
  documents of that class in which the feature is present,
  as a map { classes => { features => counts } }."
  [training-dataset]
  (zipmap [:classes :features :classes-counts :classes-features-counts]
       (reduce (fn [[classes features classes-counts classes-features-counts] [doc cls]]
                 [(conj classes cls)
                  (into features doc)
                  (merge-with-+ classes-counts {cls 1})
                  (merge-with merge-with-+
                              classes-features-counts
                              {cls (frequencies doc)})])
               [#{} #{} {} {}]
               training-dataset)))

(defn- class-cond-probs
  "Returns the conditional probabilities of each feature, given a class,
as a map { classes => { features => probabilities } }

The conditional probability is the number of documents of the class in
which the feature occurs divided by the number of documents
belonging to the class. To avoid zero values, 1-smoothing is used."
  [features class-count class-features-counts]
  (let [denominator (+ class-count 2)]
    (zipmap features
            (map (fn [feature]
                   (/ (inc (get class-features-counts
                                feature 0))
                      denominator))
                 features))))

(defn learn-model
  "Returns the classifier model learned from the training-dataset.

The training-dataset must be a seq of training examples each of which
must be a 2-item vector of the document and the correct class of that
document. prepare-doc will be called on each document.

The model is a map with the following keys:
:classifier, a symbol naming this namespace.
:classes, a hash-set of all the classes.
:features, a hash-set of all the features.
:priors, prior probabilities for each class.
:cond-probs, conditional probabilities (probability of feature given
  class) for each class for each feature."
  [training-dataset]
  (let [prepared-dataset        (map-firsts prepare-doc training-dataset)
        aggregate-info          (aggregate prepared-dataset)
        classes                 (:classes aggregate-info)
        features                (:features aggregate-info)
        classes-counts          (:classes-counts aggregate-info)
        classes-features-counts (:classes-features-counts aggregate-info)
        docs-total              (reduce + (vals classes-counts))]
    (struct-map model
      :classifier 'nerchuko.classifiers.naive-bayes.bernoulli
      :classes    classes
      :features   features
      :priors     (fmap #(/ % docs-total) classes-counts)
      :cond-probs (->> classes-features-counts
                       (map (fn [[cls class-features-counts]]
                              (vector cls
                                      (class-cond-probs features
                                                        (classes-counts cls)
                                                        class-features-counts))))
                       (reduce merge {})))))

(defn- log-likelihood
  "Given a doc and the conditional probabilities of the features for a class,
returns the log of the likelihood that the document belongs to the
class."
  [doc class-cond-probs]
  (->> class-cond-probs
       (map (fn [[feature prob]]
             (if (doc feature)
               prob
               (- 1 prob))))
       (map log)
       (reduce +)))

(defn- estimates
  "Returns the log of the product of the prior probabilities of each
class and the likelihood of the document belonging to that class."
  [model doc]
  (let [classes (:classes model)
        features (:features model)
        priors (:priors model)
        cond-probs (:cond-probs model)]
    (into {}
          (map (fn [[cls prior]]
                 (vector cls
                         (+ (log prior)
                            (log-likelihood doc (cond-probs cls)))))
               priors))))

(defn scores
  "Returns the a posteriori probabilities of the document
belonging to each class, estimated using the Naive Bayes
classifier over model."
  [model doc]
  (let [es (->> doc
                prepare-doc
                (estimates model)
                (fmap exp))]
    (zipmap (keys es)
            (to-probabilities (vals es)))))

(defn classify
  "Classifies the doc using a Naive Bayes classifier based on model.
Returns the most probable class."
  [model doc & [default-class]]
  (let [classes-probabilities (scores model doc)]
    (key-with-max-val classes-probabilities default-class)))

(defn classifier
  "Returns (partial classify model).
The return value can be used as a classifier function that classifies
a given doc."
  [model]
  (fn [doc]
    (classify model doc)))
