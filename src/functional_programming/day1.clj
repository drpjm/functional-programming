(ns functional-programming.day1
  (:require [clojure.core.reducers :as r]))

; Performing accumulation with recursion
; This implementation matches one of the exercises from Day 1.
(defn sum [nums]
  (loop [acc 0
         curr-nums nums]
    (if (empty? curr-nums)
      acc
      (recur (+ acc (first curr-nums)) (rest curr-nums)))))

; and better with reduce!
(defn reduce-sum [nums]
  (reduce (fn [n1 n2] (+ n1 n2)) 0 nums))

; Reimplements reduce-sum using the reader macro syntax, which I do not care for, but it
; is one of the exercises at the end of Day 1.
(defn reduce-sum-alt [nums]
  (reduce #(+ %1 %2) 0 nums))


; using fold to parallelize sum
(defn parallel-sum [nums]
  (r/fold + nums))

; Word counting - taking the Java implementation and making it functional.
(defn get-words [text]
  (re-seq #"\w+" text)) ; removes white space

(defn count-words [pages]
  ; mapping get-words over pages, then concats into a seq, counts the words
  (frequencies (mapcat get-words pages))) 
