(ns aoc-2023-clojure.day-13-test 
  (:require
   [aoc-2023-clojure.day-13 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#")

(deftest point-of-incidence
  (testing "Day 13 - Part 1"
    (is (= 405 (a input))))
  (testing "Day 13 - Part 1"
    (is (= 400 (b input)))))
