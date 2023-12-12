(ns aoc-2023-clojure.day-12-test 
  (:require
   [aoc-2023-clojure.day-12 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1")

(deftest hot-springs
  (testing "Day 12 - Part 1"
    (is (= 21 (a input))))
  (testing "Day 12 - Part 2"
    (is (= 525152 (b input)))))
