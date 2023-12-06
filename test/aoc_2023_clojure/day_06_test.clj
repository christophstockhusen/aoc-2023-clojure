(ns aoc-2023-clojure.day-06-test 
  (:require
   [aoc-2023-clojure.day-06 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "Time:      7  15   30
Distance:  9  40  200")

(deftest wait-for-it
  (testing "Day 06 - Part 1"
    (is (= 288 (a input))))
  (testing "Day 06 - Part 2"
    (is (= 71503 (b input)))))

