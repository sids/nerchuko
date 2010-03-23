(defproject nerchuko "0.1.3"
  :description "Machine Learning in Clojure."
  :url "http://github.com/sids/nerchuko"
  :namespaces [nerchuko.classification
               nerchuko.feature-selection
               nerchuko.text-utils
               nerchuko.helpers]
  :dependencies [[org.clojure/clojure "1.1.0-master-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.0-SNAPSHOT"]
                 [text.clj "0.0.1"]]
  :dev-dependencies [[leiningen/lein-swank "1.1.0"]
                     [lein-clojars "0.5.0-SNAPSHOT"]])
