(ns cljs-ipfs-api.misc
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[id [[callback]]]
   [version [[callback]]]
   [ping []]
   [dns [domain [callback]]]
   [stop [[callback]]]])
