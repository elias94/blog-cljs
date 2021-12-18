(ns blog.markdown
  (:require
   [goog.object]
   [reagent.dom :as rdom]
   [clojure.string :as string]
   [markdown.core :refer [md->html]]
   [blog.editor :refer [editor]]
   [blog.utils :refer [obj->clj in?]]))

(def code-modifiers
  "Markdown code block modifiers."
  [{:key "run"      :prop :runnable}
   {:key "readonly" :prop :readonly}])

(defn extract-opts
  "Extract codeblocks options from classnames."
  [node]
  (let [class-list (-> (.-classList node)
                       (obj->clj)
                       :value)
        mod-keys   (map #(:key %) code-modifiers)
        cleaned    (->> (string/split class-list " ")
                        (filter #(not (in? mod-keys %)))
                        (string/join " "))]
    (loop [cm   code-modifiers
           opts {:class-list cleaned}]
      (if (empty? cm)
        opts
        (let [{:keys [prop key]} (first cm)]
          (recur
           (rest cm)
           (assoc opts prop (string/includes? class-list key))))))))

(defn replace-code
  "Replace code blocks using the editor."
  [parent-comp]
  (let [dn  (rdom/dom-node parent-comp)
        cbs (.querySelectorAll dn "pre > code")]
    (doseq [node cbs]
      (let [content (.-textContent node)
            pre     (.-parentNode node)
            div     (.createElement js/document "div")
            opts    (extract-opts node)]
        (set!
         (.. div -dataset -language)
         (string/replace (:class-list opts) #" run" ""))
        (.replaceChild (.-parentNode pre) div pre)
        (rdom/render [editor content opts] div)))))

(defn format-code-style
  "Format code block class replacing `-` with spaces."
  [style]
  (str "class=\"" (string/replace style #"-" " ") "\""))

(defn to-html
  "Convert Markdown string to formatted HTML."
  [content]
  (-> (string/trim content)
      (md->html :code-style format-code-style)))
