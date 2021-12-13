(ns blog.session
  (:refer-clojure :exclude [get set])
  (:require
   [reagent.core :as r]))

;; Session db
(defonce session (r/atom {:page :home}))

(defn get [k]
  (k @session))

(defn set [k v]
  (swap! session assoc k v))
