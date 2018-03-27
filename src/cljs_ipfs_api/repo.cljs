(ns cljs-ipfs-api.repo
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
  [[repo.gc [[options] [callback]]]
   [repo.stat [[options] [callback]]]
   [repo.version [[callback]]]])
