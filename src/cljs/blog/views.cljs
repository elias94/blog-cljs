(ns blog.views
  (:require
   [reagent.core :as r]
   [clojure.string :as string]
   [blog.session :as session]
   [markdown.core :refer [md->html]]
   [blog.theme :as theme]
   [blog.utils :as utils]))

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
     [:a.post-nav {:href "#/"} "Go home"]
     (when config
       [:header.post-header
        [:h1.post-title (:title config)]
        [:p.post-meta
         (str (utils/format-date (:date config)) "\n" (:meta config))]])
     [:article.post-content
      {:dangerouslySetInnerHTML {:__html (md->html content)}}]]
    [:div.loading "Loading..."]))

(defn archive-page [])

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
