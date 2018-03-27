# Cljs IPFS-API

[![Build Status](https://travis-ci.org/district0x/cljs-ipfs-api.svg?branch=master)](https://travis-ci.org/district0x/cljs-ipfs-api)

ClojureScript wrapper for [JavaScript HTTP IPFS client library](https://github.com/ipfs/js-ipfs-api) 

## Installation
```clojure
;; Add to dependencies
[district0x/cljs-ipfs-api "0.0.13-SNAPSHOT"]
```
```clojure
(ns my.app
  (:require 
  [cljs-ipfs-api.core :as icore :refer [init-ipfs-node]] ;;Or init-ipfs-web for web
  [cljs-ipfs-api.files :as ifiles]))
```

## Usage
So basically, stick with the js-ipfs-api [docs](https://github.com/ipfs/js-ipfs-api#api), all methods there have their kebab-cased version in this library. Also, return values and responses in callbacks are automatically kebab-cased and keywordized. You can provide IPFS instance as an additional first argument to each function, in case you'd need more than one connection:

### Example call
```clojure
(init-ipfs-node "/ip4/127.0.0.1/tcp/5001")
;;fls to avoid clashes with ls from files.ls
(ifiles/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/" (fn [err files]
                                                                      (info [err "ERROR"])
                                                                      (info [files "FILES"])))
```

#### cljs.core.async integration
You can also provide an async channel instead of the callback function
