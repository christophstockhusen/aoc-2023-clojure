(ns aoc-2023-clojure.day-15
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-input [input]
  (str/split (str/trim input) #","))

(defn- holiday-ascii-string-helper [s]
  (let [hash-rf #(mod (* 17 (+ %1 %2)) 256)]
    (reduce hash-rf 0 (map int s))))

(defn a
  ([] (a (slurp (io/resource "15.txt"))))
  ([input]
   (->> (parse-input input)
        (map holiday-ascii-string-helper)
        (reduce +))))

(defn- instruction-rf [boxes instruction]
  (let [label (first (re-seq #"[a-z]+" instruction))
        operation (first (re-seq #"[=-]" instruction))
        box-idx (holiday-ascii-string-helper label)
        box (get boxes box-idx)]
    (->> (case operation
           "=" (let [focal-length (parse-long (first (re-seq #"[0-9]+" instruction)))]
                 (if (some #(= label (:label %)) box)
                   (mapv #(if (= label (:label %)) (assoc % :focal-length focal-length) %) box)
                   (conj box {:label label :focal-length focal-length})))
           "-" (filterv #(not= label (:label %)) box))
         (assoc boxes box-idx))))

(defn- process-instructions [instructions]
  (let [boxes (vec (repeat 255 []))]
    (reduce instruction-rf boxes instructions)))

(defn- overall-focusing-power [instructions]
  (let [boxes (process-instructions instructions)]
    (->> (mapcat (fn [idx box]
                   (map #(* (inc idx) (inc %1) (:focal-length %2))
                        (range)
                        box))
                 (range) boxes)
         (reduce +))))

(defn b
  ([] (b (slurp (io/resource "15.txt"))))
  ([input]
   (->> (parse-input input)
        overall-focusing-power)))

