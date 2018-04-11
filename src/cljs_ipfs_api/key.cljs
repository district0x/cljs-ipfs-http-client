(ns cljs-ipfs-api.key
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[key.gen [name [options] [callback]]]
   [key.list [[options] [callback]]]
   [key.rm [name [callback]]]
   [key.rename [oldName newName [callback]]]
   [key.export [name password [callback]]]
   [key.import [name pem password [callback]]]])
