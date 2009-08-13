(ns site.mvc.controllers
  (:use [compojure])
  (:use [site database setup utilities])
  (:use [site.mvc.views]))

(defn login-controller
  [params session]
  (if (= (params :password) *password*)
    [(session-assoc :login true)
     (redirect-to "/")]
    [(flash-assoc :message "Incorrect Password.")
     (redirect-to "/")]))
 
(defn logout
  [session]
  [(session-assoc :login false)
  (redirect-to "/")])

(defn add-comment
  [params session]
  (INSERT db :comments {:name (:name params)
                        :body (:body params)
                        :post_id (:post_id params)})
  (if (:post_id params)
     (redirect-to (str "/" (:post_id params) "/show"))
     (redirect-to "/")))


(defn update-controller
  [params session]
  [(logged? session
   (UPDATE db :posts (params :id) {:title (params :title) :body (params :body)})
   (flash-assoc :message "Must login to update."))
  (redirect-to "/")])

(defn new-controller
  [params session]
  [(logged? session
    (INSERT db :posts {:title (params :title) :body (params :body)})
    (flash-assoc :message "Must log in to post."))
   (redirect-to "/")])

(defn delete-controller
  [where params session]
  [(logged? session
   (REMOVE db where (params :id))
   (flash-assoc :message "Must login to delete."))
  (redirect-to "/")])
