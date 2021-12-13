(ns blog.controllers
  (:require
   [ajax.core :refer [GET POST]]
   [blog.session :as session]))

;; -------------------------
;; Controllers

(defn fetch-posts []
  (GET "/posts" {:handler #(session/set :posts %)}))

(defn fetch-post [post-id]
  (GET "/post" {:format :json
                :params {:id post-id}
                :handler #(session/set :post %)}))
