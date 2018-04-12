(ns cljs-ipfs-api.files-test
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
                      ;;"resources/test/ipfs-logo.svg"
                      (fn [err data]
                        (if-not err
                          (files/add  ;;
                           data
                           (fn [err files]
                             (is (= err nil))
                             (is (= files
                                    {:Name "QmP6LozGREM9RWNv7EvER8shCQi1KzwYVKZFnHPNsKGbRd",
                                     :Hash "QmP6LozGREM9RWNv7EvER8shCQi1KzwYVKZFnHPNsKGbRd",
                                     :Size "141584"}
                                    #_{:Name "QmX6d3PikDgpzrAB8xzHhygnELPCDRA4UuuJK1VeCefa48",
                                     :Hash "QmX6d3PikDgpzrAB8xzHhygnELPCDRA4UuuJK1VeCefa48",
                                     :Size "1946"}))
                             (done)))))))))
(deftest ls-test []
  (async done
         (core/init-ipfs)
         (files/fls "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/"
                   (fn [err files]
                     (is (= err nil))
                     (is (= files {:Objects
                                   [{:Hash "/ipfs/QmYwAPJzv5CZsnA625s3Xf2nemtYgPpHdWEz79ojWnPbdG/",
                                     :Links [{:Name "about",
                                              :Hash "QmZTR5bcpQD7cFgTorqxZDYaew1Wqgfbd2ud9QqGPAkK2V",
                                              :Size 1688,
                                              :Type 2}
                                             {:Name "contact",
                                              :Hash "QmYCvbfNbCwFR45HiNP45rwJgvatpiW38D961L5qAhUM5Y",
                                              :Size 200,
                                              :Type 2}
                                             {:Name "help",
                                              :Hash "QmY5heUM5qgRubMDD1og9fhCPA6QdkMp3QCwd4s7gJsyE7",
                                              :Size 322,
                                              :Type 2}
                                             {:Name "quick-start",
                                              :Hash "QmdncfsVm2h5Kqq9hPmU7oAVX2zTSVP3L869tgTbPYnsha",
                                              :Size 1728,
                                              :Type 2}
                                             {:Name "readme",
                                              :Hash "QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB",
                                              :Size 1102,
                                              :Type 2}
                                             {:Name "security-notes",
                                              :Hash "QmTumTjvcYCAvRRwQ8sDRxh8ezmrcr88YFU7iYNroGGTBZ",
                                              :Size 1027,
                                              :Type 2}]}]}))
                     (done)))))
