(ns clj-subjectivity.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [clj-subjectivity.core :refer [sentiment]]))

(deftest sentiment-test
  (testing "test sentiment wth negative work returns correct result."
    (let [expected {:difference -0.5,
                    :negative 0.5,
                    :neutral 0,
                    :positive 0,
                    :top-negative '("fire"),
                    :top-neutral '(),
                    :top-positive '()}
          actual (sentiment ["fire"])]
      (is (= expected actual)))))
