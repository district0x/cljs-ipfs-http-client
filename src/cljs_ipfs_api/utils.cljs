(ns cljs-ipfs-api.utils
  (:require
   [camel-snake-kebab.core :as cs :include-macros true]
   [camel-snake-kebab.extras :refer [transform-keys]]
   [cljs.core.async :refer [<! >! chan]]
   [cljs-http.client :as http]
   [clojure.string :as string])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;;Shims for NODEJS
(when (= cljs.core/*target* "nodejs")
  (set! js/XMLHttpRequest (js/require "xhr2"))
  (set! js/FormData (.-FormData js/XMLHttpRequest)))

(defn safe-case [case-f]
  (fn [x]
    (cond-> (subs (name x) 1)
      true (string/replace "_" "*")
      true case-f
      true (string/replace "*" "_")
      true (->> (str (first (name x))))
      (keyword? x) keyword)))

(def camel-case (safe-case cs/->camelCase))
(def kebab-case (safe-case cs/->kebab-case))

(def js->cljk #(js->clj % :keywordize-keys true))

(def js->cljkk
  "From JavaScript to Clojure with kekab-cased keywords."
  (comp (partial transform-keys kebab-case) js->cljk))

(def cljkk->js
  "From Clojure with kebab-cased keywords to JavaScript."
  (comp clj->js (partial transform-keys camel-case)))

(defn callback-js->clj [x]
  (if (fn? x)
    (fn [err res]
      (when (and res (aget res "v"))
        (aset res "v" (aget res "v")))                      ;; Prevent weird bug in advanced optimisations
      (x err (js->cljkk res)))
    x))

(defn args-cljkk->js [args]
  (map (comp cljkk->js callback-js->clj) args))

(defn js-apply
  ([this method-name]
   (js-apply this method-name nil))
  ([this method-name args]
   (if (aget this method-name)
     (js->cljkk (apply js-invoke this method-name (args-cljkk->js args)))
     (throw (str "Method: " method-name " was not found in object.")))))

(defn js-prototype-apply [js-obj method-name args]
  (js-apply (aget js-obj "prototype") method-name args))

(defn wrap-callback [f-n]
  (let [callback (fn callback [err res]
                   (if (instance? cljs.core.async.impl.channels/ManyToManyChannel f-n)
                     (go (>! f-n [(js->cljkk err)
                                  (js->cljkk res)]))
                     (if (fn? f-n)
                       (f-n (js->cljkk err)
                            (js->cljkk res))
                       f-n)))]
    callback))

(defn is-blob? [x]
  (not (= js/String (type x))))

(defn http-call [url args params]
  (if-let [cb (:callback params)]
    (go (let [reply
              (<! (http/post url (merge
                                  {:query-params {"arg" (clojure.string/join " " (remove is-blob? args))}
                                   :with-credentials? false}
                                  (when-let [b (first (filter is-blob? args))]
                                    {:multipart-params
                                     [["file" b
                                       ;;[b (clj->js {"knownLength" (.-length b)})]
                                       ]]}))))]
          (if (= (:status reply) 200)
            (cb nil
                (:body reply))
            (cb reply nil))))))

(defn api-call [inst ac args params]
  (http-call (str (:host inst)
                  (:endpoint inst) "/" ac)
             args
             (merge inst
                    {:opts (:options params)
                     :callback (:callback params)})))
