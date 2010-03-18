(ns nerchuko.feature-selectors.chi-squared
  (:use nerchuko.utils)
  (:use clojure.contrib.generic.math-functions
        clojure.contrib.generic.functor))

(defn- aggregate [training-dataset]
  (->> training-dataset
       (reduce (fn [[docs-count classes-counts features-counts features-classes-counts]
               [doc class]]
            (let [doc-features (keys doc)]
              [(inc docs-count)
               (merge-with-+ classes-counts {class 1})
               (merge-with-+ features-counts (counts doc-features))
               (reduce (partial merge-with merge-with-+)
                       features-classes-counts
                       (map (fn [feature]
                              {feature {class 1}})
                            doc-features))]))
          [0 {} {} {}]))) 

(defn- expected-count [row-count column-count docs-count]
  (* docs-count
     (/ row-count docs-count)
     (/ column-count docs-count)))

(defn- chi-squared-term [observed-count expected-count]
  (if-not (zero? expected-count)
    (/ (pow (- observed-count expected-count) 2) expected-count)
    0))

(defn- feature-class-chi-squared [feature-class-count class-count feature-count docs-count]
  (+
   (chi-squared-term feature-class-count
                     (expected-count class-count
                                     feature-count
                                     docs-count))
   (chi-squared-term (- class-count feature-class-count)
                     (expected-count class-count
                                     (- docs-count feature-count)
                                     docs-count))))

(defn- features-chi-squareds [[docs-count classes-counts features-counts features-classes-counts]]
  (let [docs-count (reduce + (vals classes-counts))]
    (reduce merge
            (map (fn [[feature feature-count]]
                   {feature
                    (reduce +
                            (map (fn [[class class-count]]
                                   (-> features-classes-counts
                                       (get feature {})
                                       (get class 0)
                                       (feature-class-chi-squared class-count
                                                                  feature-count
                                                                  docs-count)))
                                 classes-counts))})
                 features-counts))))

(defn select [k training-dataset]
  (->> training-dataset
       aggregate
       features-chi-squareds
       (largest-n-by-vals k)
       keys
       set))
