(ns functional-programming.day2
  (:require [functional-programming.day1 :as day1]
            [clojure.core.reducers :as r]
            [clojure.core.protocols :refer [CollReduce coll-reduce]]
            [clojure.core.reducers :refer [CollFold coll-fold]]))

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

; Making our own reducer
(defn my-reduce
  ([f coll] (coll-reduce coll f))
  ([f init coll] (coll-reduce coll f init)))

(defn make-reducer [reducible transform-fn]
  (reify ; similar in Java to making a new anonymous instance of an interface
    CollFold ; added the coll-fold to make-reducer
    (coll-fold [_ n combine-fn reduce-fn]
      (coll-fold reducible n combine-fn (transform-fn reduce-fn)))
    
    CollReduce
    (coll-reduce [_ f1]
      (coll-reduce reducible (transform-fn f1) (f1)))
    (coll-reduce [_ f1 init]
      (coll-reduce reducible (transform-fn f1) init))))

(defn my-map [map-fn reducible]
  (make-reducer reducible
                (fn [reduce-fn]
                  (println reduce-fn) ; just to see wha the reduce-fn
                  (fn [acc val]
                    (reduce-fn acc (map-fn val))))))

(defn my-fold
  ([reduce-fn coll]
    (my-fold reduce-fn reduce-fn coll)) ; default combines and reduces with the same function
  ([combine-fn reduce-fn coll]
    (my-fold 512 combine-fn reduce-fn coll))
  ([n combine-fn reduce-fn coll]
    (println "combine: " combine-fn " reduce: " reduce-fn " coll: " coll)
    (coll-fold coll n combine-fn reduce-fn)))