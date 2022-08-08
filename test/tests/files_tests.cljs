(ns tests.files-tests
  (:require-macros [cljs.test :refer [deftest testing is async]])
  (:require [cljs.test :as t]
            [cljs-ipfs-api.core :as core]
            [cljs-ipfs-api.files :as files]
            ; ["node:buffer" :refer [Blob]]
            ))

; (defn create-blob-nodejs [data]
;   (require "node:buffer" :refer [Blob]))

; (defn to-blob [data]
;   (if (= cljs.core/*target* "nodejs")
;     (create-blob-nodejs data)
;     (create-blob-browser data)))

(defn to-blob [data]
  (js/Blob. [(str data)] {:type "text/plain"}))

; (defn to-blob [data]
;   (Blob. [(str data)] {:type "text/plain"}))

(deftest add-test []
  (async done
         (core/init-ipfs)
         (files/add (to-blob "vladislav baby don't hurt me")
                    (fn [err files]
                      (is (= err nil))
                      (is (= (js->clj (.parse js/JSON files) :keywordize-keys true)
                             {:Name "blob" :Hash "QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu" :Size "36"}))
                      (done)))))

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
         (files/fget "QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu"
                     {:req-opts {:compress true}}
                     (fn [err content]
                       (is (= err nil))
                       (is (> (count content) 0))
                       (done)))))
