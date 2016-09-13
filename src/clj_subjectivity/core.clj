(ns clj-subjectivity.core
  "A Clojure wrapper around a subjectivity lexicon [from here](http://mpqa.cs.pitt.edu/)"
  {:author "Mark Woodhall"}
  (:require [clojure.java.io :as io]
            [clojure.string :refer [split lower-case]]))

(def subjectivity-clues
  (memoize #(read-string
             (slurp (io/resource "subjectivityclues.edn")))))

(def negations
  (memoize #(read-string
             (slurp (io/resource "negations.edn")))))

(defn- word-sentiment
  ([word]
   (word-sentiment word false))
  ([word negate?]
     (let [{:keys [priorpolarity type]} (first (filter #(= (:word1 %) word) (subjectivity-clues)))
           score (if (not (nil? priorpolarity))
                   (if (= type :strongsubj)
                     1
                     0.5)
                   0)]
       {:sentiment (if (and (= priorpolarity :positive)
                            negate?)
                     :negative
                     priorpolarity)
        :score score
        :word word})))

(def ^:private word-sentiment-memo
  (memoize word-sentiment))

(defn- words-sentiment
  [words]
  (let [last-sentiment (atom {:word ""})]
    (for [word words]
      (let [negate? (some #{(lower-case (:word @last-sentiment))} (negations))
            sentiment (word-sentiment-memo word negate?)
            sentiment (if (= (:sentiment @last-sentiment) (:sentiment sentiment))
                        (update-in sentiment [:score] + 0.5)
                        sentiment)]
        (reset! last-sentiment sentiment)
        sentiment))))

(defn sentiment
  "Given a collection of words or a function returning a collection of words will
  return a map representing the overall sentiment for the words."
  [words-or-func]
  (let [words (if (fn? words-or-func)
                (words-or-func)
                words-or-func)
        words (if (sequential? words)
                words
                (split words #"\ "))
        sentiments (words-sentiment words)
        pos-sents (filter #(= (:sentiment %) :positive) sentiments)
        neg-sents (filter #(= (:sentiment %) :negative) sentiments)
        neu-sents (filter #(= (:sentiment %) :neutral) sentiments)
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
