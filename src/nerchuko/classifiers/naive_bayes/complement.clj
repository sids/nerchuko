(ns nerchuko.classifiers.naive-bayes.complement
  "An implementation of the Complement Naive Bayes classification technique.

This classifer works with numeric features map. If given documents are
in any other representation, they are converted to a numeric features
map using prepare-doc."
  (:use [nerchuko utils helpers]
        nerchuko.text.helpers)
  (:use [clojure.set :only (intersection)]
        [clojure.contrib.seq-utils :only (frequencies)]
        [clojure.contrib.generic math-functions functor]
        clojure.contrib.pprint))

(defn prepare-doc
  "Converts doc to a numeric features map by calling
nerchuko.helpers/numeric-features-map."
  [doc]
  (numeric-features-map doc))

(defn aggregate
  "Returns the aggregate information gleaned from the training dataset.

The aggregate information is returned as a map:
:classes, all the classes in the training data, as a hash-set.
:features, all the features in the training data, as a hash-set.
:classes-counts, the number of documents belonging to each class,
  as a map { classes => counts }.
:classes-features-counts, for each class and feature, the number of
  times the feature occurs in documents of that class (including
  multiple occurrences), as a map { classes => { features => counts } }."
  [training-dataset]
  (zipmap [:classes :features :classes-counts :features-counts :classes-features-counts]
          (reduce (fn [[classes features classes-counts features-counts classes-features-counts]
                       [doc cls]]
                    (vector (conj classes cls)
                            (into features (keys doc))
                            (merge-with-+ classes-counts {cls 1})
                            (merge-with-+ features-counts doc)
                            (merge-with merge-with-+
                                        classes-features-counts
                                        {cls doc})))
                  [#{} #{} {} {} {}]
                  training-dataset)))

(defn class-cond-probs
  "Returns the conditional probabilities of each feature, given a class,
as a map { classes => { features => probabilities } }.

The coditional probability is the number of occurrences of the feature
within documents of the class divided by the total number of
occurrences of all the features within documents of the class. To
avoid zero values, 1-smoothing is used."
  [features-counts features-total class-features-counts]
  (let [class-features-total (reduce + (vals class-features-counts))
        num-of-features      (count features-counts)
        denominator          (+ (- features-total class-features-total)
                                num-of-features)]
    (reduce merge {}
            (map (fn [[feature feature-count]]
                   [feature (/ (inc (- feature-count
                                       (get class-features-counts
                                            feature 0)))
                               denominator)])
                 features-counts))))

(defn learn-model
  "Returns the classifier model learned from the training-dataset.

The training data must be a sequence of training examples each of
which must be a 2-item vector of the document and the corresponding
class.

The model is a map with the following keys:
:classifier, a symbol naming this namespace.
:classes, the hash-set of all the classes.
:features, the hash-set of all the features.
:priors, prior probabilities for each class.
:cond-probs, conditional probabilities for each class for each feature."
  [training-dataset]
  (let [prepared-dataset        (map-firsts prepare-doc training-dataset)
        aggregate-info          (aggregate prepared-dataset)
        classes                 (:classes aggregate-info)
        features                (:features aggregate-info)
        classes-counts          (:classes-counts aggregate-info)
        features-counts         (:features-counts aggregate-info)
        classes-features-counts (:classes-features-counts aggregate-info)
        docs-total              (reduce + (vals classes-counts))
        features-total          (reduce + (vals features-counts))]
    (struct-map model
      :classifier 'nerchuko.classifiers.naive-bayes.complement
      :classes    classes
      :features   features
      :priors     (fmap #(/ % docs-total) classes-counts)
      :cond-probs (fmap (partial class-cond-probs features-counts features-total)
                        classes-features-counts))))

(defn- log-likelihood
  "Given a doc and the conditional probabilities of the features for a class,
returns the log of the likelihood that the document belongs to the
class.  Essentially, this returns log of the product of the
conditional probabilities of the features that appear in the doc."
  [doc class-cond-probs]
  (->> doc
       (map (fn [[feature feature-count]]
              (pow (class-cond-probs feature) feature-count)))
       (map log)
       (reduce +)))

(defn- estimates
  "Returns the log of the product of the prior probabilities of each
class and the likelihood of the document belonging to that class."
  [model doc]
  (let [classes    (:classes model)
        features   (:features model)
        priors     (:priors model)
        cond-probs (:cond-probs model)
        doc        (select-keys doc features)]
    (zipmap classes
            (map (fn [class]
                   (- (log (priors class))
                      (log-likelihood doc (cond-probs class))))
                 classes))))

(defn scores
  "Returns the a posteriori probabilities of the document belonging to
each class, estimated using the Naive Bayes classifier over model."
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
  (-> (scores model doc)
      (key-with-max-val default-class)))

(defn classifier
  "Returns (partial classify model).
The return value can be used as a classifier function that classifies
a given doc."
  [model]
  (fn [doc]
    (classify model doc)))
