(ns blog.markdown
  (:require
   [reagent.dom :as rdom]
   [clojure.string :as string]
   [markdown.core :refer [md->html]]
   [blog.editor :refer [editor]]))

(defn replace-code
  "Replace code blocks using the editor."
  [parent-comp]
  (let [dn  (rdom/dom-node parent-comp)
        cbs (.querySelectorAll dn "code.clojure")]
    (doseq [node cbs]
      (let [content (.-textContent node)
            pre (.-parentNode node)
            div (.createElement js/document "div")]
        (.replaceChild (.-parentNode pre) div pre)
        (rdom/render
         [editor content]
         div)))))

(defn to-html
  "Convert Markdown string to formatted HTML."
  [content]
  (-> (string/trim content)
      (md->html)))
