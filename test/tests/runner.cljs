(ns tests.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [tests.files-tests]))

(enable-console-print!)

(doo-tests 'tests.files-tests)
