(ns cljs-ipfs-api.object
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
  [[object.new [[template] [callback]]]
   [object.put [obj [options] [callback]]]
   [object.get [multihash [options] [callback]]]
   [object.data [multihash [options] [callback]]]
   [object.links [multihash [options] [callback]]]
   [object.stat [multihash [options] [callback]]]
   [object.patch.addLink [multihash DAGLink [options callback]] patch-add-link]
   [object.patch.rmLink [multihash DAGLink [options callback]] patch-rm-link]
   [object.patch.appendData [multihash data [options callback]] patch-append-data]
   [object.patch.setData [multihash data [options callback]] patch-set-data]])
