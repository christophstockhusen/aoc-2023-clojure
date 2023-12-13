(ns aoc-2023-clojure.day-13
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-pattern [input]
  (->> (str/split-lines input)
       (map (fn [idx line] (map #(vector [idx %1] %2) (range) line)) (range))
       (apply concat)
       (into {})))

(defn- parse-input [input]
  (map parse-pattern (str/split input #"\n\n")))

(defn- mismatches-in-reflection-h [pattern i]
  (let [max-j (reduce max (map #(second (first %)) pattern))]
    (->> (for [i' (range (inc i))
               j (range (inc max-j))
               :when (<= i' i)]
           [[i' j] [(+ i (- i i') 1) j]])
         (map #(vector (get pattern (first %))
                       (get pattern (second %))))
         (filter #(and (some? (first %))
                       (some? (second %))))
         (filter #(not= (first %) (second %)))
         count)))

(defn- mismatches-in-reflection-v [pattern j]
  (let [max-i (reduce max (map #(first (first %)) pattern))]
    (->> (for [j' (range (inc j))
               i (range (inc max-i))
               :when (<= j' j)]
           [[i j'] [i (+ j (- j j') 1)]])
         (map #(vector (get pattern (first %))
                       (get pattern (second %))))
         (filter #(and (some? (first %))
                       (some? (second %))))
         (filter #(not= (first %) (second %)))
         count)))

(defn- find-line-of-reflection [dim smudge pattern]
  (let [smudge-cnt (if smudge 1 0)
        project-fn (if (= dim :horizontal) ffirst #(second (first %)))
        mismatch-fn (if (= dim :horizontal) mismatches-in-reflection-h mismatches-in-reflection-v)
        max-i (apply max (map #(project-fn %) pattern))
        lines (range max-i)]
    (first (filter #(= smudge-cnt (mismatch-fn pattern %)) lines))))

(defn- score [smudge pattern]
  (if-let [l (find-line-of-reflection :horizontal smudge pattern)]
    (* 100 (inc l))
    (inc (find-line-of-reflection :vertical smudge pattern))))

(defn- summarize [smudge patterns]
  (reduce + (map (partial score smudge) patterns)))

(defn a
  ([] (a (slurp (io/resource "13.txt"))))
  ([input] (summarize false (parse-input input))))

(defn b
  ([] (b (slurp (io/resource "13.txt"))))
  ([input] (summarize true (parse-input input))))

