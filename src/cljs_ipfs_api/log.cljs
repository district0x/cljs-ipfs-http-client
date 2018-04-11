(ns cljs-ipfs-api.log
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[log.ls [[callback]]]
   [log.tail [[callback]]]
   [log.level [subsystem level [options callback]]]])
