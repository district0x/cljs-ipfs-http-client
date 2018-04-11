(ns cljs-ipfs-api.block
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[block.get [cid [options] [callback]]]
   [block.put [block cid [callback]]]
   [block.stat [cid [options]]]])
