(ns blog.utils
  (:require
   ["date-fns" :as date]))

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

