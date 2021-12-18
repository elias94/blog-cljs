(ns blog.editor
  (:require
   [clojure.string :as string]
   [blog.scittle.core :refer [eval-string]]
   [blog.session :as session]
   [reagent.core :as r]))

(defn codemirror
  [id state language opts]
  (let [
        theme (session/get :theme)
        dark? (= theme "light")
        cm    (.fromTextArea
               js/CodeMirror
               (.getElementById js/document id)
               #js {:mode              language
                    :theme             "gruvbox"
                    :readOnly          (:readonly opts)
                    :lineNumbers       true
                    :lineWrapping      true
                    :smartIndent       true
                    :autoCloseBrackets true
                    :matchBrackets     true
                    :tabSize           2})]
    (.on cm "change" #(reset! state (.getValue cm)))
    (.setSize cm "auto" "auto")))

(defn eval-state [state]
  (try {:output (eval-string @state)}
       (catch :default e
         {:output (.-message e)
          :error  e})))

(defn render-output [state]
  (let [res (eval-state state)
        error? (contains? res :error)]
    [:div.code-results
     [:pre
      [:div (if error? "Error:" "Output:")]
      [:div (str (:output res))]]]))

(defn on-editor-mount [id state el opts]
  (let [language (.. (.-parentNode el) -dataset -language)]
    (codemirror id state language opts)))

(defn editor [value opts]
  (r/with-let [id       (gensym "editor-")
               init-val (string/trim value)
               state    (r/atom value)
               mount!   #(on-editor-mount id state % opts)]
    [:div.code-editor {:ref mount!}
     [:textarea {:id id} init-val]
     (when (:runnable opts)
       [render-output state])]))
