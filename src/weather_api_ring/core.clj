(ns weather-api-ring.core
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [weather-api-ring.db :as db]
            [environ.core :as env])
  (:gen-class))

; deploy to heroku

(defn temperature
  [city]
  (def w (client/get
          (str (env/env :owm-api-url) "?q=" city "&units=metric&appid=" (env/env :owm-api-app-id))))
  (get-in (json/read-str (:body w)) ["main" "temp"]))

(defn weather
  [request]
  (let [c (get-in request [:path-params :city])
        t (temperature c)]
    (db/save c t)
    {:status 200
     :body {:temperature t
            :city c
            :datetime (new java.util.Date)}}))

(defn all-weather
  [_]
  {:status 200
   :body (db/all)})

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["weather/:city" weather]
     ["weather" all-weather]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn start
  [port]
  (jetty/run-jetty #'app {:port port
                          :join? false}))

(defn -main
  [& args]
  (db/migrate)
  (let [port (Integer. (env/env :port))]
    (start port)))
