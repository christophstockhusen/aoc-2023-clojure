(ns aoc-2023-clojure.day-04
  (:require
   [clojure.java.io :as io]
   [clojure.math :as math]
   [clojure.string :as str]))

(defn- parse-line [line]
  (let [id (parse-long (re-find #"\d+" line))
        without-card (second (str/split line #":."))
        numbers (str/split without-card #"\|")
        numbers (->> (map #(re-seq #"\d+" %) numbers)
                     (map #(map parse-long %)))]
    {:id id
     :scratched-numbers (first numbers)
     :winning-numbers (second numbers)}))

(defn- parse-input [input]
  (map parse-line (str/split-lines input)))

(defn- scratched-winning-numbers [{winning-numbers :winning-numbers scratched-numbers :scratched-numbers}]
  (filter (set winning-numbers) (set scratched-numbers)))

(defn- points-card [card]
  (int (math/pow 2 (dec (count (scratched-winning-numbers card))))))

(defn- points-cards [input]
  (reduce + (map points-card input)))

(defn a
  ([] (a (slurp (io/resource "04.txt"))))
  ([input] (points-cards (parse-input input))))

(def matches
  (memoize (fn [card] (count (scratched-winning-numbers card)))))

(defn- next-cards [cards-map card]
  (->> (range (matches card))
       (map inc)
       (map #(+ (:id card) %))
       (map #(get cards-map %))))

(defn- total-scratchcard-count [cards]
  (let [cards-map (into {} (map #(vector (:id %) %) cards))]
    (loop [q (into (clojure.lang.PersistentQueue/EMPTY) cards)
           seen []]
      (if (empty? q)
        (count seen)
        (let [current-card (peek q)]
          (recur (into (pop q) (next-cards cards-map current-card))
                 (conj seen current-card)))))))

(defn b
  ([] (b (slurp (io/resource "04.txt"))))
  ([input] (total-scratchcard-count (parse-input input))))
