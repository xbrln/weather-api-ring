(ns weather-api-ring.weather
  (:require [ring.util.response :refer [response]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [weather-api-ring.db :as db]
            [environ.core :refer [env]]))

(defn temperature
  [city]
  (let [w (client/get
           (str (env :owm-api-url)
                "?q=" city "&units=metric&appid="
                (env :owm-api-app-id)))]
    (get-in (json/read-str (:body w)) ["main" "temp"])))

(defn weather
  [c]
  (let [t (temperature c)]
    (db/save c t)
    (response {:temperature t
               :city c
               :datetime (new java.util.Date)})))

(defn all-weather
  []
  (response (db/all)))
