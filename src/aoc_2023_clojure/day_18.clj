(ns aoc-2023-clojure.day-18
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn parse-line [line]
  (let [[direction steps color] (str/split line #" ")]
    {:direction (case direction "R" :right "L" :left "U" :up "D" :down)
     :steps (parse-long steps)
     :color (first (re-seq #"#[a-z0-9]+" color))}))

(defn- parse-input [input]
  (map parse-line (str/split-lines input)))

(defn- move [[x y] direction steps]
  (case direction
    :right [(+ x steps) y]
    :up [x (+ y steps)]
    :left [(- x steps) y]
    :down [x (- y steps)]))

(defn- directions->points [directions]
  (loop [points [[0 0]]
         directions directions]
    (if (empty? directions)
      points
      (let [p (last points)
            next-dir (first directions)
            d (:direction next-dir)
            s (:steps next-dir)]
        (recur (conj points (move p d s))
               (rest directions))))))

(defn- shoelace [points]
  (let [points (partition 2 1 points  points)]
    (->> (map (fn [[[xi yi] [xj yj]]] (* (+ yi yj) (- xi xj))) points)
         (reduce +)
         (* 0.5)
         (abs))))

(defn- points-covered-by-boundary [points]
  (->> (partition 2 1 points points)
       (map (fn [[[x1 y1] [x2 y2]]] (+ (abs (- x1 x2)) (abs (- y1 y2)))))
       (reduce +)))

(defn- points-covered [points]
  (let [A (shoelace points)
        b (points-covered-by-boundary points)]
    (long (+ b (- (+ A 1) (/ b 2))))))

(defn a
  ([] (a (slurp (io/resource "18.txt"))))
  ([input] (let [parsed (parse-input input)]
             (points-covered (directions->points parsed)))))

(defn- color->instruction [s]
  (let [steps (Long/decode (apply str "0x" (take 5 (drop 1 s))))
        direction (case (last s)
                    \0 :right
                    \1 :down
                    \2 :left
                    \3 :up)]
    {:steps steps
     :direction direction}))

(defn b
  ([] (b (slurp (io/resource "18.txt"))))
  ([input] (let [parsed (parse-input input)
                 parsed (map #(color->instruction (:color %)) parsed)]
             (points-covered (directions->points parsed)))))
