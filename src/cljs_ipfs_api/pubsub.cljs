(ns cljs-ipfs-api.pubsub
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[pubsub.subscribe [topic options handler callback]]
   [pubsub.unsubscribe [topic handler]]
   [pubsub.publish [topic data callback]]
   [pubsub.ls [topic callback]]
   [pubsub.peers [topic callback]]])
