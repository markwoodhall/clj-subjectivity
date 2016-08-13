(ns clj-subjectivity.core
  {:author "Mark Woodhall"}
  (:require [clojure.java.io :as io]))

(def subjectivity-clues
  (memoize #(read-string
             (slurp (io/resource "subjectivityclues.edn")))))

(defn- word-sentiment
  [word]
  (let [{:keys [priorpolarity type]} (first (filter #(= (:word1 %) word) (subjectivity-clues)))]
    {:sentiment priorpolarity
     :score (if (not (nil? priorpolarity))
              (if (= type :strongsubj)
                1
                0.5)
              0)
     :word word}))

(defn sentiment
  [words]
  (let [sentiments (map word-sentiment words)
        pos-sents (filter #(= (:sentiment %) :positive) sentiments)
        neg-sents (filter #(= (:sentiment %) :negative) sentiments)
        neu-sents (filter #(or (= (:sentiment %) :neutral)
                               (nil? (:sentiment %))) sentiments)
        pos (reduce + (map #(:score %) pos-sents))
        neg (reduce + (map #(:score %) neg-sents))
        neu (reduce + (map #(:score %) neu-sents))
        pos-words (map #(:word %) pos-sents)
        neu-words (map #(:word %) neu-sents)
        neg-words (map #(:word %) neg-sents)
        p-frequent (map key (take 10 (reverse (sort-by val (frequencies pos-words)))))
        p-rare (map key (take 10 (sort-by val (frequencies pos-words))))
        n-frequent (map key (take 10 (reverse (sort-by val (frequencies neg-words)))))
        n-rare (map key (take 10 (sort-by val (frequencies neg-words))))
        neu-frequent (map key (take 10 (reverse (sort-by val (frequencies neu-words)))))
        neu-rare (map key (take 10 (sort-by val (frequencies neu-words))))]
    {:positive pos
     :negative neg
     :neutral neu
     :top-positive (distinct p-frequent)
     :top-negative (distinct n-frequent)
     :top-neutral  (distinct neu-frequent)
     :bottom-positive (distinct p-rare)
     :bottom-negative (distinct n-rare)
     :bottom-neutral  (distinct neu-rare)
     :difference (- pos neg)}))
