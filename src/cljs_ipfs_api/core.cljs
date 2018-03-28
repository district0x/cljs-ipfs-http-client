(ns cljs-ipfs-api.core
  (:require [taoensso.timbre :as timbre :refer-macros [log
                                                       trace
                                                       debug
                                                       info
                                                       warn
                                                       error
                                                       fatal
                                                       report]]))

(def *ipfs-instance* (atom nil))

(defn init-ipfs
  ([] (init-ipfs {}))
  ([params]
   (let [i (merge
            {:host "http://127.0.0.1:5001"
             :endpoint "/api/v0/"}
            params)]
     (reset! *ipfs-instance* i)
     i)))

(defn init []
  (info "INIT"))
