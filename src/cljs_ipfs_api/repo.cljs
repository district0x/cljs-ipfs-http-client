(ns cljs-ipfs-api.repo
  (:require [cljs-ipfs-api.core :refer-macros [defsignatures]]
            [cljs-ipfs-api.utils :refer [wrap-callback api-call js->cljkk cljkk->js]]))

(defsignatures
  [[repo.gc [[options] [callback]]]
   [repo.stat [[options] [callback]]]
   [repo.version [[callback]]]])
