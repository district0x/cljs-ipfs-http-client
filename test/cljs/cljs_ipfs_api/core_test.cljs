(ns cljs-ipfs-api.core-test
  (:require
   [cljs.test :refer [deftest testing is async]]
   [cljs.test :as t]
            [taoensso.timbre :as timbre :refer-macros [log
                                                       trace
                                                       debug
                                                       info
                                                       warn
                                                       error
                                                       fatal
                                                       report]]
            [cljs-ipfs-api.core :as core]))

(deftest defnils-test []
  ;; (is (= (core/nil-patched-defns 'test ['data ['options] ['callback] ['quack] 'mordata]) nil))
  )

(deftest defsignature-test []
  ;;(info (macroexpand '(core/defsignature [add files.add [data [options] [callback] [mordata]]])))
  ;;(is (= (macroexpand '(core/defsignature [add files.add [data [options] [callback] [mordata]]])) nil))
  )
