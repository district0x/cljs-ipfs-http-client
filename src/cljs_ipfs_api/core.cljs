(ns cljs-ipfs-api.core)

(def ^:dynamic *ipfs-instance* (atom nil))

(defn get-instance [] @*ipfs-instance*)

(defn init-ipfs
  ([] (init-ipfs {}))
  ([params]
   (let [instance (merge {:host "http://host-machine:5001" :endpoint "/api/v0"} params)]
     (if (nil? @*ipfs-instance*)
       (reset! *ipfs-instance* instance)
       (println "Not initing, already there"))
     instance)))
