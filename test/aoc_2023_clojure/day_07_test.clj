(ns aoc-2023-clojure.day-07-test 
  (:require
   [aoc-2023-clojure.day-07 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483")

(deftest camel-cards
  (testing "Day 07 - Part 1"
    (is (= 6440 (a input))))
  (testing "Day 07 - Part 2"
    (is (= 5905 (b input)))))
