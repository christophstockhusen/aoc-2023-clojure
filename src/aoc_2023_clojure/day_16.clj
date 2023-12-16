(ns aoc-2023-clojure.day-16
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(defn- parse-line [idx line]
  (let [m (map #(vector [idx %1] %2) (range) line)]
    (into {} (filter #(#{\\ \/ \- \|} (second %)) m))))

(defn- parse-input [input]
  (let [lines (str/split-lines input)
        items (->> lines
                   (map parse-line (range))
                   (apply merge))
        max-i (dec (count lines))
        max-j (dec (count (first lines)))]
    (-> items
        (assoc :max-i max-i)
        (assoc :max-j max-j))))

(defn- move [direction [i j]]
  (case direction
    :up [(dec i) j]
    :down [(inc i) j]
    :left [i (dec j)]
    :right [i (inc j)]))

(defn- trace-beam [contraption [[i j] direction]]
  (let [tile (get contraption [i j])]
    (->>
     (cond
       (or (and (= tile \-)
                (#{:left :right} direction))
           (and (= tile \|)
                (#{:up :down} direction)))
       [[(move direction [i j]) direction]]

       (and (= tile \-)
            (#{:up :down} direction))
       [[(move :left [i j]) :left] [(move :right [i j]) :right]]

       (and (= tile \|)
            (#{:left :right} direction))
       [[(move :up [i j]) :up] [(move :down [i j]) :down]]

       (and (= tile \/)
            (= direction :up))
       [[(move :right [i j]) :right]]

       (and (= tile \/)
            (= direction :down))
       [[(move :left [i j]) :left]]

       (and (= tile \/)
            (= direction :right))
       [[(move :up [i j]) :up]]

       (and (= tile \/)
            (= direction :left))
       [[(move :down [i j]) :down]]

       (and (= tile \\)
            (= direction :up))
       [[(move :left [i j]) :left]]

       (and (= tile \\)
            (= direction :down))
       [[(move :right [i j]) :right]]

       (and (= tile \\)
            (= direction :right))
       [[(move :down [i j]) :down]]

       (and (= tile \\)
            (= direction :left))
       [[(move :up [i j]) :up]]

       :else
       [[(move direction [i j]) direction]])
     (filter (fn [[[i j] _]]
               (and (<= 0 i (:max-i contraption))
                    (<= 0 j (:max-j contraption))))))))

(defn- energized-tiles [contraption starting-position]
  (loop [beams [starting-position]
         visited (into #{} beams)]
    (if (empty? beams)
      (set (map first visited))
      (let [next-beams (->> beams
                            (mapcat (partial trace-beam contraption))
                            (filter (complement visited)))]
        (recur next-beams (into visited next-beams))))))

(defn a
  ([] (a (slurp (io/resource "16.txt"))))
  ([input] (count (energized-tiles (parse-input input) [[0 0] :right]))))

(defn b
  ([] (b (slurp (io/resource "16.txt"))))
  ([input]
   (let [contraption (parse-input input)
         max-i (:max-i contraption)
         max-j (:max-j contraption)]
     (->> (concat [[[0 0] :right]
                   [[0 0] :down]
                   [[max-i 0] :right]
                   [[max-i 0] :up]
                   [[0 max-j] :left]
                   [[0 max-j] :down]
                   [[max-i max-j] :up]
                   [[max-i max-j] :left]]
                  (map #(vector [% 0] :right) (range 1 (inc max-i)))
                  (map #(vector [% max-j] :left) (range 1 (inc max-i)))
                  (map #(vector [0 %] :down) (range 1 (inc max-j)))
                  (map #(vector [max-i %] :up) (range 1 (inc max-j))))
          (pmap (partial energized-tiles contraption))
          (pmap count)
          (reduce max)))))
