(ns blog.session
  (:refer-clojure :exclude [get set])
  (:require
   [reagent.core :as r]
   [blog.utils :refer [dark-theme?]]))

;; Session db
(defonce session (r/atom {:page :home}))

(defn get [k]
  (k @session))

(defn set [k v]
  (swap! session assoc k v))

;; prefix for local storage
(def ls-prefix "unico")

(defn ls-key [k]
  (str ls-prefix "-" (if (keyword? k) (name k) k)))

(defn set-ls
  "Set key-value into localStorage, using prefix."
  [k v]
  (.setItem js/localStorage (ls-key k) v))

(defn get-ls
  "Return value of key from localStorage."
  [k]
  (.getItem js/localStorage (ls-key k)))

(defn remove-ls
  "Remove key from localStorage."
  [k]
  (.removeItem js/localStorage (ls-key k)))
