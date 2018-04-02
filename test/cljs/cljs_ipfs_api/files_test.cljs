(ns cljs-ipfs-api.files-test
  (:require-macros [cljs.test :refer [deftest testing is async]])
  (:require [cljs.test :as t]
            [taoensso.timbre :as timbre :refer-macros [log
                                                       trace
                                                       debug
                                                       info
                                                       warn
                                                       error
                                                       fatal
                                                       report]]
            [cljs-ipfs-api.core :as core]
            [cljs-ipfs-api.files :as files]))

(deftest add-test []
  (async done
         (core/init-ipfs)
         (let [fs (js/require "fs")
               dw (js/require "buffer-dataview")]
           (.readFile fs "resources/test/ipfs-logo.svg"
                      (fn [err data]
                        (if-not err
                          (files/add (new dw data) (fn [err files]
                                            (is (= err nil))
                                            (info ["DONE" err files])
                                            (done)))))))))
#_(deftest ls-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/"
                   (fn [err files]
                     (is (= err nil))
                     (info ["DONE" err files])
                     (done)))))
