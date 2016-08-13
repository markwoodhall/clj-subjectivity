(ns clj-subjectivity.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [clj-subjectivity.core :refer [sentiment]]))

(deftest sentiment-test
  (testing "test sentiment with weak negative word returns correct result."
    (let [expected {:difference -0.5
                    :negative 0.5
                    :neutral 0
                    :positive 0
                    :top-negative '("fire")
                    :top-neutral '()
                    :top-positive '()
                    :bottom-negative '("fire")
                    :bottom-neutral '()
                    :bottom-positive '()}
          actual (sentiment ["fire"])]
      (is (= expected actual))))

  (testing "test sentiment with strong negative word returns correct result."
    (let [expected {:difference -1
                    :negative 1
                    :neutral 0
                    :positive 0
                    :top-negative '("filthy")
                    :top-neutral '()
                    :top-positive '()
                    :bottom-negative '("filthy")
                    :bottom-neutral '()
                    :bottom-positive '()}
          actual (sentiment ["filthy"])]
      (is (= expected actual))))

  (testing "test sentiment with weak positive word returns correct result."
    (let [expected {:difference 0.5
                    :negative 0
                    :neutral 0
                    :positive 0.5
                    :top-negative '()
                    :top-neutral '()
                    :top-positive '("fine")
                    :bottom-negative '()
                    :bottom-neutral '()
                    :bottom-positive '("fine")}
          actual (sentiment ["fine"])]
      (is (= expected actual))))

  (testing "test sentiment with strong positive word returns correct result."
    (let [expected {:difference 1
                    :negative 0
                    :neutral 0
                    :positive 1
                    :top-negative '()
                    :top-neutral '()
                    :top-positive '("happy")
                    :bottom-negative '()
                    :bottom-neutral '()
                    :bottom-positive '("happy")}
          actual (sentiment ["happy"])]
      (is (= expected actual))))

  (testing "test sentiment with a selection of words."
    (let [expected {:difference 0.0
                    :negative 1.5
                    :neutral 0
                    :positive 1.5
                    :top-negative '("filthy" "fire")
                    :top-neutral '()
                    :top-positive '("fine" "happy")
                    :bottom-negative '("fire" "filthy")
                    :bottom-neutral '()
                    :bottom-positive '("happy" "fine")}
          actual (sentiment ["happy" "fine" "fire" "filthy"])]
      (is (= expected actual)))))
