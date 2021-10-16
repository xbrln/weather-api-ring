(ns weather-api-ring.core
  (:require
   [compojure.core :as compojure]
   [compojure.route :as route]
   [environ.core :refer [env]]
   [ring.adapter.jetty :as jetty]
   [ring.middleware.json :refer [wrap-json-response]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response :refer [response]]
   [weather-api-ring.controllers.weather :as w]
   [weather-api-ring.models.api-calls :as db])
  (:gen-class))

(compojure/defroutes
 routes
 (compojure/GET "/" [] (response "weather api wrapper"))
 (compojure/GET "/weather/:city" [city] (response (w/weather city)))
 (compojure/GET "/weather" [] (response (w/all-weather)))
 (compojure/GET "/qp" {qp :query-params} (response qp))
 (route/not-found (response "Not found")))

(def app (-> #'routes
             wrap-json-response
             wrap-params))

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
