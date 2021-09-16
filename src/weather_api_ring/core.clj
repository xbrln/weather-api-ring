(ns weather-api-ring.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :as comp]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [weather-api-ring.db :as db]
            [environ.core :refer [env]])
  (:gen-class))

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
  (response {:status 200
             :body (db/all)}))

(comp/defroutes
 routes
 (comp/GET "/" [] "weather api wrapper")
 (comp/GET "/weather/:city" [city] (weather city))
 (comp/GET "/weather" [] (all-weather))
 (route/not-found "Not found"))

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
