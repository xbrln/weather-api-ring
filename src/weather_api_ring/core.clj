(ns weather-api-ring.core
  (:require [weather-api-ring.server :as server]
            [weather-api-ring.db :as db]
            [environ.core :refer [env]])
  (:gen-class))

(defn -main
  [& args]
  (db/migrate)
  (let [port (Integer. (env :port))]
    (server/start-server port)))
