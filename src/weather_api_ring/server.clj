(ns weather-api-ring.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :refer [wrap-json-response]]
            [weather-api-ring.routes :as routes]))

(def app (wrap-json-response #'weather-api-ring.routes/routes))

(defonce server (atom nil))

(defn start-server
  [port]
  (reset! server
          (jetty/run-jetty #'app
                           {:port port :join? false})))

(defn stop-server
  []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))
