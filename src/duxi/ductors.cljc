(ns duxi.ductors
  (:require [duxi.xforms :as xf])
  (:refer-clojure :exclude [map keys vals]))

(defn nest
  [xf]
  (fn nest*
    ([pred? duct]
     (fn [f]
       (fn [bool coll]
         (let [f' (fn [x] (f (and bool (pred? x)) x))]
           (duct (fn [xform rf] (transduce (comp (xf f') xform) rf coll)))))))
    ([duct]
     (nest* (constantly true) duct))))

(defn link
  [duct]
  (fn [f]
    (fn [coll]
      (f (duct (fn [xform rf] (transduce xform rf coll)))))))

(defn make-ductor
  [duct]
  (fn [f]
    (fn [bool coll]
      (f bool (duct (fn [xform rf] (transduce xform rf coll)))))))

(def map (nest xf/map))

(def map-vals (nest xf/map-vals))

(def map-keys (nest xf/map-keys))

(def keys (nest xf/keys))

(def vals (nest xf/vals))

(def filter (nest clojure.core/filter))
