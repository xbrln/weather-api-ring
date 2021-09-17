(ns weather-api-ring.controllers.weather
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [weather-api-ring.models.api-calls :as db]
            [environ.core :refer [env]]))

(defn temperature
  "Get temperature for a given city using external API"
  [city]
  (let [w (client/get
           (str (env :owm-api-url)
                "?q=" city "&units=metric&appid="
                (env :owm-api-app-id)))]
    (get-in (json/read-str (:body w)) ["main" "temp"])))

(defn weather
  "Return a map with temperature for a given city"
  [c]
  (let [t (temperature c)]
    (db/save c t)
    {:temperature t
     :city c
     :datetime (new java.util.Date)}))

(defn all-weather
  "Get all entries for weather api call from db table"
  []
  {:status 200
   :body (db/all)})
