(ns aoc-2023-clojure.day-03
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn digits->number [y digits]
  {:value (parse-long (str/join (map first digits)))
   :start-x (apply min (map second digits))
   :stop-x (apply max (map second digits))
   :y y})

(defn- parse-line [idx line]
  (loop [line (map vector (map str line) (range))
         current-digits []
         collected-numbers []
         collected-symbols {}]
    (cond
      (empty? line)
      {:numbers (if (empty? current-digits)
                  collected-numbers
                  (conj collected-numbers (digits->number idx current-digits)))
       :symbols collected-symbols}

      (some? (parse-long (ffirst line)))
      (recur (rest line)
             (conj current-digits [(parse-long (ffirst line)) (second (first line))])
             collected-numbers
             collected-symbols)

      (= "." (ffirst line))
      (recur (rest line)
             []
             (if (empty? current-digits)
               collected-numbers
               (conj collected-numbers (digits->number idx current-digits)))
             collected-symbols
             )

      :else
      (recur (rest line)
             []
             (if (empty? current-digits)
               collected-numbers
               (conj collected-numbers (digits->number idx current-digits)))
             (assoc collected-symbols [(second (first line)) idx] (ffirst line))))))

(defn merge-lines [a b]
  {:numbers (concat (:numbers a) (:numbers b))
   :symbols (merge (:symbols a) (:symbols b))})

(defn- parse-input [input]
  (->> (str/split-lines input)
       (map-indexed parse-line)
       (reduce merge-lines)))

(defn surrounding [[x y]]
  (let [deltas (for [dx [-1 0 1]
                     dy [-1 0 1]
                     :when (not (and (zero? dx) (zero? dy)))]
                 [dx dy])]
    (map #(vector
           (+ x (first %))
           (+ y (second %))) deltas)))

(defn number-coordinates [number]
  (map #(vector % (:y number)) (range (:start-x number) (inc (:stop-x number)))))

(defn has-neighboring-symbol? [symbols number]
  (let [coordinates (set (mapcat surrounding (number-coordinates number)))]
    (boolean (some (set (keys symbols)) coordinates))))

(defn- sum-of-parts [{numbers :numbers symbols :symbols}]
  (->> (filter (partial has-neighboring-symbol? symbols) numbers)
       (map :value)
       (reduce +)))

(defn a
  ([] (a (slurp (io/resource "03.txt"))))
  ([input] (->> (parse-input input)
                sum-of-parts)))

(defn assoc-number-to-gears [gears number]
  (let [number-neighbors (set (mapcat surrounding (number-coordinates number)))
        gear-coordinates (keys gears)]
    (->> (into {} (map (fn [xy] [xy (if (contains? number-neighbors xy) [(:value number)] [])]) gear-coordinates))
         (merge-with concat gears))))

(defn b
  ([] (b (slurp (io/resource "03.txt"))))
  ([input]
   (let [{numbers :numbers symbols :symbols} (parse-input input)
         gears (map first (filter #(= "*" (second %)) symbols))
         gears-to-numbers (into {} (map #(vector % []) gears))]
     (->> (reduce assoc-number-to-gears gears-to-numbers numbers)
          (map second)
          (filter #(= 2 (count %)))
          (map #(reduce * %))
          (reduce +)))))
