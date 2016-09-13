(defproject clj-subjectivity "0.8.0"
  :description "Library wrapping the subjectivity lexicon from http://mpqa.cs.pitt.edu/"
  :url "https://github.com/markwoodhall/clj-subjectivity"
  :license {:name "The MIT License."
            :url "https://opensource.org/licenses/MIT"}
  :codox {:metadata {:doc/format :markdown}
          :namespaces [clj-subjectivity.core]
          :source-uri "https://github.com/markwoodhall/clj-subjectivity/blob/master/src/{classpath}#L{line}"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :jar-exclusions [#"\.swp|\.swo|user.clj"]
  :source-paths ["src"])
