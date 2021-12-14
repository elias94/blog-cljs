(ns blog.controllers
  (:require
   [ajax.core :refer [GET POST]]
   [blog.session :as session]))

;; -------------------------
;; Controllers

(defn url [path]
  (str "/api" path))

(defn req [opts]
  (cond
    (contains? opts :get)
    (GET (url (:get opts)) (dissoc opts :get))

    (contains? opts :post)
    (POST (url (:post opts)) (dissoc opts :post))))

(defn fetch-posts []
  (req {:get     "/posts"
        :handler #(session/set :posts %)}))

(defn fetch-post [post-id]
  (req {:get             "/post"
        :format          :json
        :response-format :json
        :keywords?       true
        :params          {:id post-id}
        :handler         #(session/set :post %)}))
