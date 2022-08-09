# cljs-ipfs-http-client

ClojureScript library for calling the [IPFS HTTP API](https://docs.ipfs.io/reference/api/http/) <br>
<br>

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/district0x/cljs-ipfs-http-client/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/district0x/cljs-ipfs-http-client/tree/master)

## Installation

Add to dependencies: <br>
[![Clojars Project](https://img.shields.io/clojars/v/district0x/cljs-ipfs-http-client.svg)](https://clojars.org/district0x/cljs-ipfs-http-client)

Using:

```clojure
(ns my.app
  (:require [cljs-ipfs-api.core :as icore :refer [init-ipfs]]
            [cljs-ipfs-api.files :as ifiles]))
```

## Documentation
https://district0x.github.io/cljs-ipfs-http-client/

## Usage

So basically, stick with the js-ipfs-api [docs](https://github.com/ipfs/js-ipfs-api#api), all methods there have their kebab-cased version in this library. Also, return values and responses in callbacks are automatically kebab-cased and keywordized. You can provide IPFS instance as an additional first argument to each function, in case you'd need more than one connection.

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

# New build, test and release commands

As this library is meant to work both in browsers and on Node.js (server), it must be tested on both as well.
Additionally CI runs the tests slightly different way, so that's the 3rd test environment.
  - it's still in browser, but CI gets success vs failure depending on the
  - browser process exit code so a bit of extra work is needed to get it out from JS (Karma is used for that)

## Node.js

1. Build: `npx shadow-cljs compile test-node`
2. Tests: `node out/node-tests.js`

## Browser

1. Build: `npx shadow-cljs watch test-browser`
2. Tests: http://d0x-vm:6502

## CI (Headless Chrome, Karma)

1. Build: `npx shadow-cljs compile test-ci`
2. Tests:
    ```
    CHROME_BIN=`which chromium-browser` npx karma start karma.conf.js --single-run
    ```

#### inspect on headless chrome on another chrome instance

1. Run headless chrome: `chromium-browser --headless --remote-debugging-port=9222 --remote-debugging-address=0.0.0.0 --allowed-origins="*" https://chromium.org`
2. Open `chrome://inspect/#devices` and configure remote target with *IP ADDRESS* (hostname doesn't work)

## Build & release with `deps.edn` and `tools.build`

1. Build: `clj -T:build jar`
2. Release: `clj -T:build deploy`
