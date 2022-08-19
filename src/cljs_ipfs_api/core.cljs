(ns cljs-ipfs-api.core)

(def ^:dynamic *ipfs-instance* (atom nil))

(defn get-instance [] @*ipfs-instance*)

(defn init-ipfs
  ([] (init-ipfs {}))
  ([params]
   (let [instance (merge {:host "http://localhost:5001" :endpoint "/api/v0"} params)]
     (reset! *ipfs-instance* instance)
     instance)))
