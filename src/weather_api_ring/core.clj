(ns weather-api-ring.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :as comp]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [environ.core :refer [env]]
            [weather-api-ring.controllers.weather :as w]
            [weather-api-ring.models.api-calls :as db])
  (:gen-class))

(comp/defroutes
 routes
 (comp/GET "/" [] (response "weather api wrapper"))
 (comp/GET "/weather/:city" [city] (response (w/weather city)))
 (comp/GET "/weather" [] (response (w/all-weather)))
 (route/not-found (response "Not found")))

(def app (wrap-json-response #'routes))

(defonce server (atom nil))

(defn start-server
  [port]
  (reset! server (jetty/run-jetty #'app {:port port :join? false})))

(defn stop-server
  []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

(defn -main
  [& args]
  (db/migrate)
  (let [port (Integer. (env :port))]
    (start-server port)))

(comment
  (start-server 3000))
