(ns blog.editor
  (:require
   [clojure.string :as string]
   [blog.session :as session]
   [reagent.core :as r]))

(defn codemirror
  [id]
  (let [theme (session/get :theme)
        dark? (= theme "light")
        cm    (.fromTextArea
               js/CodeMirror
               (.getElementById js/document id)
               #js {:mode              "clojure"
                    :theme             (if dark? "default" "gruvbox")
                    :lineNumbers       true
                    :lineWrapping      true
                    :smartIndent       true
                    :autoCloseBrackets true
                    :matchBrackets     true
                    :tabSize           2})]
    ;; (.on cm "change" (fn [_ _]
    ;;                    (reset! state (str ns-str (.getValue cm)))))
    (.setSize cm "auto" "auto")))

(defn editor [value]
  (r/with-let [id     (gensym "editor-")
               mount! #(codemirror id)]
    [:div.code-editor {:ref mount!}
     [:textarea {:id id :value (string/trim value)}]]))
