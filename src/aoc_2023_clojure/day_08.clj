(ns aoc-2023-clojure.day-08
  (:require
   [clojure.java.io :as io]
   [clojure.math.numeric-tower :refer [gcd lcm]]
   [clojure.string :as str]))

(defn- parse-input [input]
  (let [[instructions nodes] (str/split input #"\n\n")]
    {:instructions (map keyword (str/split instructions #""))
     :nodes (->> nodes
                 (re-seq #"[A-Z0-9]{3}")
                 (partition 3)
                 (map (fn [[s t1 t2]] (vector s {:L t1 :R t2})))
                 (into {}))}))

(defn- follow [{instructions :instructions nodes :nodes} start target?]
  (loop [instructions (cycle instructions)
         current start
         cnt 0]
    (if (target? current)
      cnt
      (recur
       (next instructions)
       (get-in nodes [current (first instructions)])
       (inc cnt)))))

(defn a
  ([] (a (slurp (io/resource "08.txt"))))
  ([input]
   (follow (parse-input input) "AAA" #(= % "ZZZ"))))

(defn b
  ([] (b (slurp (io/resource "08.txt"))))
  ([input]
   (let [parsed (parse-input input)
         starts (filter #(= (last %) \A) (keys (:nodes parsed)))
         lengths (map #(follow parsed % (fn [t] (= (last t) \Z))) starts)]
     (reduce lcm lengths))))
