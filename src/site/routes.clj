(ns site.routes
  (:use [compojure])
  (:use [site.mvc views controllers])
  (:use [site.setup]))

(defroutes blog-routes
  (GET "/" (home-view params session flash))
  (GET "/index" (index-view params))
  (GET "/index/:num" (index-view params))
  (GET "/login" (login-view params session))
  (POST "/login" (login-controller params session))
  (GET "/logout" (logout session))
  (GET "/new" (new-view params session))
  (POST "/new" (new-controller params session))
  (GET "/:id/update" (update-view params session))
  (POST "/:id/update" (update-controller params session))
  (GET "/:id/show" (show-view params session flash))
  (POST "/delete" (delete-controller :posts params session))
  (POST "/addcomment" (add-comment params session))
  (POST "/deletecomment" (delete-controller :comments params session))
  (GET "/static/*" (or (serve-file "./static" (params :*)) :next))
  (ANY "/*" (not-found)))

(decorate blog-routes
          (with-session {:type :memory, :expires 600}))

(defserver myserver
  {:port *my-port*} "/*" (servlet blog-routes))
