(ns blog.theme
  [:require
   [blog.session :as session]
   [blog.utils :refer [dark-theme? getElementById]]])

(def sun-icon
  [:svg
   {:width "24"
    :stroke-linejoin "round"
    :shape-rendering "geometricPrecision"
    :fill "none"
    :viewBox "0 0 24 24"
    :stroke "currentColor"
    :stroke-linecap "round"
    :stroke-width "1.5"
    :height "24"}
   [:circle {:r "5", :cy "12", :cx "12"}]
   [:path
    {:d
     "M12 1v2M12 21v2M4.22 4.22l1.42 1.42M18.36 18.36l1.42 1.42M1 12h2M21 12h2M4.22 19.78l1.42-1.42M18.36 5.64l1.42-1.42"}]])

(def moon-icon
  [:svg
   {:width "24"
    :stroke-linejoin "round"
    :shape-rendering "geometricPrecision"
    :fill "none"
    :viewBox "0 0 24 24"
    :stroke "currentColor"
    :stroke-linecap "round"
    :stroke-width "1.5"
    :height "24"}
   [:path {:d "M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"}]])

(defn invert [theme]
  (if (= theme "dark")
    "light"
    "dark"))

(defn change-theme [new-theme]
  (let [link (getElementById "theme-link")
        curr (.getAttribute link "value")
        update? (not= curr new-theme)]
    (session/set :theme new-theme)
    (session/set-ls :theme new-theme)
    (when update?
      (let [value (str new-theme)]
        (set! (.-value link) value)
        (set! (.-href  link) (str "/css/" value ".css"))))))

(defn button []
  (let [theme (session/get :theme)]
    [:div {:on-click #(change-theme (invert theme))}
     (if (= theme "dark")
       sun-icon
       moon-icon)]))

(defn init []
  (let [theme (if (dark-theme?) "dark" "light")
        ls-theme (session/get-ls :theme)]
    (if ls-theme
      (change-theme ls-theme)
      (change-theme theme))))
