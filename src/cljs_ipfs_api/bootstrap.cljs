(ns cljs-ipfs-api.bootstrap
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[bootstrap.list]
   [bootstrap.add]
   [bootstrap.rm]])
