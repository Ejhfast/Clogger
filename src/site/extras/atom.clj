(ns site
  (:use [compojure]))

(defn split-string
  [stri del]
  (seq (.split stri del)))

(defn make-atom-entry
  [base-name {:keys [id title updated link]}]
  [:entry
   [:title title]
   [:id id]
   [:updated
    (reduce str
            (concat (interpose "T" (split-string
                                    (reduce str (take
                                                 19
                                                 (str updated))) " ")) "Z"))]
   [:link {:href link}]])

(defn make-atom
  [base-name title author posts]
  (let [atom-loc (str base-name "/atom.xml")]
    (into
     [:feed {:xmlns "http://www.w3.org/2005/Atom"}
    [:title title]
    [:id atom-loc]
    [:updated "2008-08-08T00:00:02Z"] ;; hardcode date here
    [:link {:rel "self" :href atom-loc :type "application/atom+xml"}]
    [:author
     [:name author]
     [:uri base-name]]]    
   (map make-atom posts))))

(defn atom-feed [base-name title author posts]
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>")
  (html (make-atom base-name author posts)))
