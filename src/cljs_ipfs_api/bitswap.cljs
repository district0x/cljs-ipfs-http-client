(ns cljs-ipfs-api.bitswap
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[bitswap.wantlist []]
   [bitswap.stat []]
   [bitswap.unwant []]])
