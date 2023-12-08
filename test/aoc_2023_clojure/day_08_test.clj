(ns aoc-2023-clojure.day-08-test
  (:require
   [aoc-2023-clojure.day-08 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input-1 "RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)")

(def input-2 "LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)")

(def input-3 "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)")

(deftest haunted-wasteland
  (testing "Day 08 - Part 1 (first example)"
    (is (= 2 (a input-1)))
    (is (= 6 (a input-2))))
  (testing "Day 08 - Part 2"
    (is (= 6 (b input-3)))))
