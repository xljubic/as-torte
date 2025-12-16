(defproject as-torte "0.1.0-SNAPSHOT"
  :description "Astorte analytics (orders dataset + analytics functions)"
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main as.core
  :profiles {:dev {:dependencies [[midje "1.10.10"]]}}
  :plugins [[lein-midje "3.2.2"]])
