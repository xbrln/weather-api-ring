(defproject  weather-api-ring "0.1.0"
  :description "API to fetch temperature for a city"
  :url "https://github.com/xbrln/weather-api-ring"
  :min-lein-version "2.0.0"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.9.4"]
                 [ring/ring-json "0.5.1"]
                 [compojure "1.6.2"]
                 [clj-http "3.12.3"]
                 [org.clojure/data.json "2.4.0"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.postgresql/postgresql "42.2.23"]
                 [environ "1.2.0"]]
  :main ^:skip-aot weather-api-ring.core
  :uberjar-name "weather-api-ring-standalone.jar"
  :plugins [[lein-environ "1.2.0"]]
  :ring {:handler weather-api-ring.core/app
         :init weather-api-ring.db/migrate}
  :profiles {:uberjar {:aot :all}})
