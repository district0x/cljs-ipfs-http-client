(ns cljs-ipfs-api.config
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[config.get [[key callback]]]
   [config.set [key value [callback]]]
   [config.replace [config [callback]]]])
