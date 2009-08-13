(ns site.utilities)

(defmacro logged?
  [session if-true not-true]
  `(if (= (~session :login) true)
     ~if-true
     ~not-true))

(defmacro do-if
  [eval & done]
  `(if ~eval
     [:breaker
      ~@done]))
