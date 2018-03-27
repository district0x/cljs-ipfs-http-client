(ns cljs-ipfs-api.bitswap
  (:require [taoensso.timbre :as timbre :refer-macros [log
                                                       trace
                                                       debug
                                                       info
                                                       warn
                                                       error
                                                       fatal
                                                       report]]
            [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback js-apply js->cljkk cljkk->js]]))

(defsignatures
  [[bitswap.wantlist []]
   [bitswap.stat []]
   [bitswap.unwant []]])
