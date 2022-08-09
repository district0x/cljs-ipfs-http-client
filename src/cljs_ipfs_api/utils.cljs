(ns cljs-ipfs-api.utils
  (:require
            [camel-snake-kebab.core :as cs :include-macros true]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [cljs.core.async :as async :refer [<! >! chan]]
            [clojure.string :as string]
            [district.format :as format]
            ["form-data" :as FormData]
            ["buffer" :refer [Buffer]]
            ["axios" :as axios])
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

(defn safe-parse-js->cljkk [object]
  (try
    (js->cljkk object)
    (catch js/SyntaxError e
      object)))

(defn wrap-callback [f-n]
  (let [callback (fn callback [err res]
                   (if (instance? cljs.core.async.impl.channels/ManyToManyChannel f-n)
                     (go (>! f-n [(js->cljkk err)
                                  (js->cljkk res)]))
                     (if (fn? f-n)
                       (f-n err res)
                       f-n)))]
    callback))

(defn is-buffer? [x]
  (= Buffer (type x)))

(def last-response (atom nil))

(defn axios-call [url args {:keys [:opts :callback] :as params}]
  (let [opts (dissoc opts :req-opts) ; req-opts are used on Node.js platform (back-end) AJAX library
        blobless-args (remove is-buffer? args)
        basic-opts (when-not (empty? blobless-args) {"arg" (clojure.string/join " " blobless-args)})
        url-extra-opts (merge basic-opts opts)
        url-with-params (format/format-url url url-extra-opts)
        possible-buffer (first (filter is-buffer? args))
        request-body (when possible-buffer (doto (FormData.) (.append "file" possible-buffer)))
        update-response-run-callback (fn [response]
                                       (let [response-data (.-data response)]
                                         (reset! last-response response-data)
                                         (callback nil (js->cljkk response-data))))]
    (-> (axios (clj->js {:method "POST" :url url-with-params :data request-body}))
        (.then update-response-run-callback)
        (.catch #(callback % nil)))))


(defn api-call [inst func args {:keys [:options :opts] :as params}]
  (axios-call (str (:host inst)
                  (:endpoint inst) "/" func)
             args
             (merge inst
                    {:opts (or opts options)
                     :callback (:callback params)})))
