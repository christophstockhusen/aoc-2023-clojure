(ns aoc-2023-clojure.day-10
  (:require
   [clojure.java.io :as io]
   [clojure.set :as set]
   [clojure.string :as str]))

(def symbol->directions
  {\| #{:north :south}
   \- #{:east :west}
   \L #{:north :east}
   \J #{:north :west}
   \7 #{:south :west}
   \F #{:south :east}
   \S :start})

(defn- parse-line [idx line]
  (->> (map #(vector [idx %1] (get symbol->directions %2 nil)) (range) line)
       (filter #(some? (second %)))))

(defn- parse-input [input]
  (let [parsed (->> (mapcat parse-line (range) (str/split-lines input))
                    (into {}))]
    {:start (ffirst (filter #(= :start (second %)) parsed))
     :pipes (into {} (filter #(not= :start (second %)) parsed))
     :size-i (count (str/split-lines input))
     :size-j (count (first (str/split-lines input)))}))

(defn- orientation [[ci cj] [ni nj]]
  (cond (= nj (inc cj)) :east
        (= nj (dec cj)) :west
        (= ni (inc ci)) :south
        (= ni (dec ci)) :north
        :else nil))

(defn- next-pos [[i j] direction]
  (case direction
    :west [i (dec j)]
    :east [i (inc j)]
    :north [(dec i) j]
    :south [(inc i) j]))

(defn- find-next-node [{start :start pipes :pipes} previous-pos current-pos]
  (cond
    (and (= start current-pos) (not= previous-pos current-pos))
    nil

    (= start current-pos previous-pos)
    (cond
      (contains? (get pipes (next-pos current-pos :west)) :east) (next-pos current-pos :west)
      (contains? (get pipes (next-pos current-pos :south)) :north) (next-pos current-pos :south)
      (contains? (get pipes (next-pos current-pos :east)) :west) (next-pos current-pos :east)
      (contains? (get pipes (next-pos current-pos :north)) :south) (next-pos current-pos :north))

    :else
    (let [current-pipe (get pipes current-pos)
          orientation-to-last-position (orientation current-pos previous-pos)
          orientation-next-position (first (set/difference current-pipe #{orientation-to-last-position}))]
      (next-pos current-pos orientation-next-position))))

(defn- loop-nodes [maze]
  (loop [previous-pos (:start maze)
         current-pos (:start maze)
         visited []]
    (if-let [next-pos (find-next-node maze previous-pos current-pos)]
      (recur current-pos next-pos (conj visited current-pos))
      visited)))

(defn- loop-length [maze]
  (/ (count (loop-nodes maze)) 2))

(defn a
  ([] (a (slurp (io/resource "10.txt"))))
  ([input] (loop-length (parse-input input))))

(defn- loop-only [maze]
  (let [loop (loop-nodes maze)
        start-pipe #{(orientation (first loop) (second loop))
                     (orientation (first loop) (last loop))}]
    (-> (into {} (map #(vector % (get-in maze [:pipes %])) loop))
        (assoc (:start maze) start-pipe))))

(defn- count-inside-outside-rf [maze state next-tile]
  (cond (and (nil? (get maze next-tile))
             (:inside state))
        (update state :cnt inc)

        (and (nil? (get maze next-tile))
             (not (:inside state)))
        state

        (= #{:north :south} (get maze next-tile))
        (update state :inside not)

        (= #{:east :west} (get maze next-tile))
        state

        (= 1 (count (filter #{:north :south} (get maze next-tile))))
        (let [d1 (first (filter #{:north :south} (get maze next-tile)))]
          (cond
            (= #{:north :south} (conj (:partial-crossing state) d1))
            (-> (assoc state :partial-crossing #{})
                (update :inside not))

            (= (:partial-crossing state) #{d1})
            (assoc state :partial-crossing #{})

            (empty? (:partial-crossing state))
            (assoc state :partial-crossing #{d1})

            :else state))))

(defn- count-tiles-line [maze i]
  (->> (reduce (partial count-inside-outside-rf maze)
               {:cnt 0 :inside false :partial-crossing #{}}
               (map #(vector i %) (range (:size-j maze))))
       (:cnt)))

(defn- count-tiles-enclosed [maze]
  (let [cleaned (loop-only maze)
        cleaned (assoc cleaned :size-i (:size-i maze))
        cleaned (assoc cleaned :size-j (:size-j maze))]
    (->> (map #(count-tiles-line cleaned %) (range (:size-i maze)))
         (reduce +))))

(defn b
  ([] (b (slurp (io/resource "10.txt"))))
  ([input] (count-tiles-enclosed (parse-input input))))

