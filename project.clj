(defproject
 weather-api-ring "0.1.0"
 :description "API to fetch temperature for a city"
 :url "https://github.com/xbrln/weather-api-ring"
 :dependencies [[clj-http "3.12.3"]
                [com.github.seancorfield/next.jdbc "1.2.731"]
                [compojure "1.6.2"]
                [environ "1.2.0"]
                [org.clojure/clojure "1.10.3"]
                [org.clojure/data.json "2.4.0"]
                [org.postgresql/postgresql "42.3.0"]
                [ring "1.9.4"]
                [ring/ring-json "0.5.1"]]
 :min-lein-version "2.0.0"
 :main ^:skip-aot weather-api-ring.core

 :plugins [[lein-environ "1.2.0"]]
 :ring {:handler weather-api-ring.core/app
        :init weather-api-ring.db/migrate}
 :profiles
 {:uberjar
  {:aot :all
   :uberjar-name "app-standalone.jar"}

  :dev [:project/dev :profiles/dev]
  :test [:project/test :profiles/test]

  :project/dev
  {:plugins []}
  :project/test {}
  :profiles/dev {}
  :profiles/test {}})
