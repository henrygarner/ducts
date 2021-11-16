(ns periscope.xforms-test
  (:require [periscope.xforms :refer [*>> map take filter last]]
            [periscope.core :refer [get update assoc]]
            [clojure.test :refer :all])
  (:refer-clojure :exclude [get update assoc map take filter last]))

(deftest get-duct
  (is (= '(1 3 5 7 9)
         (get (*>> (filter odd?)) (range 10))))
  (is (= ['(0 2) '(0 2 4)]
         (get (comp (*>> (filter (comp odd? count)))
                        (*>> (filter even?)))
              [(range 2) (range 3) (range 4) (range 5)]))))

(deftest lens-xforms-are-compatible
  (is (= '(1 3 5 7 9)
         (sequence (filter odd?) (range 10))))
  (is (= '(1 2 3 4 5 6 7 8 9 10)
         (sequence (map inc) (range 10)))))

(deftest update-duct
  (is (= '(0 2 2 4 4 6 6 8 8 10)
         (update (*>> (filter odd?)) inc (range 10))))
  (is (= ['(0 1) '(1 1 3) '(0 1 2 3) '(1 1 3 3 5)]
         (update (comp (*>> (filter (comp odd? count)))
                       (*>> (filter even?)))
                 inc
                 [(range 2) (range 3) (range 4) (range 5)]))))

(deftest map-duct
  (is (= [2 3 4 5 6]
         (get (*>> (map inc)) [1 2 3 4 5])))
  (is (= [3 4 5 6 7]
         (update (*>> (map inc)) inc [1 2 3 4 5])))
  (is (= [1 1 1 1 1]
         (assoc (*>> (map inc)) 1 [1 2 3 4 5]))))

(deftest take-test
  (is (= '(0 1 2) (get (*>> (take 3)) (range 10))))
  (is (= '(1 2 3 3 4 5 6 7 8 9) (update (*>> (take 3)) inc (range 10))))
  (is (= '(0 0 0 3 4 5 6 7 8 9) (assoc (*>> (take 3)) 0 (range 10)))))

(deftest duct-composition
  (is (= '(0 42 2 42 4 42 6 7 8 9) (assoc (*>> (filter odd?) (take 3)) 42 (range 10))))
  (is (= '(0 42 2 3 4 5 6 7 8 9) (assoc (*>> (take 3) (filter odd?)) 42 (range 10))))
  (is (= '(0 1 2 3 4 5 6 7 42 9) (assoc (*>> (filter even?) last) 42 (range 10)))))
