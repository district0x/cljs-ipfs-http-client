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

#_(deftest add-test []
  (async done
         (core/init-ipfs)
         (let [fs (js/require "fs")
               file (.createReadStream fs "resources/test/testfile.jpg")]
           (files/add file (fn [err files]
                             (is (= err nil))
                             (info ["DONE" err files])
                             (done))))))
(deftest ls-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/"
                   (fn [err files]
                     (is (= err nil))
                     (info ["DONE" err files])
                     (done)))))
