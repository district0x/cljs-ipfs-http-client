(ns cljs-ipfs-api.dht
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[dht.findprovs []]
   [dht.get []]
   [dht.put []]])
