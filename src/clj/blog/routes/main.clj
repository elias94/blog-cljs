(ns blog.routes.main
  (:require
   [babashka.fs :as fs]
   [blog.layout :as layout]
   [clojure.string :as string]
   [clojure.java.io :as io]
   [blog.middleware :as middleware]
   [blog.middleware.formats :as formats]
   [ring.util.response]
   [ring.util.http-response :as response]
   [reitit.ring.coercion :as coercion]
   [reitit.coercion.spec :as spec-coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]))

;; Regex for YAML header
(def re-yaml #"^[\s]*---\n([^(-){3}]*)---\n")

;; Regex for YAML prop
(def re-yaml-prop #"(\w+)\:\s(.+)")

(defn parse-yaml [content]
  (let [yaml (peek (re-find re-yaml content))]
    (when yaml
      (let [props (->> (re-seq re-yaml-prop yaml)
                       (map rest))]
        (reduce
         (fn [acc v]
           (merge acc {(keyword (first v)) (second v)}))
         {}
         props)))))

(defn home-page [request]
  (layout/render request "home.html"))

(defn remove-ext [f-name]
  (first (string/split f-name #"\.")))

(defn post-name [f-name]
  (str "posts/" f-name ".md"))

(defn get-post [f-name]
  (-> (post-name f-name)
      (io/resource)
      (slurp)))

(defn filter-post? [post]
  (string/starts-with? post "@"))

(defn- get-posts-files []
  (-> "posts"
      (io/resource)
      (io/file)
      (.listFiles)))

(defn get-posts-name []
  (->> (get-posts-files)
       (map
        #(-> (.getName %)
             (remove-ext)))    ; extract file-name without extension
       (filter #(not (string/starts-with? % "@")))))

(defn post? [f-name]
  (let [src (io/resource (post-name f-name))]
    (and src (fs/exists? src)
         (not (fs/directory? src)))))

(defn preprocess-content [content]
  (-> (string/replace content re-yaml "")
      (string/trim)))

(defn main-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 coercion/coerce-exceptions-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware]}
   ["/posts"
    {:get (fn [_]
            (response/ok
             (vec (get-posts-name))))}]
   ["/post"
    {:parameters {:query {:id string?}}
     :get (fn [{{id :id} :params}]
            (if (post? id)
              (response/ok
               (let [post (get-post id)]
                 {:content (preprocess-content post)
                  :config  (parse-yaml post)}))
              (response/bad-request
               {:error "Invalid document"})))}]])

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]])
