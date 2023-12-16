(ns aoc-2023-clojure.day-16-test 
  (:require
   [aoc-2023-clojure.day-16 :refer [a b]]
   [clojure.java.io :as io]
   [clojure.test :refer [deftest is testing]]))

(deftest the-floor-will-be-lava
  (let [input (slurp (io/resource "16_test.txt"))]
    (testing "Day 16 - Part 1"
      (is (= 46 (a input))))
    (testing "Day 16 - Part 2"
      (is (= 51 (b input))))))

