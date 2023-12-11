(ns aoc-2023-clojure.day-11-test 
  (:require
   [aoc-2023-clojure.day-11 :refer [main]]
   [clojure.test :refer [deftest is testing]]))

(def input "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....")

(deftest cosmic-expansion
  (testing "Day 11 - Part 1"
    (is (= 374 (main 2 input))))
  (testing "Day 11 - Part 2"
    (is (= 8410 (main 100 input)))))

