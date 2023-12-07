(ns aoc-2023-clojure.day-07
  (:require
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(defn- parse-line [line]
  (let [[hand bid] (str/split line #" ")]
    {:hand (str/split hand #"")
     :bid (parse-long bid)}))

(defn- parse-input [input]
  (map parse-line (str/split-lines input)))

(def hand-type->number
  (zipmap
   (reverse [:five-of-a-kind :four-of-a-kind :full-house :three-of-a-kind :two-pair :one-pair :high-card])
   (range)))

(def card-type->number
  (zipmap
   (reverse ["A" "K" "Q" "J" "T" "9" "8" "7" "6" "5" "4" "3" "2"])
   (range)))

(defn- hand-type [hand]
  (let [counts (frequencies (vals (frequencies hand)))]
    (cond (= 1 (get counts 5)) :five-of-a-kind
          (= 1 (get counts 4)) :four-of-a-kind
          (and (= 1 (get counts 3))
               (= 1 (get counts 2))) :full-house
          (= 1 (get counts 3)) :three-of-a-kind
          (= 2 (get counts 2)) :two-pair
          (= 1 (get counts 2)) :one-pair
          :else :high-card)))

(defn- cmp-fn [x y]
  (let [opted (contains? x :optimized)
        card-type->number (if opted (assoc card-type->number "J" -1) card-type->number)
        hands (map #(if opted (:optimized %) (:hand %)) [x y])
        [type-score-x type-score-y] (map (comp hand-type->number hand-type) hands)
        type-scores-compared (compare type-score-x type-score-y)]
    (cond (zero? type-scores-compared) (compare (mapv card-type->number (:hand x))
                                                (mapv card-type->number (:hand y)))
          :else type-scores-compared)))

(defn- total-winnings [hands-and-bids]
  (let [s (sort-by identity cmp-fn hands-and-bids)]
    (reduce + (map #(* (:bid %1) %2) s (map inc (range))))))

(defn a
  ([] (a (slurp (io/resource "07.txt"))))
  ([input] (total-winnings (parse-input input))))

(defn- most-valueable-card [hand]
  (if (zero? (count hand))
    "A"
    (let [fs (frequencies hand)
          max-f (apply max (vals fs))]
      (->> (filter #(= max-f (second %)) fs)
           (map first)
           (map #(vector % (card-type->number %)))
           (sort-by second)
           reverse
           ffirst))))

(defn- optimize [{hand :hand bid :bid}]
  (let [hand-without-jokers (filter #(not= "J" %) hand)
        mvc (most-valueable-card hand-without-jokers)]
    {:optimized (replace {"J" mvc} hand)
     :hand hand
     :bid bid}))

(defn b
  ([] (b (slurp (io/resource "07.txt"))))
  ([input] (->> (parse-input input)
                (map optimize)
                total-winnings)))
