(ns aoc-2023-clojure.day-17
  (:require
   [clojure.data.priority-map :refer [priority-map]]
   [clojure.java.io :as io]
   [clojure.pprint :as pprint]
   [clojure.string :as str]))

(def input "2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533")

(defn- parse-line [idx line]
  (into {} (map #(vector [idx %1] (parse-long (str %2))) (range) line)))

(defn- parse-input [input]
  (let [lines (str/split-lines input)
        nodes (->> lines
                   (map parse-line (range))
                   (apply merge))]
    {:nodes nodes
     :max-i (dec (count lines))
     :max-j (dec (count (first lines)))}))

(defn- move [direction [i j]]
  (case direction
    :west [i (dec j)]
    :south [(inc i) j]
    :east [i (inc j)]
    :north [(dec i) j]))

(defn- left-right [direction]
  (cond
    (#{:west :east} direction) [:north :south]
    (#{:north :south} direction) [:east :west]))

(defn- neighbors [matrix {pos :pos steps :steps direction :direction}]
  (let [lr (left-right direction)]
    (->> (cond-> []
           (< steps 3) (conj {:pos (move direction pos) :direction direction :steps (inc steps)})
           true (conj {:pos (move (first lr) pos) :direction (first lr) :steps 1})
           true (conj {:pos (move (second lr) pos) :direction (second lr) :steps 1}))
         (filter (fn [{[i j] :pos}] (and (<= 0 i (:max-i matrix))
                                         (<= 0 j (:max-j matrix))))))))

(defn- shortest-path [graph neighbors-fn start]
  (loop [cnt 0
         distances {}
         visited #{}
         q (priority-map {:pos start
                          :direction :east
                          :steps 0} 0
                         {:pos start
                          :direction :south
                          :steps 0} 0)]
    (if (empty? q)
      distances
      (let [[node dist] (peek q)
            nbs (neighbors-fn graph node)
            new-nbs (filter (complement visited) nbs)
            new-prios (map (fn [v] (vector v (+ dist (get-in graph [:nodes (:pos v)])))) new-nbs)]
        (recur (inc cnt)
               (assoc distances node dist)
               (conj visited node)
               (reduce (fn [q p]
                         (if (contains? q (first p))
                           (update q (first p) #(min (second p) %))
                           (assoc q (first p) (second p))))
                       (pop q)
                       new-prios))))))

(defn a
  ([] (a (slurp (io/resource "17.txt"))))
  ([input]
   (let [matrix (parse-input input)
         dists (shortest-path matrix neighbors [0 0])
         target [(:max-i matrix) (:max-j matrix)]]
     (->> (filter #(= target (:pos (first %))) dists)
          (map second)
          (reduce min)))))

(defn- neighbors-2 [matrix {pos :pos steps :steps direction :direction}]
  (let [lr (left-right direction)]
    (->> (cond-> []
           (< steps 10) (conj {:pos (move direction pos) :direction direction :steps (inc steps)})
           (< 3 steps) (conj {:pos (move (first lr) pos) :direction (first lr) :steps 1})
           (< 3 steps) (conj {:pos (move (second lr) pos) :direction (second lr) :steps 1}))
         (filter (fn [{[i j] :pos}] (and (<= 0 i (:max-i matrix))
                                         (<= 0 j (:max-j matrix))))))))

(defn b
  ([] (b (slurp (io/resource "17.txt"))))
  ([input]
   (let [matrix (parse-input input)
         dists (shortest-path matrix neighbors-2 [0 0])
         target [(:max-i matrix) (:max-j matrix)]]
     (->> (filter #(= target (:pos (first %))) dists)
          (map second)
          (reduce min)))))
