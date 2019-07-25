(defproject district0x/cljs-ipfs-http-client "1.0.2-SNAPSHOT"
  :description "library for calling ipfs HTTP API"
  :url "https://github.com/district0x/cljs-ipfs-http-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[camel-snake-kebab "0.4.0"]
                 [cljs-ajax "0.8.0"]
                 [cljs-node-io "1.1.2"]
                 [district0x/district-format "1.0.6"]
                 [org.clojure/clojurescript "1.10.439"]]

  :doo {:paths {:karma "./node_modules/karma/bin/karma"}}

  :npm {:dependencies [[xmlhttprequest "1.8.0"]
                       [buffer-dataview "0.0.2"]
                       [request "2.88.0"]]
        :devDependencies [[karma "1.7.1"]
                          [karma-chrome-launcher "2.2.0"]
                          [karma-cli "1.0.1"]
                          [karma-cljs-test "0.1.0"]]}

  :codox {:output-path "docs"
          :language :clojurescript
          :project {:name "cljs-ipfs-http-client" :version "1.0.0"}}

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]
                                  [org.clojure/core.async "0.4.490"]]
                   :plugins [[lein-cljsbuild "1.1.7"]
                             [lein-doo "0.1.8"]
                             [lein-npm "0.6.2"]
                             [lein-codox "0.10.7"]]}}

  :deploy-repositories [["snapshots" {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]
                        ["releases"  {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["deploy"]]

  :cljsbuild {:builds [{:id "browser-tests"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "tests-output/browser/tests.js"
                                   :output-dir "tests-output/browser"
                                   :main "tests.runner"
                                   :optimizations :none
                                   :external-config {:devtools/config {:features-to-install :all}}}}
                       {:id "nodejs-tests"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "tests-output/node/tests.js"
                                   :output-dir "tests-output/node"
                                   :main "tests.nodejs-runner"
                                   :target :nodejs
                                   :optimizations :none
                                   :external-config {:devtools/config {:features-to-install :all}}}}]})
