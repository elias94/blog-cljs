(ns blog.views
  (:require
   [reagent.core :as r]
   [clojure.string :as string]
   [blog.session :as session]
   [herb.core :refer [<class]]
   ["animejs" :as anime]
   ["charming" :as charming]
   [blog.markdown :as markdown]
   [blog.theme :as theme]
   [blog.utils :as utils]))

(defn about-page []
  [:section.section>div.container>div.content
   [:img {:src "/img/warning_clojure.png"}]])

(def re-date #"^[0-9]{4}-[0-9]{2}-[0-9]{2}")

(defn parse-filename [post]
  {:date  (utils/format-date (re-find re-date post))
   :title (-> (string/split post re-date)
              (second)
              (subs 1)
              (string/replace #"-" " "))})

(defn styled-title []
  {:font-weight 700
   :font-size "5em"
   :line-height ".85em"
   :text-transform "uppercase"
   :letter-spacing "-.05em"
   :border "7px solid var(--text-color)"
   :padding "30px"
   :margin "80px 0"
   :opacity 1})

(defn title-anim []
  (let [title-el (.querySelector js/document ".main-title")]
    (charming title-el)
    (js/setTimeout
     (fn []
       (anime
        #js
         {:targets  (.querySelectorAll title-el "div > span")
          :easing "easeInOutQuad"
          :duration #(rand-int 800)
          :delay    #(rand-int 400)
          :opacity  #js [0 1]}))
     "300")))

(defn home-title []
  (r/create-class
   {:component-did-mount
    #(title-anim)
    :reagent-render
    (fn []
      [:div.main-title {:class (<class styled-title)}
       [:div "ELIA"]
       [:div "SCO"]
       [:div "TTO"]])}))

(defn home-page []
  [:div.home
   [home-title]
   [:div.home-content
    (if-let [posts (session/get :posts)]
      [:ul.posts-list
       (for [post posts]
         (let [conf (parse-filename post)]
           ^{:key (str post)}
           [:li.post-item
            [:a.post-link {:href (str "#/blog/" post)} (:title conf)]
            [:span.post-meta (:date conf)]]))]
      [:p "No posts!"])]])

(defn post-page []
  (r/with-let [mount!
               (fn [this]
                 (js/setTimeout #(markdown/replace-code this) "150"))]
    [:div.blog
     (if-let [{:keys [content config id]} (session/get :post)]
       [:div.post
        [:a.post-nav {:href "#/"} "Go home"]
        (when config
          (let [conf (parse-filename id)
                date (utils/format-date (or (:date config) (:date conf)))]
            [:header.post-header
             [:h1.post-title (:title config)]
             [:p.post-meta
              (str date "\n" (:meta config))]]))
        [:article.post-content
         {:ref mount!
          :dangerouslySetInnerHTML {:__html (markdown/to-html content)}}]]
       [:div.loading "Loading..."])]))

(defn archive-page []
  [:div.home
   [:div.home-content
    [:h3.title "Posts archive"]
    (if-let [posts (session/get :posts)]
      [:ul.posts-list
       (for [post posts]
         (let [conf (parse-filename post)]
           ^{:key (str post)}
           [:li.post-item
            [:a.post-link {:href (str "#/blog/" post)} (:title conf)]
            [:span.post-meta (:date conf)]]))]
      [:p "No posts!"])]])

(defn header []
  [:header.header
   [theme/button]])

(defn footer []
  [:footer.footer
   [:div.footer-menu
    [:div.footer-menu-line
     [:a.footer-menu-option {:href "#/"} "Home"]
     [:span.sep " • "]
     [:a.footer-menu-option {:href "#/archive"} "Archive"]
     [:span.sep " • "]
     [:a.footer-menu-option {:href "https://github.com/elias94/blog"
                             :target "_blank"}
      "Source"]]]
   [:p (str "© " (.getFullYear (js/Date.)) " Elia Scotto")]])

(def pages
  {:home    home-page
   :about   about-page
   :archive archive-page
   :post    post-page})

(defn page []
  [:<>
   [header]
   [(pages (session/get :page))]
   [footer]])
