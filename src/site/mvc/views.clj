(ns site.mvc.views
  (:use [compojure])
  (:use [site.mvc.controllers])
  (:use [site setup database utilities]))

(def my-meta [:meta {:http-equiv "Content-type" :content "text/html;charset=UTF-8"}] )

(def tags {:xmlns "http://www.w3.org/1999/xhtml" :lang "en" :xml:lang "en"})

(defn head-setup
     [title]
     [:head
      [:title title]
      my-meta
     [:link {:href "/static/style.css" :rel "stylesheet" :type "text/css"}]])

(def nav-bar
     [:div {:id "head"}
      [:h1 *blog-name*]
      [:h3 (link-to "/" "Home") " " (link-to "/index" "Index")]])

;; Forms

(defn comment-form
  [id]
  (form-to [:post "/addcomment"]
    [:br]
    [:input {:name "name" :class "comment-entry " :label "Name" :cols 40}]
    [:br]
    [:textarea {:name "body" :class "comment-entry" :cols 60 :rows 10}]
    [:br]
    (hidden-field :post_id id)
    (submit-button "Submit")))

(defn post-form
  ([action title body]
  (form-to [:post action]
    [:input {:name "title" :cols 70 :value title :style "font-size:16px"}]
    [:br]
    [:textarea {:name "body" :cols 70 :rows 30 :style "font-size:16px"} body]
    [:br]
    (submit-button "Submit")))
  ([action]
     (post-form action "" ""))
  ([action id]
     (post-form action ((get-post id) :title) ((get-post id) :body))))

;; Display Helpers

(defn display-comments
  [post_id session]
  (let [coms (sort-by :id < (get-comments-post post_id))]
    (into [:div {:class "comments"}]
          (mapcat (fn [x]
                    [[:div {:class "comment"}
                      [:p {:class "signiture"} (:name x) ":"]
                      [:p (:body x)]
                      (logged?
                       session
                       (form-to [:post "/deletecomment"]
                                (hidden-field :id (:id x))
                                (submit-button "delete"))
                       nil)]])
                  coms))))

(defn display-posts
  [session posts & [{:keys [title body id read?]
                     :or {title :h2 body :p :id 0 read? true}}]]
  (mapcat (fn [x]
            [[title (x :title)]
             [:pre {:class "post"} (x :body)]
             [:div {:class "post_foot"}
               (subs (str (x :updated)) 0 10) " || "
              (if read?
                (link-to (str "/" (x :id) "/show") "[Read]")
                (link-to "/" "Home"))
              (do-if
               (not read?)
               (display-comments id session)
               [:h4 "Have a comment?"]
               (comment-form id))
              ]
             (logged? session
                      [:p {:id "post_foot"}
                       (form-to [:get (str "/" (x :id) "/update")]
                         (submit-button "update"))
                       (form-to [:post "/delete"]
                         (hidden-field :id (x :id))
                         (submit-button "delete"))]
                      nil)])
          posts))

;; Views

(defn login-view
  [params session]
  (html
   (form-to [:post "/login"]
    (text-field :password)
    (submit-button "Login"))))

(defn show-view
  [params session flash]
  (let [selected-post (get-post (params :id))]
    (html
   (:xhtml-strict doctype)
   [:html
    tags
    (head-setup (selected-post :title))
    (into [:body
           nav-bar
           (if (flash :message)
             [:p {:id "flash"} (flash :message)])]
          (display-posts session
                         (list selected-post)
                         {:title :h2 :body :p :id (params :id) :read? false}))])))
(defn update-view
  [params session]
  [(logged?
   session
   (html
   (post-form "update"
              (params :id)))
   [(flash-assoc :message "Must login to update.")
    (redirect-to "/")])])

(defn new-view
  [params session]
  [(logged? session
    (html (post-form "/new"))
    [(flash-assoc :message "Must login to post.")
      (redirect-to "/")])])

(defn home-view
  [params session flash]
  (html
   (:xhtml-strict doctype)
   [:html
    tags
    (head-setup *blog-name*)
    [:body
     nav-bar
     (if (flash :message)
       [:p {:id "flash"} (flash :message)])
     (if (session :login)
       [:p "Hi, you are currently logged in! "
        (link-to "/logout" "[Logout]")])
     (into
     [:div {:class "left"} ]
     (display-posts session (take 10 (all-posts))))
     [:div {:class "right"}]]]))

(defn index-view
  [params]
  (let [num-s (if (:num params) (:num params) "0")
        posts (take 30 (drop (Integer/parseInt num-s) (all-posts)))]
    (html
     (:xhtml-strict doctype)
     [:html
      tags
      (head-setup *blog-name*)
      (into [:body
             nav-bar
             [:h2 "Index of posts:"]]
            (if (> (count posts) 0)
              (mapcat
               (fn [post]
                 [[:p {:class "post"}
                   (link-to (str "/" (:id post) "/show") (:title post))
                   ]
                  [:p {:class "descrip"}
                   (reduce str (take 125 (:body post)))
                   ]])
               posts)
              [ [:p "No more posts..."]]))])))

(defn not-found
  []
  (html
   [:html
    tags
    (head-setup *blog-name*)
    [:body
     nav-bar
     [:h2 "Page Not Found!"]]]))
