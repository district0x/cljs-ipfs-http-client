(ns cljs-ipfs-api.pin
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[pin.add [hash [options] [callback]]]
   [pin.rm [hash [options] [callback]]]
   [pin.ls [[hash] [options] [callback]]]])
