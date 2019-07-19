(ns tests.nodejs-runner
  (:require
    [cljs.nodejs :as nodejs]
    [cljs.test :refer [run-tests]]
    [tests.files-tests]))

(nodejs/enable-util-print!)

(defn -main [& _]
  (run-tests 'tests.files-tests))

(set! *main-cli-fn* -main)
