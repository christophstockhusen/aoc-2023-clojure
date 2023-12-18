(ns aoc-2023-clojure.day-18-test 
  (:require
   [aoc-2023-clojure.day-18 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)")

(deftest lavaduct-lagoon
  (testing "Day 18 - Part 1"
    (is (= 62 (a input))))
  (testing "Day 18 - Part 2"
    (is (= 952408144115 (b input)))))
