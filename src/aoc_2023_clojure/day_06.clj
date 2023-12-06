(ns aoc-2023-clojure.day-06
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def input "Time:      7  15   30
Distance:  9  40  200")

(defn- parse-input [part input]
  (if (= 1 part)
    (let [numbers (map parse-long (re-seq #"\d+" input))
          [times distances] (split-at (/ (count numbers) 2) numbers)]
      (map #(hash-map :time %1 :record %2) times distances))
    (let [digit-lines (map (fn [l] (re-seq #"\d" l)) (str/split-lines input))
          numbers (map parse-long (map str/join digit-lines))]
      [{:time (first numbers)
        :record (second numbers)}])))

(defn- distance [charged max-time]
  (* charged (- max-time charged)))

(defn- max-distances [time]
  (map #(distance % time) (range time)))

(defn- margin-of-error [races]
  (->> (map max-distances (map :time races))
       (map (fn [r ds] (filter #(< (:record r) %) ds)) races)
       (map count)
       (reduce *)))

(defn a
  ([] (a (slurp (io/resource "06.txt"))))
  ([input] (margin-of-error (parse-input 1 input))))

(defn b
  ([] (b (slurp (io/resource "06.txt"))))
  ([input] (margin-of-error (parse-input 2 input))))
