(ns aoc-2023-clojure.day-09-test 
  (:require
   [aoc-2023-clojure.day-09 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45")

(deftest mirage-maintenance
  (testing "Day 09 - Part 1"
    (is (= 114 (a input))))
  (testing "Day 09 - Part 2"
    (is (= 2 (b input)))))
