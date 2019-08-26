(ns cljs-ipfs-api.utils
  (:require [ajax.core :as ajax :refer [POST]]
            [camel-snake-kebab.core :as cs :include-macros true]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [cljs.core.async :as async :refer [<! >! chan]]
            [clojure.string :as string]
            [district.format :as format])
  (:require-macros [cljs.core.async.macros :refer [go]]))

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

(defn web-http-call [url args {:keys [:opts :callback] :as params}]
  (POST (format/format-url url (merge
                                (let [args (remove is-blob? args)]
                                  (when-not [empty? args]
                                    {"arg" (clojure.string/join " " args)}))
                                opts))
      (merge {:handler (fn [response] (callback nil response))
              :error-handler (fn [err] (callback err nil))
              :response-format (ajax/raw-response-format)}
             (when-let [b (first (filter is-blob? args))]
               {:body (doto
                          (js/FormData.)
                        (.append "file" b))}))))

(defn node-http-call [url args params]
  (if-let [cb (:callback params)]
    (let [rm (js/require "request")
          fs (js/require "fs")
          on-done (fn [err oresp obody]
                    (let [err (js->cljkk err)
                          resp (js->cljkk oresp)]

                      (cond
                        err
                        (cb err nil)

                        (= (.-statusCode resp) 200)
                        (if (get-in params [:opts :binary?])
                          ;; if :binary? option is set, then obody will be a Buffer with binary data
                          ;; so just return it as it is
                          (cb nil obody)

                          ;; response body is going to be json
                          (cb nil
                             (try
                               (.parse js/JSON (js->cljkk obody))
                               (catch js/SyntaxError e
                                 (js->cljkk obody)))))

                        :else (cb (.-statusMessage resp) nil))))
          form (when-let [b (first (filter is-blob? args))]
                 {:formData
                  {:file b}})
          req-options (clj->js (merge {:url url
                                       :qs (merge {:arg (clojure.string/join " " (remove is-blob? args))}
                                                  (get-in params [:opts :req-opts]))}
                                      ;; if we have :binary? option lets set encoding nil
                                      ;; nodejs doc says that a post with encoding nil will return
                                      ;; the response body in a Buffer instead of a string
                                      (when (get-in params [:opts :binary?]) {:encoding nil})
                                      form))
          req (.post rm req-options on-done)]
      (when-let [out (get-in params [:opts :pipe-to])]
        (.pipe req out)))))

(def http-call
  (if (= cljs.core/*target* "nodejs")
    node-http-call
    web-http-call))

(defn api-call [inst func args params]
  (http-call (str (:host inst)
                  (:endpoint inst) "/" func)
             args
             (merge inst
                    {:opts (:options params)
                     :callback (:callback params)})))
