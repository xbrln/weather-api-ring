(ns weather-api-ring.routes
  (:require [compojure.core :as comp]
            [compojure.route :as route]
            [weather-api-ring.weather :as w]
            [ring.util.response :refer [response]]))

(comp/defroutes
 routes
 (comp/GET "/" [] (response "weather api wrapper"))
 (comp/GET "/weather/:city" [city] (w/weather city))
 (comp/GET "/weather" [] (w/all-weather))
 (route/not-found "Not found"))
