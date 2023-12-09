(ns aoc-2023-clojure.day-09
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-line [line]
  (mapv parse-long (re-seq #"-?\d+" line)))

(defn- parse-input [input]
  (mapv parse-line (str/split-lines input)))

(defn- differences [numbers]
  (if (= 1 (count numbers))
    numbers
    (mapv #(- %2 %1) numbers (rest numbers))))

(defn- differences-seq [numbers]
  (loop [current numbers
         collected-diffs []]
    (let [current-diffs (differences current)
          collected-diffs (conj collected-diffs current-diffs)]
      (if (every? zero? current)
        collected-diffs
        (recur current-diffs collected-diffs)))))

(defn- extrapolate-last [numbers]
  (let [diffs (differences-seq numbers)
        data (into [numbers] diffs)]
    (reduce + (map last data))))

(defn- extrapolate-first [numbers]
  (let [diffs (differences-seq numbers)
        data (into [numbers] diffs)
        firsts (map first data)
        new-first (reduce #(- %2 %1) (reverse firsts))]
    new-first))

(defn main
  ([] (main (slurp (io/resource "09.txt"))))
  ([input]
   (let [parsed (parse-input input)
         new-firsts (map extrapolate-first parsed)
         new-lasts (map extrapolate-last parsed)]
     (map (partial reduce +) [new-firsts new-lasts]))))

(defn a
  ([] (second (main)))
  ([input] (second (main input))))

(defn b
  ([] (first (main)))
  ([input] (first (main input))))
