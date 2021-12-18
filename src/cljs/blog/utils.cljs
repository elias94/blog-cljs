(ns blog.utils
  (:require
   ["date-fns" :as date]
   [goog.object]))

(defn getElementById [id]
  (.getElementById js/document id))

(defn format-date
  "Returns formatted date form post."
  [date]
  (date/format (.parse js/Date date) "MMM d, yyyy"))

(defn dark-theme?
  "Returns true if the preferred color-scheme of the user
  is currently set to dark."
  []
  (and
   (.-matchMedia js/window)
   (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))))

(defn obj->clj
  "Convert a js object to Clojurescript map."
  [obj]
  (if (goog/isObject obj)
    (-> (fn [result key]
          (let [v (goog.object/get obj key)]
            (if (= "function" (goog/typeOf v))
              result
              (assoc result (keyword key) (obj->clj v)))))
        (reduce {} (.getKeys ^js goog/object obj)))
    obj))

(defn in?
  "Returns true if coll contains el."
  [coll el]
  (some #(= el %) coll))
