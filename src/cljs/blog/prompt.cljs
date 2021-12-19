(ns blog.prompt
  (:require
   [herb.core :refer [<class]]))

(def terminal-icon
  [:svg.jsx-3995436223
   {:width "24"
    :stroke-linejoin "round"
    :shape-rendering "geometricPrecision"
    :fill "none"
    :viewbox "0 0 24 24"
    :stroke "currentColor"
    :stroke-linecap "round"
    :stroke-width "1.5"
    :height "24"}
   [:path {:d "M4 17l6-6-6-6M12 19h8"}]])

(defn cm-styles []
  {:width    "470px"
   :position "fixed"
   :bottom   "15px"
   :left     "20px"
   :height   "32px"
   :margin   "0 auto"
   :border   "var(--code-textarea-border)"
   :border-radius "6px"
   :overflow "hidden"})

;; Convert this using just a css and css format
(defn cm-input []
  {:border "none"
   :height "100%"
   :width  "100%"
   :padding "0 20px"
   :color "var(--text-color)"
   :background "var(--background-color)"})

(defn cm-icon []
  {})

(defn command-bar []
  (let [a 1]
    [:div.command-bar {:class (<class cm-styles)}
     terminal-icon
     [:input.command-input
      {:class (<class cm-input)}]]))
