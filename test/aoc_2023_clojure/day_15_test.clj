(ns aoc-2023-clojure.day-15-test 
  (:require
   [aoc-2023-clojure.day-15 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7")

(deftest lens-library
  (testing "Day 15 - Part 1"
    (is (= 1320 (a input))))
  (testing "Day 15 - Part 2"
    (is (= 145 (b input)))))
