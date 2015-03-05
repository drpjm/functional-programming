(ns functional-programming.day2
  (:require [clojure.core.reducers :as r]
            [clojure.core.protocols :refer [CollReduce coll-reduce]]
            [clojure.core.reducers :refer [CollFold coll-fold]]))

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

(defn map2 [map-fn reducible]
  "Returns a reducer that performs map."
  (make-reducer reducible
                (fn [reduce-fn] ; This is the "transform function" from Rich Hickey's talk.
                  (fn [acc val]
                    (reduce-fn acc (map-fn val))))))

(defn fold2
  ([reduce-fn coll]
    (fold2 reduce-fn reduce-fn coll)) ; default combines and reduces with the same function
  ([combine-fn reduce-fn coll]
    (fold2 512 combine-fn reduce-fn coll))
  ([n combine-fn reduce-fn coll]
    (println "combine: " combine-fn " reduce: " reduce-fn)
    (coll-fold coll n combine-fn reduce-fn)))

; Exercises
; Flatten needs to determine if an input element is a sequence. If so, it needs to call
; coll-reduce on the result of flatten2. If the element is not a sequence, it applies the 
; reducingfunction to current acc and val. 
(defn flatten2 [coll]
  (make-reducer coll
                (fn [reduce-fn]
                  (fn [acc val]
                    (if (sequential? val)
                      (coll-reduce (flatten2 val) reduce-fn acc)
                      (reduce-fn acc val))))))