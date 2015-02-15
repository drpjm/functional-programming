(ns functional-programming.day2
  (:require [functional-programming.day1 :as day1]
            [clojure.core.reducers :as r]))

; This code follows Day 2 within Chapter 3.
; To start, utilize the function merge-with to combine parallel word count operations.
(defn count-words-parallel [pages]
  (reduce (partial merge-with +)
          (pmap (fn [p] (frequencies (day1/get-words p))) pages)))

; This implementation is still not as fast as it could be. The pages could be batched with partition!
(defn count-words-batched [pages]
  (reduce (partial merge-with +)
          (pmap (fn [p] (frequencies (day1/get-words p))) (partition-all 100 pages))))

; Messing with reducers...
; A reducer returns a recipe to generate a result. Reducers are NOT executed until they
; are passed to fold or reduce.
(def mult-2-map
  (r/map (partial * 2) (range 1 10))) ; r/map is a recipe to generate a sequence of square numbers
(reduce conj [] mult-2-map)

; Protocols in clojure

