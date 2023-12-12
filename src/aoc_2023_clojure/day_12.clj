(ns aoc-2023-clojure.day-12
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-line [line]
  (let [[pattern groups] (str/split line #" ")]
    {:pattern pattern
     :groups (map parse-long (str/split groups #","))}))

(defn- parse-input [input]
  (map parse-line (str/split-lines input)))

(def count-arrangements
  (memoize
   (fn [pattern groups]
     (cond
       (and (= 1 (count groups))
            (re-matches #"[#?]+" pattern)
            (= (count pattern) (first groups))) 1
       (and (empty? groups) (re-matches #"[?.]*" pattern)) 1
       (and (empty? groups) (re-matches #".*#.*" pattern)) 0
       :else
       (+ (if (re-matches (re-pattern (str "[#?]{" (first groups) "}[.?].*")) pattern)
            (count-arrangements (subs pattern (inc (first groups))) (rest groups))
            0)
          (if (re-matches #"[?.].*" pattern)
            (count-arrangements (subs pattern 1) groups)
            0))))))

(defn a
  ([] (a (slurp (io/resource "12.txt"))))
  ([input]
   (->> (parse-input input)
        (map #(count-arrangements (:pattern %) (:groups %)))
        (reduce +))))

(defn- unfold [{pattern :pattern groups :groups}]
  {:pattern (str/join "?" (repeat 5 pattern ))
   :groups (apply concat (repeat 5 groups))})

(defn b
  ([] (b (slurp (io/resource "12.txt"))))
  ([input]
   (->> (parse-input input)
        (map unfold)
        (map #(count-arrangements (:pattern %) (:groups %)))
        (reduce +))))
