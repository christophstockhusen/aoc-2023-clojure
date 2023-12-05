(ns aoc-2023-clojure.day-01
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]))

(def word-digit-map
  (zipmap ["zero" "one" "two" "three" "four" "five" "six" "seven" "eight" "nine"]
          (range 10)))

(defn- parse-number-string [s]
  (parse-long (str (get word-digit-map s s))))

(defn digits-from-string [part s]
  (let [pattern (if (= part 1)
                  #"^\d"
                  (re-pattern (str/join "|" (map #(str "(^" % ")") (conj (keys word-digit-map) "\\d")))))
        digits (loop [s s
                      res []]
                 (if (zero? (count s))
                   res
                   (let [m (first (re-find pattern s))]
                     (if (nil? m)
                       (recur (subs s 1) res)
                       (recur (subs s 1) (conj res m))))))]
    (map parse-number-string digits)))

(defn- extract-calibration-value [part line]
  (let [digits (digits-from-string part line)]
    (parse-long (str (first digits) (last digits)))))

(defn main
  ([part] (main part (slurp (io/resource "01.txt"))))
  ([part input]
   (->> (str/split-lines input)
        (map (partial extract-calibration-value part))
        (reduce +))))

(defn a
  ([] (main 1))
  ([input] (main 1 input)))

(defn b
  ([] (main 2))
  ([input] (main 2 input)))

(b)


