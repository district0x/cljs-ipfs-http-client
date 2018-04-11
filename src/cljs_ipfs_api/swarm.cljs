(ns cljs-ipfs-api.swarm
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[swarm.addrs [[callback]]]
   [swarm.connect [addr [callback]]]
   [swarm.disconnect [addr [callback]]]
   [swarm.peers [[opts] [ callback]]]])
