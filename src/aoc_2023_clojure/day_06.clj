(ns aoc-2023-clojure.day-06
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.math :as math]))

(defn- parse-input [part input]
  (cond
    (= 1 part)
    (let [numbers (map parse-long (re-seq #"\d+" input))
          [times distances] (split-at (/ (count numbers) 2) numbers)]
      (map #(hash-map :racing-time %1 :record-distance %2) times distances))

    :else
    (let [digit-lines (map #(re-seq #"\d" %) (str/split-lines input))
          [time record] (map (comp parse-long str/join) digit-lines)]
      [{:racing-time time :record-distance record}])))

;; Distance"" charged * (max-time - charged) = - charged^2 + max-time * charged
;; We search for natural numbers with - charged^2 + max-time * charged > record, i.e.
;; natural numbers _between_ the roots of - charged^2 + max-time * charged - record = 0
;; or charged^2 - max-time * charged + record = 0

(defn- roots [p q]
  (let [r (math/sqrt (- (math/pow (/ p 2) 2) q))]
    [(- (- (/ p 2)) r) (+ (- (/ p 2)) r)]))

(defn- ugly-floor [x]
  (math/floor (- x 0.000001)))

(defn- ugly-ceil [x]
  (math/ceil (+ x 0.000001)))

(defn- winning-options [{racing-time :racing-time
                         record-distance :record-distance}]
  (let [[r1 r2] (roots (- racing-time) record-distance)]
    (int (inc (- (ugly-floor r2) (ugly-ceil r1))))))

(defn- margin-of-error [races]
  (reduce * (map winning-options races)))

(defn a
  ([] (a (slurp (io/resource "06.txt"))))
  ([input] (margin-of-error (parse-input 1 input))))

(defn b
  ([] (b (slurp (io/resource "06.txt"))))
  ([input] (margin-of-error (parse-input 2 input))))
