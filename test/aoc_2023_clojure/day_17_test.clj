(ns aoc-2023-clojure.day-17-test 
  (:require
   [aoc-2023-clojure.day-17 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533")

(deftest clumsy-crucible
  (testing "Day 17 - Part 1"
    (is (= 102 (a input))))
  (testing "Day 17 - Part 2"
    (is (= 94 (b input)))))
