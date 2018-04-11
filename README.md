# Cljs IPFS-API-NATIVE (DIRECT)

[![Build Status](https://travis-ci.org/district0x/cljs-ipfs-native.svg?branch=master)](https://travis-ci.org/district0x/cljs-ipfs-native)

ClojureScript direct-call implementation of [JavaScript HTTP IPFS client library](https://github.com/ipfs/js-ipfs-api) 

## Installation
```clojure
;; Add to dependencies
[district0x/cljs-ipfs-native "0.0.3-SNAPSHOT"]
```
```clojure
(ns my.app
  (:require 
  [cljs-ipfs-api.core :as icore :refer [init-ipfs]]
  [cljs-ipfs-api.files :as ifiles]))
```

## Usage
So basically, stick with the js-ipfs-api [docs](https://github.com/ipfs/js-ipfs-api#api), all methods there have their kebab-cased version in this library. Also, return values and responses in callbacks are automatically kebab-cased and keywordized. You can provide IPFS instance as an additional first argument to each function, in case you'd need more than one connection:

### Example call
```clojure
(init-ipfs)
;;(init-ipfs {:host "http://127.0.0.1:5001" :endpoint "/api/v0"})

;;fls to avoid clashes with ls from files.ls
(ifiles/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/" (fn [err files]
                                                                      (info [err "ERROR"])
                                                                      (info [files "FILES"])))
;;Files upload on NODEJS see files_test                                                                  
;;Files upload -- reagent
(let [f (atom nil)]
    (fn []
      [:form
       {:on-submit
        (fn [e]
          (.preventDefault e)
          (ifiles/add @f (fn [err files]
                                      (info ["DONE" err files]))))}
       [:input {:type "file"
                :on-change #(let [v (-> % .-target .-files (aget 0))]
                              (reset! f v))}]
       [:input
        {:type "submit"
         :value "Import"}]]))

                                                                      
```

#### cljs.core.async integration
You can also provide an async channel instead of the callback function
