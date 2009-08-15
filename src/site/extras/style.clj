(ns site.style
  )

(defn style
  ([nm a-map]
     (let [myks (keys a-map)]
       (str nm "{\n"
            (apply str
                   (map (fn [k]
                          (str "\t" k ":" (a-map k) ";\n")) myks)) "}\n")))
  ([type nm a-map]
     (let [point (cond (= type :class) "."
                       (= type :id) "#"
                       true type)]
       (style (str point nm) a-map))))

(defn style-list
  [& styles]
  [:style {:type "text/css"}
   "\n"
   (reduce str
           (map (fn [args]
                  (apply style args))
                styles))])

(def def-style
     (style-list
      ["html"
       {"background-color" "#FFF"}]
      ["body"
       {"padding-top" "5px"
        "font-size" "20px",
        "font-family" "times"
        "margin" "0px auto 0px auto"
        "width" "850px"
        "color" "#5C5C5C"}]
      ["pre"
       {"white-space" "pre-wrap"
        " white-space" "-moz-pre-wrap !important"
        "  white-space" "-o-pre-wrap"
        "word-wrap" "break-word"
        "font-size" "20px"}]
      ["p"
       {"font-size" "1em"
        "padding" "0px"
        "margin" "0px"}]
      ["h1"
       {"font-family" "times"
        "font-style" "underline"
        "font-size" "4em"
        "color" "#454545"
        "margin" "10px 30px 0px 0px"
        "float" "right"
        "font-height" "2em"
        "position" "relative"}]
      ["h2"
       {"font-family" "times"
        "font-size" "1.5em"
        "color" "#2E2E2E"
        "width" "100%"
        "margin" "20px 0px 0px 0px"}]
      ["h3"
       {"font-family" "times"
        "margin" "15px 0px 0px 0px"
        "float" "left"
        "font-size" "2.0em"
        "color" "#D3D3D3"}]
      ["h3" " a"
       {"text-decoration" "none"
        "color" "inherit"
        "margin" "inherit"}]
      ["h4"
       {"font-family" "times"
        "font-size" "1em"
        "color" "#2E2E2E"
        "margin" "10px 200px 0px 0px"
        "float" "right"}]
      [:id "flash"
       {"background-color" "#CC0033"}]
      [:class "post"
       {"font-size" "1em"
        "font-family" "times"}]
      [:id "head"
       {"width" "100%"
        "height" "70px"
        "margin" "0px 0px 0px 0px"
        "padding" "0px 0px 10px 0px"}]
      [:class "comments"
       {"width" "100%"
        "padding" "0px 0% 5px 0%"
        "margin" "10px 0px 0px 0px"}]
      [:class "comment"
       {"width" "98%"
        "margin" "0px 0px 10px 0px"
        "padding" "1% 2px 0px 1%"
        "background-color" "#F4F4F4"}]
      [:class "signiture"
       {"font-weight" "bold"
        "padding" "0px 0px 10px 0px"}]
      [:class "comment-entry"
       {"font-size" "1em"}]))

