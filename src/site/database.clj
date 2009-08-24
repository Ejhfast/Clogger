(ns site.database
  (:use [compojure])
  (:use [clojure.contrib.sql])
  (:use [site.setup]))

;; database setup

(def db {:classname "com.mysql.jdbc.Driver" :subprotocol "mysql"
         :subname (str "//" *db-host* ":" *db-port* "/" *db-name*)
         :user *db-user* :password *db-password*})

(defn create-blog
  "Setup my database" [db]
  (with-connection db
    (create-table
     :posts
     [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
     [:title "varchar(255)"]
     [:body :text]
     [:updated :timestamp "DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"])
    (create-table
     :comments
     [:id :integer "PRIMARY KEY" "AUTO_INCREMENT"]
     [:name "varchar(255)"]
     [:body :text]
     [:post_id :integer])))

(defn drop-blog
  "Drop all tables" [db]
  (with-connection db
    (try
     (do
       (drop-table :posts)
       (drop-table :comments))   
     (catch Exception _))))

;; post management

(defn ask-sql
  "Returns a seq of maps with the contents of an sql command."
  [db query]
  (with-connection db
    (with-query-results rs [query]
      (doall (map identity rs)))))

(defn SELECT
  "A generic select function."
  ([db what place]
     (ask-sql db (str "select " what " from " place)))
  ([db what place where]
     (ask-sql db (str "select " what " from " place " where " where))))

(defn INSERT
  "A generic insert function."
  [db table map]
  (with-connection db
    (insert-values table
                   (keys map)
                   (vals map))))
(defn UPDATE
  "A generic update function -- based on id."
  [db table id a-map]
  (with-connection db
    (update-values table ["id=?" id] a-map)))

(defn REMOVE
  "A generic delete function -- based on id."
  [db table id]
  (with-connection db
    (delete-rows table ["id=?" id])))

(defn all-posts
  "Retreive all posts"
  []
  (sort-by :id  > (SELECT db "*" "posts")))

(defn get-post
  "Get a post by id."
  [id]
  (first (SELECT db "*" "posts" (str "id=" id))))

(defn all-titles
  "Retreive all titles."
  []
  (map #(% :title) (all-posts)))

(defn get-comments-post
  "Get comments by post_id"
  [id]
  (SELECT db "*" "comments" (str "post_id=" id)))

