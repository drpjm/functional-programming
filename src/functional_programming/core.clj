(ns functional-programming.core)

; Performing accumulation with recursion
(defn sum [nums]
  (loop [acc 0
         curr-nums nums]
    (if (empty? curr-nums)
      acc
      (recur (+ acc (first curr-nums)) (rest curr-nums)))))

; and better with reduce!
(defn reduce-sum [nums]
  (reduce (fn [n1 n2] (+ n1 n2)) 0 nums))