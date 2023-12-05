(ns aoc-2023-clojure.day-05
  (:require
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(defn- parse-seeds [part seed-line]
  (let [numbers (map parse-long (re-seq #"\d+" seed-line))
        numbers (if (= 1 part)
                  (map vector numbers numbers)
                  (map (fn [[x d]] (vector x (+ x (dec d)))) (partition 2 numbers)))]
    (map (fn [[a b]] {:first a :last b}) numbers)))

(defn- parse-map-line [map-line]
  (let [[dest-start source-start range-length] (map parse-long (re-seq #"\d+" map-line))]
    {:source-first source-start
     :source-last (+ source-start (dec range-length))
     :offset (- dest-start source-start)}))

(defn- parse-map [map-lines]
  (let [lines (rest (str/split-lines map-lines))]
    (map parse-map-line lines)))

(defn- parse-input [part input]
  (let [splitted (str/split input #"\n\n")
        seeds (parse-seeds part (first splitted))
        maps (map parse-map (rest splitted))]
    {:seeds seeds
     :maps maps}))

(defn- split-seed-range [maps seed-range]
  (let [sorted-map-ranges (into clojure.lang.PersistentQueue/EMPTY (sort-by :source-first maps))]
    (loop [seed-range seed-range
           sorted-map-ranges sorted-map-ranges
           seed-ranges []]
      (let [m (peek sorted-map-ranges)
            offset (:offset m)]
        (cond
          (nil? seed-range)
          seed-ranges

          (nil? m)
          (conj seed-ranges seed-range)

          (< (:last seed-range) (:source-first m))
          (conj seed-ranges seed-range)

          (< (:source-last m) (:first seed-range))
          (recur seed-range (pop sorted-map-ranges) seed-ranges)

          (< (:first seed-range) (:source-first m))
          (recur (assoc seed-range :first (:source-first m))
                 sorted-map-ranges
                 (conj seed-ranges {:first (:first seed-range)
                                    :last (dec (:source-first m))}))

          (<= (:last seed-range) (:source-last m))
          (conj seed-ranges {:first (+ offset (:first seed-range))
                             :last (+ offset (:last seed-range))})

          :else
          (recur (assoc seed-range :first (inc (:source-last m)))
                 (pop sorted-map-ranges)
                 (conj seed-ranges {:first (+ offset (:first seed-range))
                                    :last (+ offset (:source-last m))})))))))

(defn- apply-mapping-rf [seed-ranges mapdef]
  (mapcat (partial split-seed-range mapdef) seed-ranges))

(defn- lowest-location [almanac]
  (->> (reduce apply-mapping-rf (:seeds almanac) (:maps almanac))
       (map :first)
       (apply min)))

(defn a
  ([] (a (slurp (io/resource "05.txt"))))
  ([input] (lowest-location (parse-input 1 input))))

(defn b
  ([] (b (slurp (io/resource "05.txt"))))
  ([input] (lowest-location (parse-input 2 input))))
