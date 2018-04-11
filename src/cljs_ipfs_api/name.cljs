(ns cljs-ipfs-api.name
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[name.publish [addr [options callback]]]
   [name.resolve [addr [options callback]]]])
