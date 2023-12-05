(ns aoc-2023-clojure.day-02
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-round [round]
  (let [tokens (re-seq #"\d+|[a-z]+" round)
        numbers (map parse-long (flatten (partition 1 2 tokens)))
        colors (flatten (partition 1 2 (rest tokens)))]
    (zipmap colors numbers)))

(defn- parse-line [line]
  (mapv parse-round (str/split (second (str/split line #": ")) #";")))

(defn possible-round? [round]
  (let [bag {"red" 12 "green" 13 "blue" 14}]
    (every? #(<= (get round % 0) (get bag %)) (keys bag))))

(defn possible-game? [game]
  (every? possible-round? (second game)))

(defn parse-input [input]
  (->> (map parse-line (str/split-lines input))
       (zipmap (rest (range)))))

(defn a
  ([] (a (slurp (io/resource "02.txt"))))
  ([input]
   (let [games (parse-input input)]
     (->> (filter possible-game? games)
          (map first)
          (reduce +)))))

(defn min-power [game]
  (let [rounds (second game)]
    (->> (apply merge-with max rounds)
         (map second)
         (reduce *))))

(defn b
  ([] (b (slurp (io/resource "02.txt"))))
  ([input]
   (let [games (parse-input input)]
     (->> (map min-power games)
          (reduce +)))))
