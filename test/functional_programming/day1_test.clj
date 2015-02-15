(ns functional-programming.day1-test
  (:require [clojure.test :refer :all]
            [functional-programming.day1 :refer :all]))

(def lots-of-numbers (range 0 10000000))

; Testing the parallel sum:
(defn test-parallel-sum []
  ; run a couple times to prime the jvm
  (time (parallel-sum lots-of-numbers))
  (time (parallel-sum lots-of-numbers))
  (time (parallel-sum lots-of-numbers)))

; For xml word counting: frequencies is a clojure function that returns a map
; with the frequency of each element.
(frequencies ["orange" "apple" "banana" "pear" "orange"])