(ns cljs-ipfs-api.dag
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[dag.put [dagNode options callback]]
   [dag.get [cid [path] [options] callback]]
   [dag.tree [cid [path] [options] callback]]])
