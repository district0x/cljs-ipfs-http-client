(ns tests.files-tests
  (:require-macros [cljs.test :refer [deftest testing is async]])
  (:require [cljs.test :as t]
            [cljs-ipfs-api.core :as core]
            [cljs-ipfs-api.files :as files]
            [cljs.reader]
            ["buffer" :refer [Buffer]]))

(defn to-buffer [data]
  (.from Buffer data))

(deftest add-test []
  (async done
         (core/init-ipfs)
         (files/add (to-buffer "vladislav baby don't hurt me")
                    (fn [err files]
                      (is (= err nil))
                      (is (= (select-keys files [:Hash :Size])
                             {:Hash "QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu" :Size "36"}))
                      (done)))))

(deftest ls-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu"
                    (fn [err files]
                      (is (= err nil))
                      (is (not (empty? files)))
                      (done)))))

(defn parse-ipfs-content [content]
  (-> (re-find #".+?(\{.+\})" content)
      second
      (js->clj :keywordize-keys true)
      cljs.reader/read-string))

(deftest fget-test []
  (async done
         (core/init-ipfs)
         (files/fget "QmU5RLaShDjmXD2qb123Soj3nHKgQn76d8jab8mNp55X1V"
                     {:req-opts {:compress true}}
                     (fn [err content]
                       (is (= err nil))
                       (is (> (count content) 0))
                       (is (= (parse-ipfs-content content) {:this-is "EDN FILE"}))
                       (done)))))

(deftest cat-test []
  (async done
         (core/init-ipfs)
         (files/fcat "QmU5RLaShDjmXD2qb123Soj3nHKgQn76d8jab8mNp55X1V"
                     {:req-opts {:compress true}}
                     (fn [err content]
                       (is (= err nil))
                       (is (= (cljs.reader/read-string content) {:this-is "EDN FILE"}))
                       (done)))))
