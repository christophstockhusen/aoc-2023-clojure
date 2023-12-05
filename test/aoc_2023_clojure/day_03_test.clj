(ns aoc-2023-clojure.day-03-test 
  (:require
   [aoc-2023-clojure.day-03 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..")

(deftest gear-ratios
  (testing "Day 03 - Part 1"
    (is (= 4361 (a input))))
  (testing "Day 03 - Part 2"
    (is (= 467835 (b input)))))
