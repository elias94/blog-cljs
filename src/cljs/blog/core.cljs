(ns blog.core
  (:require
   [reagent.dom :as rdom]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [blog.ajax :as ajax]
   [reitit.core :as reitit]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]
   [clojure.string :as string]
   [blog.session :as session]
   [blog.controllers :as ctrl]
   [blog.theme :as theme]
   [blog.views :refer [page]])
  (:import goog.History))

;; Match atom for reitit navigation
(defonce match-a (atom nil))

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/"        {:name :home
                 :controllers
                 [{:start #(ctrl/fetch-posts)}]}]
    ["/about"   :about]
    ["/archive" :archive]
    ["/blog/:id" {:name :post
                  :controllers
                  [{:parameters {:path [:id]}
                    :start (fn [params]
                             (ctrl/fetch-post (-> params :path :id)))}]}]]))

(defn match-route [uri]
  (->> (or (not-empty (string/replace uri #"^.*#" "")) "/")
       (reitit/match-by-path router)
       :data
       :name))

(defn navigate! [uri]
  (session/set :page (match-route uri)))

(defn start-router! []
  (rfe/start!
   router
   (fn [new-match]
     (swap!
      match-a
      (fn [old-match]
        (when new-match
          (->> (-> (:controllers old-match)
                   (rfc/apply-controllers new-match))
               (assoc new-match :controllers))))))
   {}))


;; -------------------------
;; History
;; must be called after routes have been defined

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     HistoryEventType/NAVIGATE
     (fn [^js/Event.token event]
       (navigate! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-components []
  (rdom/render
   [#'page]
   (.getElementById js/document "app")))

(defn init! []
  (start-router!)
  (session/init)
  (theme/init)
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
