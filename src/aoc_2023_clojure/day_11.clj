(ns aoc-2023-clojure.day-11
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-line [idx line]
  (keep-indexed #(if (= \# %2) (vector idx %1) nil) line))

(defn- is-empty-column? [galaxies j]
  (empty? (filter #(= j (second %)) galaxies)))

(defn- is-empty-row? [galaxies i]
  (empty? (filter #(= i (first %)) galaxies)))

(defn- parse-input [input]
  (let [galaxies (mapcat parse-line (range) (str/split-lines input))
        is (map first galaxies)
        js (map second galaxies)
        empty-rows (set (filter #(is-empty-row? galaxies %) (range (reduce min js) (inc (reduce max js)))))
        empty-columns (set (filter #(is-empty-column? galaxies %) (range (reduce min is) (inc (reduce max is)))))]
    {:galaxies galaxies
     :empty-rows empty-rows
     :empty-columns empty-columns}))

(defn- count-empty-between [row-or-column universe x1 x2]
  (let [[x1 x2] (sort [x1 x2])
        pred-fn (if (= row-or-column :row) (:empty-rows universe) (:empty-columns universe))]
    (count (filter pred-fn (range x1 (inc x2))))))

(defn- distance [universe x y]
  (let [empty-rows (count-empty-between :row universe (first x) (first y))
        empty-columns (count-empty-between :column universe (second x) (second y))
        factor (dec (:factor universe))]
    (+ (reduce + (pmap #(abs (- %1 %2)) x y))
       (* factor empty-rows)
       (* factor empty-columns))))

(defn- sum-of-distances [universe]
  (->>
   (for [x (:galaxies universe)
         y (:galaxies universe)
         :when (= -1 (compare x y))]
     (distance universe x y))
   (reduce +)))

(defn main
  ([factor] (main factor (slurp (io/resource "11.txt"))))
  ([factor input]
   (-> input
       parse-input
       (assoc :factor factor)
       sum-of-distances)))

(defn a
  ([] (main 2))
  ([input] (main 2 input)))

(defn b
  ([] (main 1000000))
  ([input] (main 1000000 input)))
