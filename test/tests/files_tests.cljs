(ns tests.files-tests
  (:require-macros [cljs.test :refer [deftest testing is async]])
  (:require [cljs.test :as t]
            [cljs-ipfs-api.core :as core]
            [cljs-ipfs-api.utils :refer [to-buffer]]
            [cljs-ipfs-api.files :as files]
            [cljs.reader]))

(deftest add-test []
  (async done
         (core/init-ipfs)
         (files/add (to-buffer "vladislav baby don't hurt me")
                    (fn [err files]
                      (is (= err nil))
                      (is (= (select-keys files [:Hash :Size])
                             {:Hash "QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu" :Size "36"}))
                      (done)))))

(deftest ls-file-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmbAmvPFuGeiTXzpyFDSRSkcaoJZuhprsMybkXZpJSdPcu"
                    (fn [err files]
                      (is (= err nil))
                      (is (not (empty? files)))
                      (done)))))

(deftest ls-folder-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmTeW79w7QQ6Npa3b1d5tANreCDxF2iDaAPsDvW6KtLmfB"
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
         (files/add (to-buffer "{:this-is \"EDN FILE\"}")
                    (fn [err files]
                      (let [hash (:Hash files)]
                        (files/fget hash
                                    {}
                                    (fn [err content]
                                      (is (= err nil))
                                      (is (> (count content) 0))
                                      (is (= (parse-ipfs-content content) {:this-is "EDN FILE"}))
                                      (done))))))))

(deftest fget-args-test []
  (async done
         (core/init-ipfs)
         (files/add (to-buffer "{:this-is \"EDN FILE\"}")
                    (fn [err files]
                      (let [hash (:Hash files)]
                        (files/fget hash
                                    {:compress true}
                                    (fn [err content]
                                      (is (= err nil))
                                      (is (> (count content) 0))
                                      (is (< (count content) 1000))
                                      (done))))))))

(deftest fget-binary-test []
  (async done
         (core/init-ipfs)
         (files/add (to-buffer "{:this-is \"EDN FILE\"}")
                    (fn [err files]
                      (let [hash (:Hash files)]
                        (files/fget hash
                                    {:binary? true}
                                    (fn [err content]
                                      (is (= err nil))
                                      (is (= (type content) (type (js/ArrayBuffer.))))
                                      (is (= (parse-ipfs-content (.decode (js/TextDecoder. "utf-8") content)) {:this-is "EDN FILE"}))
                                      (done))))))))

(deftest cat-test []
  (async done
         (core/init-ipfs)
         (files/add (to-buffer "{:this-is \"EDN FILE\"}")
                    (fn [err files]
                      (let [hash (:Hash files)]
                        (files/fcat hash
                                    {:req-opts {:compress true}}
                                    (fn [err content]
                                      (is (= err nil))
                                      (is (= (cljs.reader/read-string content) {:this-is "EDN FILE"}))
                                      (done))))))))
