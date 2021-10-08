(ns weather-api-ring.models.api-calls
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]))

; postgress in one line
(def db (env :database-url))

; postgress
; (def db {:dbtype "postgresql"
;          :dbname "weather_api_ring"
;          :host "localhost"
;          :user "weather_api_ring"
;          :password (env :db-password)})

(defn migrated?
  "Predicate to check if table is migrated"
  []
  (-> (sql/query db ["select count(*) from information_schema.tables where table_name='api_calls'"])
      first :count pos?))

(defn migrate
  "Migrate table"
  []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands db
                        (sql/create-table-ddl
                         :api_calls
                         [[:id :serial "PRIMARY KEY"]
                          [:city :varchar "NOT NULL"]
                          [:temperature :varchar "NOT NULL"]
                          [:created_at :timestamp
                           "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]]))
    (println " done")))

(defn save
  "Save weather api call"
  [c t]
  (sql/insert! db :api_calls {:city c :temperature t}))

(defn all
  "Get all records"
  []
  (sql/query db ["SELECT * FROM api_calls ORDER BY id DESC"]))

(comment
  (migrate))
