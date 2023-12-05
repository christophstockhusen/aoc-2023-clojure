(ns aoc-2023-clojure.day-01-test 
  (:require
   [aoc-2023-clojure.day-01 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input-1
  "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet")

(def input-2
  "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen")

(deftest trebuchet
  (testing "Day 01 - Part 1"
    (is (= 142 (a input-1))))
  (testing "Day 01 - Part 2"
    (is (= 281 (b input-2)))))
