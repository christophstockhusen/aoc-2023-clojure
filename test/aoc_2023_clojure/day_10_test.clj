(ns aoc-2023-clojure.day-10-test
  (:require
   [aoc-2023-clojure.day-10 :refer [a b]]
   [clojure.test :refer [deftest is testing]]))

(def input-1 "-L|F7
7S-7|
L|7||
-L-J|
L|-JF")

(def input-2 "7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ")

(def input-3 "...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........")

(def input-4 ".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...")

(def input-5 "FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L")

(deftest pipe-maze
  (testing "Day 10 - Part 1"
    (is (= 4 (a input-1)))
    (is (= 8 (a input-2))))
  (testing "Day 10 - Part 2"
    (is (= 4 (b input-3)))
    (is (= 8 (b input-4)))
    (is (= 10 (b input-5)))))
