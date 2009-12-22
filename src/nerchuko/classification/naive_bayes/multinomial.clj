(ns nerchuko.classification.naive-bayes.multinomial
  (:use nerchuko.helpers)
  (:use [clojure.set :only (intersection)]
        clojure.contrib.generic.math-functions
        clojure.contrib.generic.functor))

(defn- aggregate
  "Returns the aggregate information gleaned from the training data.

The aggregate information is returned as a vector of:
- all the classes in the training data, as a hash-set.
- all the features in the training data, as a hash-set.
- the number of documents belonging to each class, as a map { classes => counts }.
- features counts for each class, as a map { classes => { features => counts } }."
  [training-dataset]
  (reduce (fn [[classes features classes-counts features-classes-counts] [doc cls]]
            [(conj classes cls)
             (into features (keys doc))
             (merge-with-+ classes-counts {cls 1})
             (merge-with merge-with-+ features-classes-counts {cls doc})])
          [#{} #{} {} {}]
          training-dataset))

(defn- conditional-probability
  "Returns the conditional probabilities of a feature for each class."
  [feature-classes-counts features-total]
  (let [feature-classes-total (reduce + (vals feature-classes-counts))
        denominator (+ feature-classes-total features-total)]
    (fmap #(/ (inc %) denominator) feature-classes-counts)))

(defn learn-model
  "Returns the classifier model learned from the training-dataset.

The training data must be a sequence of training examples each of which
must be a 2-item vector of the document and the corresponding class.

The model is a map with the following keys:
:classes, the hash-set of all the classes.
:features, the hash-set of all the features.
:priors, prior probabilities for each class.
:cond-probs, conditional probabilities for each class for each feature."
  [training-dataset]
  (let [[classes features classes-counts features-classes-counts] (aggregate training-dataset)
        docs-total (reduce + (vals classes-counts))
        features-total (count features)
        features-0s (fmap dec (counts features))]
    {:classes classes
     :features features
     :priors (fmap #(/ % docs-total) classes-counts)
     :cond-probs (fmap (fn [feature-classes-counts]
                         (conditional-probability (merge features-0s feature-classes-counts)
                                                  features-total))
                       features-classes-counts)}))

(defn- likelihood
  "Given a doc and the conditional probabilities of the features for a class,
returns the likelihood that the document belongs to the class.
Essentially, this returns a product of the conditional probabilities of the
features that appear in the doc."
  [doc class-cond-probs]
  (reduce (fn [class-likelihood [feature feature-count]]
            (* class-likelihood (pow (class-cond-probs feature) feature-count)))
          1
          doc))

(defn probabilities
  "Returns the a posteriori probabilities of the document
belonging to each class, estimated using the Naive Bayes
classifier over model."
  [model doc]
  (let [classes (:classes model)
        features (:features model)
        priors (:priors model)
        cond-probs (:cond-probs model)
        doc (select-keys doc features)]
    (zipmap classes
            (to-probabilities
             (map (fn [class]
                    (let [prior (priors class)
                          class-likelihood (likelihood doc (cond-probs class))]
                      (* prior class-likelihood)))
                  classes)))))

(defn classify
  "Classifies the doc using a Naive Bayes classifier based on model.
Returns the most probable class."
  [model doc]
  (let [classes-probabilities (probabilities model doc)]
    (key-with-max-val classes-probabilities)))

(defn classifier
  "Returns (partial classify model).
The return value can be used as a classifier function that classifies
a given doc."
  [model]
  (partial classify model))
