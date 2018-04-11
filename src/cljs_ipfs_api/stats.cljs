(ns cljs-ipfs-api.stats
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[stats.bitswap [[callback]]]
   [stats.bw [[options] [callback]]]
   [stats.repo [[options] [callback]]]])
