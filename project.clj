(defproject
 weather-api-ring "0.1.0"
 :description "API to fetch temperature for a city"
 :url "https://github.com/xbrln/weather-api-ring"
 :min-lein-version "2.0.0"
 :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
           :url "https://www.eclipse.org/legal/epl-2.0/"}
 :dependencies [[clj-http "3.12.3"]
                [compojure "1.6.2"]
                [environ "1.2.0"]
                [org.clojure/clojure "1.10.3"]
                [org.clojure/data.json "2.4.0"]
                [org.clojure/java.jdbc "0.7.12"]
                [org.postgresql/postgresql "42.3.0"]
                [ring "1.9.4"]
                [ring/ring-json "0.5.1"]]
 :main ^:skip-aot weather-api-ring.core
 :uberjar-name "weather-api-ring-standalone.jar"
 :plugins [[lein-environ "1.2.0"]]
 :ring {:handler weather-api-ring.core/app
        :init weather-api-ring.db/migrate}
 :profiles {:uberjar {:aot :all}
            :dev [:project/dev :profiles/dev]
            :prod [:project/prod :profiles/prod]
            :profiles/dev  {}
            :profiles/prod {}
            :project/dev {:resource-paths ["resource-dev"]
                          :dependencies [[expectations "2.1.10"]]}
            :project/prod {:resource-paths ["resource-prod"]}})

; :profiles {:dev [:project/dev :profiles/dev]
;            :test [:project/test :profiles/test]
;            ;; only edit :profiles/* in profiles.clj
;            :profiles/dev  {}
;            :profiles/test {}
;            :project/dev {:source-paths ["src" "tool-src"]
;                          :dependencies [[midje "1.6.3"]]
;                          :plugins [[lein-auto "0.1.3"]]}
;            :project/test {}}
