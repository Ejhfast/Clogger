(ns site.start
  (:use [compojure])  
  (:use site.routes)
  (:use site.database))

(defn first-config []
  (do
    (create-blog db)
    (start myserver)))

(defn up []
  (start myserver))

(defn down []
  (stop myserver))
