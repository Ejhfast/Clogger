(ns site.extras.atom
  (:use [compojure]))

(defn split-string
  [stri del]
  (seq (.split stri del)))

(defn make-atom-entry
  [base-name {:keys [id title body updated link]}]
  (let [link (str base-name "/" id "/show")]
    [:entry
     [:title title]
     [:id link]
     [:updated
      (reduce str
              (concat (interpose "T" (split-string
                                      (reduce str (take
                                                   19
                                                   (str updated))) " ")) "Z"))]
     [:link {:href link}]
     [:content {:type "xhtml"} [:div body]]]))

(defn make-atom
  [base-name title author posts]
  (let [atom-loc (str base-name "/atom.xml")]
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
    [:title title]
    [:id atom-loc]
    [:updated "2008-08-08T00:00:02Z"] ;; hardcode date here
    [:link {:rel "self" :href atom-loc :type "application/atom+xml"}]
    [:author
     [:name author]
     [:uri base-name]]
     (map (partial make-atom-entry base-name) posts)]    
   ))

(defn atom-feed [base-name title author posts]
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
  (html (make-atom base-name title author posts)))
