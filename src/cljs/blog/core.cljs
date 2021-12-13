(ns blog.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [goog.events :as events]
   [goog.history.EventType :as HistoryEventType]
   [markdown.core :refer [md->html]]
   [blog.ajax :as ajax]
   [reitit.core :as reitit]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]
   [clojure.string :as string]
   [blog.session :as session]
   [blog.controllers :as ctrl])
  (:import goog.History))

;; Match atom for reitit navigation
(defonce match-a (atom nil))

(defn nav-link [uri title page]
  [:a.navbar-item
   {:href   uri
    :class (when (= page (session/get :page)) "is-active")}
   title])

(defn navbar []
  (r/with-let [expanded? (r/atom false)]
    [:nav.navbar.is-info>div.container
     [:div.navbar-brand
      [:a.navbar-item {:href "/" :style {:font-weight :bold}} "blog"]
      [:span.navbar-burger.burger
       {:data-target :nav-menu
        :on-click #(swap! expanded? not)
        :class (when @expanded? :is-active)}
       [:span] [:span] [:span]]]
     [:div#nav-menu.navbar-menu
      {:class (when @expanded? :is-active)}
      [:div.navbar-start
       [nav-link "#/" "Home" :home]
       [nav-link "#/about" "About" :about]]]]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(defn home-page []
  [:section.section>div.container>div.content
   (if-let [posts (session/get :posts)]
     (for [post posts]
       ^{:key (str post)}
       [:a {:href (str "#/blog/" post)} post])
     [:p "No posts!"])])

(defn post-page []
  (if-let [{:keys [content config]} (session/get :post)]
    [:div.post
     (when config
       [:header.post-header
        [:h1.post-title (:title config)]
        [:p.post-meta
         (str (:date config) "\n" (:meta config))]])
     [:article.post-content
      {:dangerouslySetInnerHTML {:__html (md->html content)}}]]
    [:div.loading "Loading..."]))

(defn archive-page [])

(def pages
  {:home    home-page
   :about   about-page
   :archive archive-page
   :post    post-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/"        {:name :home
                 :view home-page
                 :controllers
                 [{:start #(ctrl/fetch-posts)}]}]
    ["/about"   :about]
    ["/archive" :archive]
    ["/blog/:id" {:name :post
                  :view post-page
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
  (ajax/load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
