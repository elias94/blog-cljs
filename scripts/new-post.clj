#!/usr/bin/env bb

(ns blog.script
  (:require [clojure.string :as string])
  (:import 'java.time.format.DateTimeFormatter
           'java.time.LocalDateTime))

(def posts-path
  "../resources/posts/")

(defn post-template [title]
  (str "---\ntitle: " title "\n---\n\n\n"))

(defn format-date []
  (let [formatter (DateTimeFormatter/ofPattern "yyyy-MM-dd")]
    (-> (LocalDateTime/now)
        (.format formatter))))

(defn format-title [title]
 (string/replace title #" " "-"))

(let [args  *command-line-args*
      title (or (:title args) "Undefined")
      exten (or (:extension args) "md")
      file  (str (format-date) "-" title "." exten)]
  (spit (str posts-path file) (post-template title)))
