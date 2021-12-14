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

(defn init []
  (let [theme (if (dark-theme?) "dark" "light")]
    (set :theme theme)))
