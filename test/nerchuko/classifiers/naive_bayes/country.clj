(ns test.nerchuko.classifiers.naive-bayes.country
  (:use nerchuko.classifiers.naive-bayes
        nerchuko.helpers))

(def training-set [[[:chinese :beijing :chinese] :yes]
                   [[:chinese :chinese :shanghai] :yes]
                   [[:chinese :macao] :yes]
                   [[:tokyo :japan :chinese] :no]])

(defn- transform [doc]
  (counts doc))

(def model (generate-model (map-on-firsts transform training-set)))

(def unclassified-doc [:chinese :chinese :chinese :tokyo :japan])
(println (classify model (transform unclassified-doc)))
(println (classify model {}))
