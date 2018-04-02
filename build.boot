(set-env!
 ;; :source-paths    #{"src"}
 :resource-paths  #{"resources" "src"}
 ;; :npm-deps {}
 :dependencies '[[org.clojure/clojure "1.9.0"]

                 ;;ENV
                 [com.taoensso/timbre "4.10.0"]
                 ;; [mount "0.1.11"]
                 ;; [org.clojure/core.async "0.3.443"]

                 ;;DATA
                 [camel-snake-kebab "0.4.0"]
                 ;; [com.taoensso/encore "2.91.0"]
                 ;; [org.clojure/core.cache "0.6.3"]
                 ;; [org.clojure/core.memoize "0.5.6" :exclusions [org.clojure/core.cache]]
                 ;;CLJS
                 [org.clojure/clojurescript "1.9.946"]
                 [cljs-node-io "0.5.0"]
                 ;; [io.nervous/kvlt "0.1.4"]
                 [cljs-http "0.1.45"]

                 ;;DEV
                 [doo "0.1.8" :scope "test"]
                 [adzerk/bootlaces    "0.1.13" :scope "test"]
                 [samestep/boot-refresh "0.1.0" :scope "test"]
                 [adzerk/boot-cljs          "2.1.4"  :scope "test"];;:exclusions [org.clojure/clojurescript]
                 [adzerk/boot-cljs-repl     "0.3.3"      :scope "test"]
                 [adzerk/boot-reload        "0.5.2"      :scope "test"]
                 ;; [pandeiro/boot-http        "0.8.3"      :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.12"     :scope "test"]
                 [weasel                    "0.7.0"      :scope "test"]
                 [crisptrutski/boot-cljs-test "0.3.5-SNAPSHOT" :scope "test"]
                 ;; [org.martinklepsch/boot-garden "1.3.2-0" :scope "test"]
                 [binaryage/dirac "1.1.3" :scope "test"]
                 [powerlaces/boot-cljs-devtools "0.2.0" :scope "test"]
                 [binaryage/devtools "0.9.4"]
                 ])

  (def +version+ "0.0.1-SNAPSHOT")

(require
 '[samestep.boot-refresh :refer [refresh]]
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 ;; '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 ;; '[org.martinklepsch.boot-garden :refer [garden]]
 '[powerlaces.boot-cljs-devtools :refer [cljs-devtools dirac]]
 '[boot.git :refer [last-commit]]
 '[adzerk.bootlaces :refer :all]
 )

(bootlaces! +version+)

(deftask cljs-env []
  (task-options! cljs {:compiler-options {:target :nodejs
                                          ;; :install-deps true
                                          ;; :npm-deps {:ipfs-api "18.1.1"}
                                          }})
  identity)

(deftask build []
  (comp (speak)
     ;; (npm-deps)
     (cljs-env)
     (cljs)))

(deftask run []
  (comp
   (watch)
   (cljs-repl)
   (build)))


(deftask production []
  (task-options! cljs {:optimizations :advanced}
                 ;; garden {:pretty-print false}
                 )
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 reload {;;:on-jsload 'cljs-ipfs.core/init
                         :asset-path "public"})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
     (run)))


(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  identity)

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
     (cljs-env)
     (test-cljs :js-env :node;;:phantom
                :exit?  true)))

(deftask auto-test []
  (comp (testing)
     (watch)
     (cljs-env)
     (test-cljs :js-env :node;;:phantom
                )))

(deftask auto-test-chrome []
  (comp (testing)
     (watch)
     (cljs-env)
     (test-cljs :js-env :chrome-headless;;:phantom
                )))

(task-options!
 ;; sift {:include #{#"\.jar$"}}
 ;; push {:repo           "deploy"
 ;;       :ensure-branch  "master"
 ;;       :ensure-clean   true
 ;;       :ensure-tag     (last-commit)
 ;;       :ensure-version +version+}
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
