(set-env!
 :resource-paths  #{"resources" "src"}
 :dependencies '[[org.clojure/clojure "1.9.0"]

                 ;;ENV
                 [com.taoensso/timbre "4.10.0"]

                 ;;DATA
                 [camel-snake-kebab "0.4.0"]

                 ;;CLJS
                 [org.clojure/clojurescript "1.10.439"]
                 [cljs-node-io "1.1.2"]
                 [cljs-http "0.1.45"]

                 ;;DEV
                 [adzerk/boot-cljs              "2.1.5"  :scope "test"]
                 [adzerk/boot-cljs-repl         "0.4.0"  :scope "test"]
                 [adzerk/boot-reload            "0.6.0"  :scope "test"]
                 [adzerk/bootlaces              "0.1.13" :scope "test"]
                 [binaryage/devtools            "0.9.10"]
                 [binaryage/dirac               "1.2.42" :scope "test"]
                 [boot-codox                    "0.10.5" :scope "test"]
                 [boot-deps                     "0.1.9"]
                 [cider/piggieback              "0.3.10" :scope "test"]
                 [crisptrutski/boot-cljs-test   "0.3.5-SNAPSHOT" :scope "test"]
                 [doo                           "0.1.11" :scope "test"]
                 [nrepl                         "0.5.0"  :scope "test"]
                 [powerlaces/boot-cljs-devtools "0.2.0"  :scope "test"]
                 [samestep/boot-refresh         "0.1.0"  :scope "test"]
                 [weasel                        "0.7.0"  :scope "test"]])


(def +version+ "0.0.5-SNAPSHOT")


(require
 '[samestep.boot-refresh :refer [refresh]]
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs prep-cljs-tests]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools dirac]]
 '[boot.git :refer [last-commit]]
 '[adzerk.bootlaces :refer :all]
 '[codox.boot :refer [codox]])


(bootlaces! +version+)


(task-options!
 codox {:version +version+
        :description "IPFS library for node and web"
        :name "IPFS API"
        :language :clojurescript})


(deftask cljs-env []
  (task-options! cljs {:compiler-options {:target :nodejs}})
  identity)


(deftask build []
  (comp
   (speak)
   (cljs-env)
   (cljs)))


(deftask run []
  (comp
   (watch)
   (cljs-repl)
   (build)))


(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)


(deftask development []
  (task-options!
   cljs {:optimizations :none}
   reload {:asset-path "public"})
  identity)


(deftask dev
  "Alias to run application in development mode. [recommended]"
  []
  (comp (development) (run)))


(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  ;; (task-options! test-cljs {:doo-opts
  ;;                           {:paths {:node "node  --inspect --debug-brk"}}})
  identity)


;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)


(deftask test []
  (comp
   (testing)
   (cljs-env)
   (test-cljs :js-env :node
              :exit?  true)))


(deftask auto-test []
  (comp
   (testing)
   (watch)
   (cljs-env)
   (test-cljs :js-env :node)))


(deftask auto-test-chrome []
  (comp
   (testing)
   (watch)
   (cljs-env)
   (test-cljs :js-env :chrome-headless)))


(task-options!
 pom  {:project     'district0x/cljs-ipfs-native
       :version     +version+
       :description "Native ClojureScript js-ipfs-api implementation."
       :url         "https://github.com/district0x/cljs-ipfs-native"
       :scm         {:url "https://github.com/district0x/cljs-ipfs-native"}
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})


(deftask package []
  (comp
   (production)
   (cljs :compiler-options {:target :nodejs})
   (build-jar)))


(deftask deploy []
  (comp
   (production)
   (cljs :compiler-options {:target :nodejs})
   (build-jar)
   (push-snapshot)))


(deftask deploy-release []
  (comp
   (production)
   (cljs :compiler-options {:target :nodejs})
   (build-jar)
   (push-release)))
