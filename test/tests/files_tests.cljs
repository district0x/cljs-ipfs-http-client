(ns tests.files-tests
  (:require-macros [cljs.test :refer [deftest testing is async]])
  (:require [cljs.test :as t]
            [cljs-ipfs-api.core :as core]
            [cljs-ipfs-api.files :as files]))

(deftest add-test []
  (async done
         (core/init-ipfs)
         (let [fs (js/require "fs")
               dw (js/require "buffer-dataview")]
           (.readFile fs
                      "resources/test/testfile.jpg"
                      (fn [err data]
                        (if-not err
                          (files/add
                           data
                           (fn [err files]
                             (is (= err nil))
                             (is (= files
                                    {:Name "QmP6LozGREM9RWNv7EvER8shCQi1KzwYVKZFnHPNsKGbRd",
                                     :Hash "QmP6LozGREM9RWNv7EvER8shCQi1KzwYVKZFnHPNsKGbRd",
                                     :Size "141584"}))
                             (done)))))))))
(deftest ls-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/"
                    (fn [err files]
                      (is (= err nil))
                      (is (not (empty? files)))
                      (done)))))

(deftest fget-test []
  (async done
         (core/init-ipfs)
         (files/fget "/ipfs/QmP6LozGREM9RWNv7EvER8shCQi1KzwYVKZFnHPNsKGbRd"
                     {:req-opts {:compress true}}
                     (fn [err content]
                       (is (= err nil))
                       (is (> (count content) 0))
                       (done)))))
